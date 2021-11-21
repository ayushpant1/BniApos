package com.example.bniapos.activities.ui.home

import MenuLink
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.activities.SubMenuActivity
import com.example.bniapos.adapters.MenuListAdapter
import com.example.bniapos.callback.MenuListAdapterListener
import com.example.bniapos.databinding.FragmentHomeBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private var menuList: List<MenuLink>? = ArrayList()
    private var gridview: RecyclerView? = null
    private var menuFilterList: List<MenuLink>? = ArrayList()
    private var subMenuListImpl: List<MenuLink> = ArrayList()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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


        gridview = binding.gridMenu

        val json = loadJSONFromAsset()
        menuList = Gson().fromJson(json, Array<MenuLink>::class.java).asList()

        menuFilterList = menuList!!.filter { s ->
            s.parentId == 0
        }.sortedWith(compareBy { it.sortOrder })

        gridview?.layoutManager = GridLayoutManager(root.context, 3)
        setAdapter(menuFilterList!!, -1)
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
}