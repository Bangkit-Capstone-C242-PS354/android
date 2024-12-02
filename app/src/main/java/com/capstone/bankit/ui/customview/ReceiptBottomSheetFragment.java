package com.capstone.bankit.ui.customview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.capstone.bankit.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReceiptBottomSheetFragment extends BottomSheetDialogFragment {
    private OnImagePickerClickListener listener;

    public interface OnImagePickerClickListener {
        void cameraClick();
        void galleryClick();
    }

    public void setOnButtonClickListener(OnImagePickerClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_bottom_sheet_fragment, container, false);

        view.findViewById(R.id.btn_scan).setOnClickListener(v -> {
            if (listener != null) listener.cameraClick();
            dismiss();
        });

        view.findViewById(R.id.btn_gallery).setOnClickListener(v -> {
            if (listener != null) listener.galleryClick();
            dismiss();
        });

        return view;
    }
}

