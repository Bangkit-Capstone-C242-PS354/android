package com.capstone.bankit.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bankit.databinding.ItemAnalyticsRowBinding
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.Constants.mapTransactionTypeToIcon
import com.capstone.bankit.data.models.DataItem

class AnalyticsAdapter : ListAdapter<DataItem, AnalyticsAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var totalAmount: Double = 0.0

    @SuppressLint("NotifyDataSetChanged")
    fun setTotalAmount(amount: Double) {
        totalAmount = amount
        notifyDataSetChanged() // Ensure that the percentages are updated
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnalyticsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)

        // Make sure dataItem is not null
        if (dataItem != null) {
            // Safely extract category and amount
            val category = dataItem.category ?: "Unknown"
            val amount = dataItem.amount?.let { formatToRupiah(it) } ?: "0"
            val image = mapTransactionTypeToIcon(category)

            // Set icon for transaction type (if category is known)
            holder.binding.ivTransaction.setImageDrawable(
                ContextCompat.getDrawable(holder.binding.root.context, image)
            )

            // Calculate percentage if totalAmount is greater than 0
            val percentage = if (totalAmount > 0) {
                val categoryAmount = dataItem.amount ?: 0.0
                ((categoryAmount / totalAmount) * 100).toInt()
            } else {
                0
            }

            // Update UI
            holder.binding.tvType.text = category
            holder.binding.tvPercentage.text = "$percentage%" // Display percentage
            holder.binding.tvAmount.text = amount
        }
    }

    class ViewHolder(val binding: ItemAnalyticsRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
