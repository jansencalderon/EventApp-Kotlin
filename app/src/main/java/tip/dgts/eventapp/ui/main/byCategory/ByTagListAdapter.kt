package tip.dgts.eventapp.ui.main.byCategory

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import com.bumptech.glide.Glide

import java.util.ArrayList

import io.realm.RealmList
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ItemEventMainTagBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Location
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Timestamp
import tip.dgts.eventapp.ui.main.byCategory.ByTagView
import tip.dgts.eventapp.utils.DateTimeUtils


/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

class ByTagListAdapter(private val mainView: ByTagView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventList: MutableList<Event>
    private var loading: Boolean = false

    init {
        eventList = ArrayList()

    }


    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemEventBinding = DataBindingUtil.inflate<ItemEventMainTagBinding>(
                LayoutInflater.from(parent.context), R.layout.item_event_main_tag, parent, false)
        return EventViewHolder(itemEventBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ByTagListAdapter", "position=$position")
        val eventViewHolder = holder as ByTagListAdapter.EventViewHolder
        eventViewHolder.itemEventBinding.event = eventList[position]
        eventViewHolder.itemEventBinding.view = mainView
        Glide.with(eventViewHolder.itemView.context)
                .load(Endpoints.IMAGE_LINK + eventList[position].image)
                .centerCrop()
                .into(eventViewHolder.itemEventBinding.eventImage)

        var location = ""
        for (location1 in eventList[position].location!!.toList()) {
            location = location + "\n" + location1.name
        }


        var tags = ""
        for (tags1 in eventList[position].tags!!.toList()) {
            tags = tags + "\n#" + tags1.title
        }

        eventViewHolder.itemEventBinding.tags.text = tags.trim { it <= ' ' }
        eventViewHolder.itemEventBinding.location.text = location.trim { it <= ' ' }

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


    inner class EventViewHolder(val itemEventBinding: ItemEventMainTagBinding) : RecyclerView.ViewHolder(itemEventBinding.root)

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun getItemId(position: Int): Long {
        return eventList[position].id!!.toLong()
    }
}
