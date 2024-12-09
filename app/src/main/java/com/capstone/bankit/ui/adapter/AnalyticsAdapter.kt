package com.capstone.bankit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bankit.data.models.TransactionModel
import com.capstone.bankit.databinding.ItemAnalyticsRowBinding
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.Constants.mapTransactionTypeToIcon

class AnalyticsAdapter : ListAdapter<TransactionModel, AnalyticsAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnalyticsRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactionModel = getItem(position)
        if (transactionModel != null) {
            val amount = formatToRupiah(transactionModel.amount)
            val image = mapTransactionTypeToIcon(transactionModel.type)

            holder.binding.ivTransaction.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.binding.root.context,
                    image
                )
            )
            holder.binding.tvType.text = transactionModel.type
            holder.binding.tvPercentage.text = transactionModel.percentage.toString() + "%"
            holder.binding.tvAmount.text = amount
        }
    }

    class ViewHolder(val binding: ItemAnalyticsRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<TransactionModel> =
            object : DiffUtil.ItemCallback<TransactionModel>() {
                override fun areItemsTheSame(
                    oldStory: TransactionModel,
                    newStory: TransactionModel,
                ): Boolean {
                    return oldStory == newStory
                }

                override fun areContentsTheSame(
                    oldStory: TransactionModel,
                    newStory: TransactionModel,
                ): Boolean {
                    return oldStory.id == newStory.id
                }
            }
    }
}