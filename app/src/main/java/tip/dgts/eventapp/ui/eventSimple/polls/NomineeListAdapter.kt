package tip.dgts.eventapp.ui.eventSimple.polls

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ItemNomineeBinding
import tip.dgts.eventapp.model.data.Nominee

/**
 * Created by Sen on 1/26/2017.
 */

class NomineeListAdapter(private val view: PollView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<Nominee>
    private var mSelectedItem = -1
    var selectedNominee: Nominee? = null
        private set
    private var isClickable: Boolean? = null

    init {
        list = ArrayList()
        isClickable = true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemNomineeBinding>(
                LayoutInflater.from(parent.context), R.layout.item_nominee, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = list[position]
        viewHolder.binding.item = item
        viewHolder.binding.view = view

        if (item.voted!!) {
            isClickable = false
            view.alreadyVoted()
            viewHolder.binding.nomineeLayout.setBackgroundResource(R.drawable.rounded_button_choice_selected)
        } else {
            if (mSelectedItem != position) {
                viewHolder.binding.nomineeLayout.setBackgroundResource(R.drawable.rounded_button_choice)
            }
        }

        viewHolder.binding.nomineeLayout.setOnClickListener {
            if (isClickable!!) {
                if (mSelectedItem == position) {
                    mSelectedItem = -1
                    viewHolder.binding.nomineeLayout.setBackgroundResource(R.drawable.rounded_button_choice)
                    selectedNominee = null
                } else {
                    mSelectedItem = position
                    viewHolder.binding.nomineeLayout.setBackgroundResource(R.drawable.rounded_button_choice_selected)
                    selectedNominee = list[position]

                }
                notifyDataSetChanged()
            }
        }


    }


    inner class ViewHolder(val binding: ItemNomineeBinding) : RecyclerView.ViewHolder(binding.root)

    fun setList(list: List<Nominee>) {
        this.list.clear()
        this.list.addAll(list)
        mSelectedItem = -1
        isClickable = true
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
