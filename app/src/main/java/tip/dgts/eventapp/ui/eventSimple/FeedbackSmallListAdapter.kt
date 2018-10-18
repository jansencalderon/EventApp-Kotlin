package tip.dgts.eventapp.ui.eventSimple

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemFeedbackSmallBinding
import tip.dgts.eventapp.model.data.Feedback

/**
 * Created by Sen on 1/26/2017.
 */

class FeedbackSmallListAdapter(private val view: EventSimpleDetailView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Feedback>
    private var loading: Boolean = false

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemFeedbackSmallBinding>(
                LayoutInflater.from(parent.context), R.layout.item_feedback_small, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view


        Glide.with(viewHolder.itemView.context)
                .load(Endpoints.IMAGE_LINK + item.otherUser!!.image)
                .placeholder(R.color.lightestGray)
                .error(R.color.lightestGray)
                .dontAnimate()
                .into(viewHolder.binding.userImage)


    }

    inner class ViewHolder(val binding: ItemFeedbackSmallBinding) : RecyclerView.ViewHolder(binding.root)


    fun setList(list: List<Feedback>) {
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
