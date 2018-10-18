package tip.dgts.eventapp.ui.eventSimple.sessions

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemSessionBinding
import tip.dgts.eventapp.model.data.Schedule
import tip.dgts.eventapp.ui.eventSimple.sessions.SessionsView

/**
 * Created by Sen on 1/26/2017.
 */

class SessionListAdapter(private val view: SessionsView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Schedule>
    private var loading: Boolean = false

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSessionBinding>(
                LayoutInflater.from(parent.context), R.layout.item_session, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view


        Glide.with(viewHolder.itemView.context).load(Endpoints.IMAGE_LINK + item.image!!).into(viewHolder.binding.imageView)

    }

    inner class ViewHolder(val binding: ItemSessionBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Schedule>) {
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
