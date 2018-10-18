package tip.dgts.eventapp.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemTextBinding

/**
 * Created by Sen on 1/26/2017.
 */

class StringListAdapter(private val list: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var loading: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemTextBinding = DataBindingUtil.inflate<ItemTextBinding>(
                LayoutInflater.from(parent.context), R.layout.item_text, parent, false)
        return ViewHolder(itemTextBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.itemTextBinding.textContent.text = list[position].trim { it <= ' ' }

    }

    inner class ViewHolder(val itemTextBinding: ItemTextBinding) : RecyclerView.ViewHolder(itemTextBinding.root)


    fun setList(list: List<String>) {
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
