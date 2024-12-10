package com.capstone.bankit.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bankit.R
import com.capstone.bankit.data.models.DataItem
import com.capstone.bankit.databinding.ItemTransactionsRowBinding
import com.capstone.bankit.ui.expensedetail.ExpenseDetailActivity
import com.capstone.bankit.utils.Constants.convertDate
import com.capstone.bankit.utils.Constants.convertLongToDate
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.Constants.mapTransactionTypeToIcon

class TransactionAdapter(private var tabFlag: Int) : ListAdapter<DataItem, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {

    // Method to update the tabFlag if it changes
    @SuppressLint("NotifyDataSetChanged")
    fun updateTabFlag(tabFlag: Int) {
        this.tabFlag = tabFlag
        notifyDataSetChanged()  // Refresh the entire list when tabFlag changes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionsRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expenseItem = getItem(position)
        if (expenseItem != null) {
            // Format the date using the provided utility function (assuming it's in seconds)
            val amount = formatToRupiah(expenseItem.amount ?: 0.0)
            val image = mapTransactionTypeToIcon(expenseItem.category)

            holder.binding.ivTransaction.setImageDrawable(
                ContextCompat.getDrawable(holder.binding.root.context, image)
            )

            // Bind the data to the views
            holder.binding.tvTime.text = convertDate(expenseItem.date)
            holder.binding.tvType.text = expenseItem.category
            holder.binding.tvDesc.text = expenseItem.note

            // Format the amount based on tabFlag
            if (!expenseItem.type.isNullOrEmpty()) {
                if (expenseItem.type == "EXPENSE") {
                    holder.binding.tvAmount.text = "- $amount"
                    holder.binding.tvAmount.setTextColor(
                        ContextCompat.getColor(holder.binding.root.context, R.color.primaryRed)
                    )
                } else {
                    holder.binding.tvAmount.text = "+ $amount"
                    holder.binding.tvAmount.setTextColor(
                        ContextCompat.getColor(holder.binding.root.context, R.color.primaryGreen)
                    )
                }
            } else {
                if (tabFlag == 2) {
                    holder.binding.tvAmount.text = "- $amount"
                    holder.binding.tvAmount.setTextColor(
                        ContextCompat.getColor(holder.binding.root.context, R.color.primaryRed)
                    )
                } else {
                    holder.binding.tvAmount.text = "+ $amount"
                    holder.binding.tvAmount.setTextColor(
                        ContextCompat.getColor(holder.binding.root.context, R.color.primaryGreen)
                    )
                }
            }


            holder.binding.btnDetail.setOnClickListener { view ->
                val context = view.context
                val intent = Intent(context, ExpenseDetailActivity::class.java).apply {
                    putExtra("expenseId", expenseItem.id)
                    if (tabFlag != 2 && expenseItem.type != "EXPENSE"){
                        putExtra("navigate", "INCOME")
                    }
                }
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(val binding: ItemTransactionsRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(
                    oldExpense: DataItem,
                    newExpense: DataItem
                ): Boolean {
                    return oldExpense.id == newExpense.id
                }

                override fun areContentsTheSame(
                    oldExpense: DataItem,
                    newExpense: DataItem
                ): Boolean {
                    return oldExpense == newExpense
                }
            }
    }
}
