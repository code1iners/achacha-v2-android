package com.codeliner.achacha.ui.accounts.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeliner.achacha.data.accounts.Account
import com.codeliner.achacha.databinding.ItemAccountBinding
import timber.log.Timber

class AccountListAdapter(
        val clickListener: AccountClickListener
): ListAdapter<Account, AccountListAdapter.AccountListViewHolder>(AccountListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        return AccountListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        val item = getItem(position)
        when (itemCount > 0) {
            true -> { holder.bind(item, clickListener) }
            false -> { Timber.w("ItemCount is zero") }
        }
    }

    class AccountListViewHolder private constructor(val binding: ItemAccountBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Account, clickListener: AccountClickListener) {
            binding.account = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AccountListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAccountBinding.inflate(layoutInflater, parent, false)
                return AccountListViewHolder(binding)
            }
        }
    }
}

interface AccountClickListener {
    fun onClick(account: Account)
}