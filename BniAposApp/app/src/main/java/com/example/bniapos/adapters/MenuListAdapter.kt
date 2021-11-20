/*
 * Copyright (c) 2020 All Rights Reserved, Ingenico SA.
 */
package com.example.bniapos.adapters

import MenuLink
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.activities.extensions.menuTypeToDrawable
import com.example.bniapos.callback.MenuListAdapterListener


internal class MenuListAdapter(
    private var listTransaction: List<MenuLink> = emptyList(),
    private val itemListener: MenuListAdapterListener? = null,
    private var defaultSelection: Int = 0
) : RecyclerView.Adapter<MenuListAdapter.TransactionDataTypeHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDataTypeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TransactionDataTypeHolder(
            layoutInflater.inflate(
                R.layout.menu_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listTransaction.size
    }

    lateinit var selectedItem: MenuLink
    override fun onBindViewHolder(
        holder: TransactionDataTypeHolder,
        position: Int
    ) {
        holder.init(listTransaction[position], defaultSelection == position)
        holder.itemView.setOnClickListener {
            defaultSelection = position
            selectedItem = listTransaction[position]
            itemListener?.onItemClick(position, listTransaction)
            notifyDataSetChanged()
        }

        if (defaultSelection != -1) {
            selectedItem = listTransaction[defaultSelection]
            itemListener?.onDefaultSelected(selectedItem)
        } else {
            itemListener?.onDefaultSelected(null)
        }

    }


    inner class TransactionDataTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun init(transactionType: MenuLink, isSelected: Boolean = false) {
            getResourceIconByMenuName(transactionType)?.let { resource ->
                itemView.findViewById<ImageView>(R.id.img_icon)?.let {
                    it.setImageResource(resource)
                }
            }
            itemView.findViewById<TextView>(R.id.txt_name)?.let {
                it.text =
                    transactionType.displayText
            }
        }


        private fun getResourceIconByMenuName(txnType: MenuLink): Int? {
            return txnType.iconName.uppercase().menuTypeToDrawable()
        }
    }
}
