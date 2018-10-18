package tip.dgts.eventapp.ui.main.tabs

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemEventMainBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Location
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Timestamp
import tip.dgts.eventapp.ui.main.byCategory.ByTagView
import tip.dgts.eventapp.utils.DateTimeUtils


/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

class MainListAdapter(private val mainView: MainPageView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventList: MutableList<Event>
    private var loading: Boolean = false

    init {
        eventList = ArrayList()

    }


    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemEventBinding = DataBindingUtil.inflate<ItemEventMainBinding>(
                LayoutInflater.from(parent.context), R.layout.item_event_main, parent, false)
        return MainListAdapter.EventViewHolder(itemEventBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val eventViewHolder = holder as MainListAdapter.EventViewHolder
        eventViewHolder.itemEventBinding.event = eventList[position]
        eventViewHolder.itemEventBinding.view = mainView
        Glide.with(eventViewHolder.itemView.context)
                .load(Endpoints.IMAGE_LINK + eventList[position].image)
                .centerCrop()
                .into(eventViewHolder.itemEventBinding.eventImage)

        var location = ""
        for (location1 in eventList[position].location.toList()) {
            location = location + "\n" + location1.name
        }

        var timeStamp = ""
        for (timestamp1 in eventList[position].timestamp.toList()) {
            timeStamp = timeStamp + "\n" + DateTimeUtils.toReadableString(timestamp1.timestampStart) + " to " + DateTimeUtils.toReadableString(timestamp1.timestampEnd)
        }

        var tags = ""
        for (tags1 in eventList[position].tags.toList()) {
            tags = tags + " #" + tags1.title
        }

        eventViewHolder.itemEventBinding.tags.text = tags.trim { it <= ' ' }
        eventViewHolder.itemEventBinding.location.text = location.trim { it <= ' ' }
        eventViewHolder.itemEventBinding.timeStamp.text = timeStamp.trim { it <= ' ' }

    }

    fun setEvents(eventList: List<Event>) {
        this.eventList.clear()
        this.eventList.addAll(eventList)
        notifyDataSetChanged()
    }

    fun clear() {
        eventList.clear()
        notifyDataSetChanged()
    }


    fun setLoading(loading: Boolean) {
        this.loading = loading

        notifyDataSetChanged()
    }


    class EventViewHolder(val itemEventBinding: ItemEventMainBinding) : RecyclerView.ViewHolder(itemEventBinding.root)

    override fun getItemId(position: Int): Long {
        return eventList[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    companion object {
        private val VIEW_TYPE_MORE = 1
        private val VIEW_TYPE_DEFAULT = 0
    }
}
