package tip.dgts.eventapp.ui.main.byCategory

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.databinding.ActivityByTagBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.ui.eventSimple.EventSimpleDetailActivity

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

class ByTagActivity : MvpActivity<ByTagView, ByTagPresenter>(), ByTagView, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: ActivityByTagBinding
    private val user: User? = null
    private var mainListAdapter: ByTagListAdapter? = null
    private var tagId: Int = 0
    private var tagTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_by_tag)
        binding.view = mvpView


        tagId = intent.getIntExtra(Tag.TAG_ID, -1)
        tagTitle = intent.getStringExtra(Tag.TAG_TITLE)


        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.title = tagTitle
        mainListAdapter = ByTagListAdapter(mvpView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mainListAdapter


        binding.swipeRefreshLayout.setOnRefreshListener(this)


        presenter.onStart(tagTitle!!)
        presenter.getEvents(tagId)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showAlert(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun setEvents(events: List<Event>) {
        Log.d(ByTagActivity::class.java.simpleName, "events " + events.size)
        checkResult(events.size)
        mainListAdapter!!.setEvents(events)
    }


    override fun refreshList() {

    }

    override fun onEventClicked(event: Event) {
        val intent = Intent(this@ByTagActivity, EventSimpleDetailActivity::class.java)
        intent.putExtra(Constants.ID, event.id)
        intent.putExtra("fromMain", true)
        startActivity(intent)
    }


    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }


    fun checkResult(count: Int) {
        binding.noResult?.resultText?.text = "Oops! No events for " + tagTitle!!
        binding.noResult?.resultImage?.setImageResource(R.drawable.ic_calendar)
        if (count > 0) {
            binding.noResult?.noResultLayout?.visibility = View.GONE
        } else {
            binding.noResult?.noResultLayout?.visibility = View.VISIBLE
        }
    }


    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun createPresenter(): ByTagPresenter {
        return ByTagPresenter()
    }


    override fun onRefresh() {
        presenter.getEvents(tagId)
    }
}
