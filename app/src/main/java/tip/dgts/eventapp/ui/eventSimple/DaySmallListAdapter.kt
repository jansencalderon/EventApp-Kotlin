package tip.dgts.eventapp.ui.eventSimple

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.databinding.ItemDaySmallBinding
import tip.dgts.eventapp.model.data.Timestamp
import tip.dgts.eventapp.ui.eventSimple.dayDetail.DayDetailActivity

/**
 * Created by Sen on 1/26/2017.
 */

class DaySmallListAdapter(private val view: EventSimpleDetailView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Timestamp>
    private var loading: Boolean = false
    private var reserved: Boolean = false

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemDaySmallBinding>(
                LayoutInflater.from(parent.context), R.layout.item_day_small, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view
        viewHolder.binding.dayLabel.text = "Day " + (position + 1)
        viewHolder.binding.dayLayout.setOnClickListener {
            if (reserved) {
                val intent = Intent(viewHolder.itemView.context, DayDetailActivity::class.java)
                intent.putExtra(Timestamp.timestampId, item.id)
                intent.putExtra(Constants.TOOLBAR_TITLE, "Day " + (position + 1))
                viewHolder.itemView.context.startActivity(intent)
            } else {
                viewHolder.binding.view?.showSnackbar("PLEASE RESERVE FIRST TO VIEW DETAILS")
            }
        }

    }

    inner class ViewHolder(val binding: ItemDaySmallBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Timestamp>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setReserved(reserved: Boolean){
        this.reserved = reserved
        notifyDataSetChanged()
    }



    fun setLoading(loading: Boolean) {
        this.loading = loading
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }
}
