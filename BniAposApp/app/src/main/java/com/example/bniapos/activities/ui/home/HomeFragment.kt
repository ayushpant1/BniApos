package com.example.bniapos.activities.ui.home

import MenuLink
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bniapos.R
import com.example.bniapos.activities.MainActivity
import com.example.bniapos.adapters.MenuListAdapter
import com.example.bniapos.callback.MenuListAdapterListener
import com.example.bniapos.databinding.FragmentHomeBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private var menuList: List<MenuLink>? = ArrayList()
    private var gridview: RecyclerView? = null
    private var menuFilterList: List<MenuLink>? = ArrayList()
    var stack: Stack<List<MenuLink>?>? = Stack()
    private var subMenuListImpl: List<MenuLink> = ArrayList()

    private val txnType = "CT"
    private val actionType = "BP"
    private val nonSelectedPosition = -1
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
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

    fun loadJSONFromAsset(): String? {
        val charset: Charset = Charsets.UTF_8
        var json: String? = null
        json = try {
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


    val adapterListener = object : MenuListAdapterListener {
        override fun onItemClick(position: Int, list: List<MenuLink>, isFirstTimeLoaded: Boolean) {
            stack!!.add(list)
            val menuId = list[position].id
            val menuName = list[position].displayText
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
                    stack!!.pop()
                    var intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("menuName", menuName)
                    intent.putExtra("menuId", menuId)
                    startActivity(intent)
                }
            }

        }

        override fun onDefaultSelected(selectedItem: MenuLink?) {

        }


    }
}