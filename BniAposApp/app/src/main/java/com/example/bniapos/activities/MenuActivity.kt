package com.example.bniapos.activities

import GridSpacingItemDecoration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.adapters.MenuAdapter
import com.example.bniapos.callback.MenuAdapterListener
import com.example.bniapos.models.MenuList
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class MenuActivity : AppCompatActivity() {
    private var menuList: List<MenuList>? = ArrayList()
    private var gridview: RecyclerView? = null
    private var menuFilterList: List<MenuList>? = ArrayList()
    private lateinit var llBack: LinearLayout
    var stack: Stack<List<MenuList>?>? = Stack()
    private var subMenuListImpl: List<MenuList> = ArrayList()

    private val txnType = "txn"
    private val actionType = "action"
    private val nonSelectedPosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        gridview = findViewById(R.id.grid_menu)
        val json = loadJSONFromAsset()
        menuList = Gson().fromJson(json, Array<MenuList>::class.java).asList()

        menuFilterList = menuList!!.filter { s ->
            s.parentId == 0 && isChildAvailable(
                menuList!!, s
            )
        }.sortedWith(compareBy { it.sortOrder })

        llBack = findViewById(R.id.ll_back)
        llBack.visibility = View.GONE
        var mLastClickTime: Long = 0L;
        llBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();



                if (!stack!!.empty()) {
                    val popMenuList = stack!!.pop()
                    if (stack!!.empty()) {
                        llBack.visibility = View.GONE
                    }
                    if (popMenuList!!.size > 1) {
                        val newData =
                            popMenuList.filter { s -> s.type == txnType }
                        if (newData.size > 0) {
                            setAdapter(popMenuList, nonSelectedPosition)
                        } else {
                            setAdapter(popMenuList, nonSelectedPosition)
                        }
                    } else {
                        if (popMenuList.size > 0) {
                            if (popMenuList.get(0).type == actionType) {
                                setAdapter(popMenuList, nonSelectedPosition)
                            } else {
                                setAdapter(popMenuList, nonSelectedPosition)
                            }
                        }


                    }
                }

                // Do some work here
            }

        })
        gridview?.layoutManager = GridLayoutManager(this, 3)
        /* val spanCount = 2
         val spacing = 150
         val includeEdge = false
         gridview?.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))*/
        setAdapter(menuFilterList!!, -1)
    }

    private fun setAdapter(menuFilterList: List<MenuList>, defaultPosition: Int) {
        gridview?.adapter = MenuAdapter(menuFilterList!!, adapterListener, defaultPosition)
    }

    private fun isChildAvailable(menuList: List<MenuList>, menu: MenuList): Boolean {
        if (menu.type == actionType) {
            val updatedMenuList = menuList.filter { it.parentId == menu.id }
            return !updatedMenuList.isNullOrEmpty()
        }
        return true
    }

    fun loadJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
            val `is`: InputStream = getAssets().open("menu_list.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        val jsonObject = JSONObject(json!!)
        val jsonArray = jsonObject.getJSONArray("menuList")

        return jsonArray.toString()
    }

    val adapterListener = object : MenuAdapterListener {
        override fun onItemClick(position: Int, list: List<MenuList>, isFirstTimeLoaded: Boolean) {
            if (list[position].type == actionType)
                stack!!.add(list)
            var menuId = list[position].id
            var menuName = list[position].displayText
            subMenuListImpl =
                menuList!!.filter { s -> s.parentId == menuId && (s.type == txnType || s.type == actionType) }
                    .sortedWith(compareBy({ it.sortOrder }))

            if (subMenuListImpl.size > 1) {
                if (subMenuListImpl.filter { s -> s.type == txnType }.size > 0) {
                    setAdapter(
                        subMenuListImpl,
                        nonSelectedPosition
                    )
                } else {
                    setAdapter(
                        subMenuListImpl,
                        nonSelectedPosition
                    )
                }
            } else {
                if (subMenuListImpl.size > 0) {
                    if (subMenuListImpl.get(0).type == actionType) {
                        setAdapter(
                            subMenuListImpl,
                            nonSelectedPosition
                        )
                    } else {
                        setAdapter(
                            subMenuListImpl,
                            nonSelectedPosition
                        )
                    }
                } else {

                }
            }
            if (subMenuListImpl!!.size > 0 || list.get(position).parentId != 0) {
                llBack.visibility = View.VISIBLE
            } else {
                llBack.visibility = View.GONE
            }

        }

        override fun onDefaultSelected(selectedItem: MenuList?) {

        }


    }
}