package com.example.bniapos.activities.extensions

import com.example.bniapos.R
import com.example.bniapos.enums.MenuIconType
import com.example.bniapos.enums.SubMenuType

fun String.menuTypeToDrawable(): Int {
    return when (this) {
        MenuIconType.PAYMENT.name -> R.drawable.ic_ic___bayar_dan_beli
        MenuIconType.PURCHASE.name -> R.drawable.ic_ic___e_wallet
        MenuIconType.TRANSFER.name -> R.drawable.transfer
        MenuIconType.DIGITAL_FINANCE_SERVICE.name -> R.drawable.ic_ic___layanan_jasa_mitra
        MenuIconType.LAKUPANDAI.name -> R.drawable.ic_ic___layanan_keuangan
        MenuIconType.AGENT_MANAGEMENT.name -> R.drawable.ic_ic___pemesanan_barang_mitra
        MenuIconType.BNI_LIFE_MICRO_INSURANCE.name -> R.drawable.ic_ic___pencatatan_usaha
        MenuIconType.CDN.name -> R.drawable.ic_ic___pengajuan_produk
        MenuIconType.REMMITANCE.name -> R.drawable.ic_ic___program_pemerintah
        MenuIconType.Additional_Service.name -> R.drawable.ic_ic___layanan_keuangan
        MenuIconType.CR_DR.name -> R.drawable.ic_ic___layanan_jasa_mitra
        else -> R.drawable.transfer
    }
}


fun String.subMenuTypeToDrawable(): Int {
    return when (this) {
        SubMenuType.ACCOUNT_OPENING.name -> R.drawable.ic_ic___bayar_dan_beli
        SubMenuType.CASH_DEPOSIT.name -> R.drawable.ic_antar_bank
        SubMenuType.CASH_WITHDRAWAL.name -> R.drawable.ic_virtual_account_billing_1
        SubMenuType.CREATE_OTP.name -> R.drawable.ic_kiriman
        else -> R.drawable.ic_ic___bayar_dan_beli
    }
}
