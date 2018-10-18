package tip.dgts.eventapp.ui.eventSimple

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemLocationBinding
import tip.dgts.eventapp.model.data.Location

/**
 * Created by Sen on 1/26/2017.
 */

class LocationListAdapter(private val view: EventSimpleDetailView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Location>
    private var loading: Boolean = false

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemLocationBinding>(
                LayoutInflater.from(parent.context), R.layout.item_location, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.binding.item = list[position]
        viewHolder.binding.view = view

    }

    inner class ViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Location>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
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
