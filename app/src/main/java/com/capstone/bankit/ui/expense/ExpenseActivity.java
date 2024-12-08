package com.capstone.bankit.ui.expense;

import static com.capstone.bankit.utils.Constants.expensesTypes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.bankit.databinding.ActivityExpenseBinding;
import com.capstone.bankit.ui.customview.ReceiptBottomSheetFragment;
import com.capstone.bankit.ui.expensedetail.ExpenseDetailActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseActivity extends AppCompatActivity {
    private ActivityExpenseBinding binding;

    private String imagePathLocation;

    private ExpenseViewModel expenseViewModel;

    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    finish();
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        expenseViewModel.selectedImageFile.postValue(reduceFileSize(toFile(uri)));
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    File file = new File(imagePathLocation);
                    Bitmap bmp = rotateImage(BitmapFactory.decodeFile(file.getPath()), imagePathLocation);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    expenseViewModel.selectedImageFile.postValue(reduceFileSize(file));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        observeSelectedImage();

        setListeners();
    }

    private void observeSelectedImage() {
        expenseViewModel.selectedImageFile.observe(this, file -> {
            if (file != null) {
                binding.layoutReceipt.setVisibility(View.VISIBLE);
                binding.ivReceipt.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            } else {
                binding.layoutReceipt.setVisibility(View.GONE);
            }
        });

        expenseViewModel.uploadResult.observe(this, result -> {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        });
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.edDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExpenseActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        binding.edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                expensesTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        binding.edReceipt.setOnClickListener(v -> {
            openReceiptBottomSheet();
        });

        binding.btnSave.setOnClickListener(v -> {
            File file = expenseViewModel.selectedImageFile.getValue();
            if (file == null) {
                Toast.makeText(this, "Add receipt first!", Toast.LENGTH_SHORT).show();
            } else {
                expenseViewModel.uploadReceipt(file);
            }
        });
    }

    private void openReceiptBottomSheet() {
        ReceiptBottomSheetFragment bottomSheetFragment = new ReceiptBottomSheetFragment();
        bottomSheetFragment.setOnButtonClickListener(new ReceiptBottomSheetFragment.OnImagePickerClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void cameraClick() {
                if (checkImagePermission()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        File file = File.createTempFile(
                                new SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                                        .format(System.currentTimeMillis()),
                                ".jpg",
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        );
                        imagePathLocation = file.getAbsolutePath();
                        Uri photoUri = FileProvider.getUriForFile(
                                ExpenseActivity.this,
                                getApplicationContext().getPackageName(),
                                file
                        );
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                        cameraLauncher.launch(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(
                            ExpenseActivity.this,
                            REQUIRED_CAMERA_PERMISSION,
                            REQUEST_CODE_PERMISSIONS
                    );
                }
            }

            @Override
            public void galleryClick() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent chooser = Intent.createChooser(intent, "Pick an Image");
                galleryLauncher.launch(chooser);
            }
        });
        bottomSheetFragment.show(getSupportFragmentManager(), "ReceiptBottomSheet");
    }

    private Bitmap rotateImage(Bitmap bitmap, String path) {
        Matrix matrix = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.setRotate(90f);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.setRotate(180f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private File toFile(Uri uri) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(
                    "Image_" + getPackageName() + "_" + System.currentTimeMillis(),
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
            try (InputStream input = getContentResolver().openInputStream(uri);
                 OutputStream output = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while (true) {
                    assert input != null;
                    if (!((length = input.read(buffer)) > 0)) break;
                    output.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private File reduceFileSize(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        int compressQuality = 100;
        int streamLength;

        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
        } while (streamLength > 1000000);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private boolean checkImagePermission() {
        for (String permission : REQUIRED_CAMERA_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private static final String[] REQUIRED_CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    private static final int REQUEST_CODE_PERMISSIONS = 100;
}