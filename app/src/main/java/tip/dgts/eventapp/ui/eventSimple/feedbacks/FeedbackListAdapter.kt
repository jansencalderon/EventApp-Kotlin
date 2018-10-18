package tip.dgts.eventapp.ui.eventSimple.feedbacks

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemFeedbackBinding
import tip.dgts.eventapp.model.data.Feedback

/**
 * Created by Sen on 1/26/2017.
 */

class FeedbackListAdapter(private val view: FeedbacksView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Feedback>
    private var loading: Boolean = false

    init {
        list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemFeedbackBinding>(
                LayoutInflater.from(parent.context), R.layout.item_feedback, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder

        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view


        Glide.with(viewHolder.itemView.context)
                .load(Endpoints.IMAGE_LINK + item.otherUser!!.image!!)
                .placeholder(R.color.lightestGray)
                .dontAnimate()
                .error(R.color.lightestGray)
                .into(viewHolder.binding.userImage)

    }

    inner class ViewHolder(internal val binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root)


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
