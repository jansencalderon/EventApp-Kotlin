package tip.dgts.eventapp.ui.profile

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemEventLikedProfileBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Location
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Timestamp
import tip.dgts.eventapp.ui.eventSimple.EventSimpleDetailActivity
import tip.dgts.eventapp.utils.DateTimeUtils

/**
 * Created by Sen on 1/26/2017.
 */

class ProfileLikedEventListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Event>

    init {
        this.list = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemEventLikedProfileBinding>(
                LayoutInflater.from(parent.context), R.layout.item_event_liked_profile, parent, false)
        return ViewHolder(binding)
    }


    inner class ViewHolder(internal val binding: ItemEventLikedProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ProfileLikedEventListAdapter.ViewHolder
        val item = list[position]
        viewHolder.binding.event = item
        Glide.with(viewHolder.itemView.context)
                .load(Endpoints.IMAGE_LINK + item.image!!)
                .centerCrop()
                .into(viewHolder.binding.eventImage)

        var location = ""
        for (location1 in item.location) {
            location = location + "\n" + location1.name
        }

        var timeStamp = ""
        for (timestamp1 in item.timestamp) {
            timeStamp = timeStamp + "\n" + DateTimeUtils.toReadableString(timestamp1.timestampStart) + " to " + DateTimeUtils.toReadableString(timestamp1.timestampEnd)
        }

        var tags = ""
        for (tags1 in item.tags) {
            tags = tags + "\n#" + tags1.title
        }

        viewHolder.binding.tags.text = tags.trim { it <= ' ' }
        viewHolder.binding.location.text = location.trim { it <= ' ' }
        viewHolder.binding.timeStamp.text = timeStamp.trim { it <= ' ' }

        viewHolder.binding.eventCard.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, EventSimpleDetailActivity::class.java)
            intent.putExtra(Constants.ID, item.id)
            intent.putExtra("fromMain", true)
            viewHolder.itemView.context.startActivity(intent)
        }

    }


    fun setList(list: List<Event>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }


    override fun getItemCount(): Int {
        return list.size
    }
}
