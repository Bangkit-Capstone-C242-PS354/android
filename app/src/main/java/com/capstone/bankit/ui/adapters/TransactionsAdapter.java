package com.capstone.bankit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.bankit.R;
import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.databinding.ItemTransactionsRowBinding;
import com.capstone.bankit.utils.Constants;

import java.util.Objects;


public class TransactionsAdapter extends ListAdapter<TransactionModel, TransactionsAdapter.ViewHolder> {
    public TransactionsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionsRowBinding binding = ItemTransactionsRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transactionModel = getItem(position);
        if (transactionModel != null) {
            String date = Constants.convertLongToDate(transactionModel.getDate());
            String amount = Constants.formatToRupiah(transactionModel.getAmount());
            int image = Constants.mapTransactionTypeToIcon(transactionModel.getType());

            holder.binding.ivTransaction.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), image));
            holder.binding.tvTime.setText(date);
            holder.binding.tvType.setText(transactionModel.getType());
            holder.binding.tvDesc.setText(transactionModel.getDesc());

            if (transactionModel.getFlagExpenseIncome().equals(Constants.FLAG_EXPENSE)) {
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.primaryRed));
                holder.binding.tvAmount.setText("- " + amount);
            } else {
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.primaryGreen));
                holder.binding.tvAmount.setText("+ " + amount);
            }

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTransactionsRowBinding binding;

        public ViewHolder(@NonNull ItemTransactionsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final DiffUtil.ItemCallback<TransactionModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TransactionModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull TransactionModel oldStory,
                                               @NonNull TransactionModel newStory) {
                    return oldStory.equals(newStory);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TransactionModel oldStory,
                                                  @NonNull TransactionModel newStory) {
                    return Objects.equals(oldStory.getId(), newStory.getId());
                }
            };
}
