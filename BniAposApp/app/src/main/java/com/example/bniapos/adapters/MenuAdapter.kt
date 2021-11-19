/*
 * Copyright (c) 2020 All Rights Reserved, Ingenico SA.
 */
package com.example.bniapos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.models.MenuList
import com.example.bniapos.callback.MenuAdapterListener


internal class MenuAdapter(
    private var listTransaction: List<MenuList> = emptyList(),
    private val itemListener: MenuAdapterListener? = null,
    private var defaultSelection: Int = 0
) : RecyclerView.Adapter<MenuAdapter.TransactionDataTypeHolder>() {


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

    lateinit var selectedItem: MenuList
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
        fun init(transactionType: MenuList, isSelected: Boolean = false) {
            val defaultColor = ContextCompat.getColor(
                itemView.context,
                if (isSelected) R.color.red else R.color.dark_gray
            )
            getResourceIconByMenuName(transactionType)?.let { resource ->
                itemView.findViewById<ImageView>(R.id.img_icon)?.let {
                    it.setImageResource(resource)
                    it.setColorFilter(defaultColor)
                }
            }

            itemView.findViewById<TextView>(R.id.txt_name)?.let {
                it.text =
                    transactionType.displayText
                it.setTextColor(defaultColor)
            }
        }


        private fun getResourceIconByMenuName(txnType: MenuList): Int? {
            return when (txnType.iconName.uppercase()) {
                "ALIPAY" -> R.drawable.ic_alipay
                "SALE" -> R.drawable.credit_card_sale
                "QR_SCAN" -> R.drawable.ic_merchant_scan
                "SHOW_QR" -> R.drawable.ic_show_qr_code

                else -> R.drawable.credit_card_sale
            }

        }
    }
}
