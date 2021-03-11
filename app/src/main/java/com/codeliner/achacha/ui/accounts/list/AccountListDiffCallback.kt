package com.codeliner.achacha.ui.accounts.list

import androidx.recyclerview.widget.DiffUtil
import com.codeliner.achacha.data.accounts.Account

class AccountListDiffCallback: DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}