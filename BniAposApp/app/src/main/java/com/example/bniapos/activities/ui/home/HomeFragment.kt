package com.example.bniapos.activities.ui.home

import MenuLink
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.activities.SubMenuActivity
import com.example.bniapos.activities.ui.SettingsActivity
import com.example.bniapos.adapters.MenuListAdapter
import com.example.bniapos.callback.MenuListAdapterListener
import com.example.bniapos.databinding.FragmentHomeBinding
import com.example.bniapos.utils.Configuration
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener {
    private var menuList: List<MenuLink>? = ArrayList()
    private var gridview: RecyclerView? = null
    private var menuFilterList: List<MenuLink>? = ArrayList()
    private var subMenuListImpl: List<MenuLink> = ArrayList()
    private var imgSettings: ImageView? = null

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null


    private var allowedPaymentType: String =
        "2500116,2500111,2500117"//replace with allowedPaymentType


    private var allowedTransactionType: String = "429,"//replaced with allowedTransactionType

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imgSettings = binding.imgSettings
        gridview = binding.gridMenu

        val json = Configuration.getMenuConfig(root.context)
        menuList = Gson().fromJson(json, Array<MenuLink>::class.java).asList()
        val filterMenuList: MutableList<MenuLink>? = ArrayList()
        filterMenuList?.addAll(menuList!!.filter {
            it.type.equals("BP", true) &&
                    it.txnType.toString() in allowedPaymentType.split(
                ","
            )
        })

        filterMenuList?.addAll(menuList!!.filter {
            it.type.equals("CP", true) &&
                    it.txnType.toString() in allowedTransactionType.split(
                ","
            )
        })

        menuList = filterMenuList
        menuFilterList = filterMenuList!!.filter { s ->
            s.parentId == 0
        }.sortedWith(compareBy { it.sortOrder })


        gridview?.layoutManager = GridLayoutManager(root.context, 3)
        setAdapter(menuFilterList!!, -1)

        imgSettings?.setOnClickListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * load menu from the assets
     */

    private fun loadJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        val json: String? = try {
            val `is`: InputStream = activity?.assets!!.open("menu.json")
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
        val jsonArray = jsonObject.getJSONObject("Menus").getJSONArray("MenuLink")

        return jsonArray.toString()
    }


    private fun setAdapter(menuFilterList: List<MenuLink>, defaultPosition: Int) {
        gridview?.adapter = MenuListAdapter(menuFilterList!!, adapterListener, defaultPosition)
    }


    private val adapterListener = object : MenuListAdapterListener {
        override fun onItemClick(position: Int, list: List<MenuLink>, isFirstTimeLoaded: Boolean) {
            val menuId = list[position].id
            subMenuListImpl =
                menuList!!.filter { s -> s.parentId == menuId }
                    .sortedWith(compareBy { it.sortOrder })
            val intent = Intent(activity, SubMenuActivity::class.java)
            intent.putExtra(SubMenuActivity.SUB_MENU_LIST, subMenuListImpl as Serializable)
            intent.putExtra(SubMenuActivity.MENU_FILTER_LIST, menuFilterList as Serializable)
            intent.putExtra(SubMenuActivity.MENU_LIST, menuList as Serializable)
            intent.putExtra(SubMenuActivity.MENU, list[position] as Serializable)
            startActivity(intent)
        }

        override fun onDefaultSelected(selectedItem: MenuLink?) {

        }


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_settings -> {
                performSettingFunction()
            }
        }

    }

    private fun performSettingFunction() {
        val intent = Intent(activity, SettingsActivity::class.java)
        startActivity(intent)
    }
}