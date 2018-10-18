package tip.dgts.eventapp.ui.eventSimple.dayDetail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View

import com.hannesdorfmann.mosby.mvp.MvpActivity

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.databinding.ActivityDayDetailBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Schedule
import tip.dgts.eventapp.model.data.Timestamp
import tip.dgts.eventapp.utils.DateTimeUtils

class DayDetailActivity : MvpActivity<DayDetailView, DayDetailPresenter>(), DayDetailView {


    lateinit var binding: ActivityDayDetailBinding
    private var timeStampId: Int = 0
    private var dayDetailListAdapter: DayDetailListAdapter? = null
    private var day: Timestamp? = null
    private var toolbarTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onStart()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_day_detail)

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        timeStampId = intent.getIntExtra(Timestamp.timestampId, -1)
        toolbarTitle = intent.getStringExtra(Constants.TOOLBAR_TITLE)
        if (timeStampId == -1) {
            finish()
        }

        dayDetailListAdapter = DayDetailListAdapter(mvpView)
        day = presenter.getDay(timeStampId)

        supportActionBar!!.setTitle(toolbarTitle)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_dark_gray)

        presenter.getSchedules(timeStampId)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = dayDetailListAdapter
    }

    override fun createPresenter(): DayDetailPresenter {
        return DayDetailPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun showAlert(message: String) {

    }

    override fun startLoading() {

    }

    override fun stopLoading() {

    }

    override fun setList(list: List<Schedule>) {
        dayDetailListAdapter!!.setList(list)
        checkResult(list.size)
    }

    override fun checkResult(count: Int) {
        binding.noResult?.resultText?.text = "No schedules for this day yet"
        if (count > 0) {
            binding.noResult?.noResultLayout?.visibility = View.GONE
        } else {
            binding.noResult?.noResultLayout?.visibility = View.VISIBLE
        }
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
}
