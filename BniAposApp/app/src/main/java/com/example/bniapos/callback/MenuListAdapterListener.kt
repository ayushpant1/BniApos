/*
 * Copyright (c) 2020 All Rights Reserved, Ingenico SA.
 */
package com.example.bniapos.callback

import MenuLink
import com.example.bniapos.models.MenuList

/*
 * ItemAdapterListener is used as callback for event happened in each item of adapter.
 */
interface MenuListAdapterListener {

    /*
     * onItemClick callback function for each item of adapter.
     * @param position position of item.
     */
    fun onItemClick(position: Int, list: List<MenuLink>, isFirstTimeLoaded: Boolean = false)
    fun onDefaultSelected(selectedItem: MenuLink?)
}
