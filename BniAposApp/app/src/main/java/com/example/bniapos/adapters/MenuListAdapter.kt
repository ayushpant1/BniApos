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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.models.MenuList
import com.example.bniapos.callback.MenuAdapterListener
import com.example.bniapos.callback.MenuListAdapterListener
import com.example.bniapos.enums.MenuType


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
            return when (txnType.iconName.uppercase()) {
                MenuType.PAYMENT.name -> R.drawable.ic_ic___bayar_dan_beli
                MenuType.PURCHASE.name -> R.drawable.ic_ic___e_wallet
                MenuType.TRANSFER.name -> R.drawable.transfer
                MenuType.DIGITAL_FINANCE_SERVICE.name -> R.drawable.ic_ic___layanan_jasa_mitra
                MenuType.LAKUPANDAI.name -> R.drawable.ic_ic___layanan_keuangan
                MenuType.AGENT_MANAGEMENT.name -> R.drawable.ic_ic___pemesanan_barang_mitra
                MenuType.BNI_LIFE_MICRO_INSURANCE.name -> R.drawable.ic_ic___pencatatan_usaha
                MenuType.CDN.name -> R.drawable.ic_ic___pengajuan_produk
                MenuType.REMMITANCE.name -> R.drawable.ic_ic___program_pemerintah
                MenuType.Additional_Service.name -> R.drawable.ic_ic___layanan_keuangan
                MenuType.CR_DR.name -> R.drawable.ic_ic___layanan_jasa_mitra
                else -> R.drawable.transfer
            }

        }
    }
}
