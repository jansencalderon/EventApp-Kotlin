package tip.dgts.eventapp.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemInterestBinding
import tip.dgts.eventapp.model.data.Interest

/**
 * Created by Sen on 1/26/2017.
 */

class InterestListAdapter(private val list: MutableList<Interest>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var loading: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemInterestBinding>(
                LayoutInflater.from(parent.context), R.layout.item_interest, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item

    }

    inner class ViewHolder(val binding: ItemInterestBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Interest>) {
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

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
