package tip.dgts.eventapp.ui.ticketList

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemTicketBinding
import tip.dgts.eventapp.model.data.Ticket

/**
 * Created by Sen on 1/26/2017.
 */

class TicketListAdapter(private val view: TicketsListView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Ticket>

    init {
        this.list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemTicketBinding>(
                LayoutInflater.from(parent.context), R.layout.item_ticket, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view

    }

    inner class ViewHolder(val binding: ItemTicketBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Ticket>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return list[position].id!!.toLong()
    }
}
