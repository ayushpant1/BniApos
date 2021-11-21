package com.example.bniapos.activities

import GridViewAdapter
import MenuLink
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.bniapos.R
import com.example.bniapos.activities.extensions.menuTypeToDrawable
import com.example.bniapos.callback.MenuListAdapterListener
import com.example.bniapos.enums.MenuType
import java.util.*
import kotlin.collections.ArrayList

class SubMenuActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    companion object {
        const val SUB_MENU_LIST = "SUB_MENU_LIST"
        const val MENU_LIST = "MENU_LIST"
        const val MENU_FILTER_LIST = "MENU_FILTER_LIST"
        const val MENU = "MENU"
        const val WORKFLOW_ID = "WORKFLOW_ID"
    }

    private var subMenuList: List<MenuLink> = ArrayList()
    private var filterMenuList: List<MenuLink> = ArrayList()
    private var menuList: List<MenuLink> = ArrayList()

    var menuStack: Stack<List<MenuLink>?>? = Stack()
    var spinnerStack: Stack<List<MenuLink>?>? = Stack()

    private lateinit var menu: MenuLink

    private var imageView: ImageView? = null

    private var spnMenu: Spinner? = null

    private var imgBack: ImageView? = null

    private var gridView: GridView? = null
    private var firstTimeLoad = true

    private var menuClicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_menu)
        subMenuList = intent.getSerializableExtra(SUB_MENU_LIST) as List<MenuLink>
        filterMenuList = intent.getSerializableExtra(MENU_FILTER_LIST) as List<MenuLink>
        menuList = intent.getSerializableExtra(MENU_LIST) as List<MenuLink>
        menu = intent.getSerializableExtra(MENU) as MenuLink

        imageView = findViewById(R.id.img_menu)

        imgBack = findViewById(R.id.img_back)

        imgBack?.setOnClickListener(this)

        spnMenu = findViewById(R.id.spn_menu)

        gridView = findViewById(R.id.gridView)
        setAdapter(subMenuList)


        spnMenu?.onItemSelectedListener = this


        val spnMenuData = filterMenuList.map { it.displayText }
        setAdapter(spnMenuData, spnMenu!!)
    }

    /**
     * method responsible to set the spinner adapter
     * @param spnData Spinner Data
     * @param spn Spinner Object
     */

    private fun setAdapter(spnData: List<String>?, spn: Spinner) {
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            R.layout.spinner_item,
            spnData!!
        )


        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spn.adapter = ad
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        if (menuStack!!.isNotEmpty()) {
            val subMenuList = menuStack!!.pop()
            setAdapter(subMenuList!!)

            val spnMenuList = spinnerStack?.pop()
            firstTimeLoad = true
            setAdapter(spnMenuList?.map { it.displayText }, spnMenu!!)
        } else {
            super.onBackPressed()
        }
    }

    private val adapterListener = object : MenuListAdapterListener {
        override fun onItemClick(position: Int, list: List<MenuLink>, isFirstTimeLoaded: Boolean) {
            menuStack!!.add(list)
            val menuId = list[position].id
            val subMenuList =
                menuList.filter { s -> s.parentId == menuId }
                    .sortedWith(compareBy { it.sortOrder })
            if (subMenuList.isNotEmpty()) {
                setAdapter(
                    subMenuList,
                )
                spinnerStack?.push(filterMenuList)
                filterMenuList = this@SubMenuActivity.subMenuList
                menuClicked = true
                setAdapter(this@SubMenuActivity.subMenuList.map { it.displayText }, spnMenu!!)
                this@SubMenuActivity.subMenuList = subMenuList
            } else {
                menuStack!!.pop()
                var intent: Intent? = null
                when (list[position].type.uppercase()) {
                    MenuType.BP.name -> {
                        intent = Intent(this@SubMenuActivity, BpControlsActivity::class.java)
                    }
                    MenuType.CP.name -> {
                        intent = Intent(this@SubMenuActivity, CpControlsActivity::class.java)
                    }
                }

                intent?.putExtra(MENU, list[position])
                intent?.putExtra(WORKFLOW_ID, list[position].workflowId)
                startActivity(intent)
            }
        }

        override fun onDefaultSelected(selectedItem: MenuLink?) {
            //do nothing for now
        }


    }

    /**
     * method responsible to set the gridView Adapter
     */
    private fun setAdapter(menuFilterList: List<MenuLink>) {
        gridView?.adapter = GridViewAdapter(this@SubMenuActivity, menuFilterList, adapterListener)
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (!firstTimeLoad) {
            val menuModel: MenuLink =
                menuList.first { it.displayText == p0?.getItemAtPosition(p2) }
            if (menuClicked) {
                menu = menuModel
                menuClicked = false
            }
            imageView?.setImageResource(menuModel.iconName.menuTypeToDrawable())
            val subMenuList =
                menuList.filter { s -> s.parentId == menuModel.id }
                    .sortedWith(compareBy { it.sortOrder })
            setAdapter(subMenuList)
            this@SubMenuActivity.subMenuList = subMenuList
        } else {
            firstTimeLoad = false
            var pos = 0
            for (i in filterMenuList.indices) {
                if (filterMenuList[i].displayText == menu.displayText) {
                    pos = i
                }
            }
            spnMenu?.setSelection(pos)
            menu = filterMenuList[pos]
            imageView?.setImageResource(menu.iconName.menuTypeToDrawable())
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Do nothing fot now
    }
}