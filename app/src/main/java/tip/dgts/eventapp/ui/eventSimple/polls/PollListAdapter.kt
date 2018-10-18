package tip.dgts.eventapp.ui.eventSimple.polls

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemPollBinding
import tip.dgts.eventapp.model.data.Nominee
import tip.dgts.eventapp.model.data.Poll

/**
 * Created by Sen on 1/26/2017.
 */

class PollListAdapter(private val view: PollView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Poll>

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPollBinding>(
                LayoutInflater.from(parent.context), R.layout.item_poll, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view

        for (nominee in item.nominees!!) {
            if (nominee.voted!!) {
                viewHolder.binding.imageView.visibility = View.VISIBLE
            }
        }

        //Glide.with(viewHolder.itemView.getContext()).load(Endpoints.IMAGE_LINK + item.getImage()).into(viewHolder.binding.imageView);

    }

    inner class ViewHolder(internal val binding: ItemPollBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Poll>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setSelected(position: Int) {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
