import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bniapos.R
import com.example.bniapos.activities.extensions.menuTypeToDrawable
import com.example.bniapos.activities.extensions.subMenuTypeToDrawable
import com.example.bniapos.callback.MenuListAdapterListener

internal class GridViewAdapter(
    private val context: Context,
    private val subMenuList: List<MenuLink>,
    private val itemListener: MenuListAdapterListener? = null,
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    override fun getCount(): Int {
        return subMenuList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.rowitem, null)
        }
        imageView = convertView!!.findViewById(R.id.img_sub_menu)
        textView = convertView.findViewById(R.id.tv_sub_menu)
        imageView.setImageResource(
            subMenuList[position].iconName.uppercase().subMenuTypeToDrawable()
        )
        textView.text = subMenuList[position].displayText

        convertView.setOnClickListener {
            itemListener?.onItemClick(position, subMenuList)
            notifyDataSetChanged()
        }

        return convertView
    }
}