package tip.dgts.eventapp.ui.eventSimple.sessions

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem

import com.hannesdorfmann.mosby.mvp.MvpActivity

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivitySessionsBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Schedule

class SessionsActivity : MvpActivity<SessionsView, SessionsPresenter>(), SessionsView {


    lateinit var binding: ActivitySessionsBinding
    private var eventId: Int = 0
    private var sessionListAdapter: SessionListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onStart()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sessions)

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_dark_gray)

        eventId = intent.getIntExtra(Event.eventId, -1)
        if (eventId == -1) {
            finish()
        }
        sessionListAdapter = SessionListAdapter(mvpView)
        presenter.getSchedules(eventId)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = sessionListAdapter
    }

    override fun createPresenter(): SessionsPresenter {
        return SessionsPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun showAlert(message: String?) {

    }

    override fun startLoading() {

    }

    override fun stopLoading() {

    }

    override fun setList(schedules: List<Schedule>) {
        sessionListAdapter!!.setList(schedules)
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
