package tip.dgts.eventapp.ui.eventSimple.feedbacks

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivityFeedbacksBinding
import tip.dgts.eventapp.databinding.DialogRateBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Feedback

class FeedbacksActivity : MvpActivity<FeedbacksView, FeedbacksPresenter>(), FeedbacksView {


    lateinit var binding: ActivityFeedbacksBinding
    private var eventId: Int = 0
    private var feedbackListAdapter: FeedbackListAdapter? = null
    private var dialogRateBinding: DialogRateBinding? = null
    private var dialogRate: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onStart()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedbacks)
        binding.view = mvpView

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_dark_gray)

        eventId = intent.getIntExtra(Event.eventId, -1)
        if (eventId == -1) {
            finish()
        }
        feedbackListAdapter = FeedbackListAdapter(mvpView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = feedbackListAdapter

        dialogRateBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_rate, null, false)
        dialogRate = Dialog(this)
        dialogRate!!.setContentView(dialogRateBinding!!.root)

        binding.rate.setOnClickListener {
            dialogRateBinding!!.rateInputPanel.visibility = View.VISIBLE
            dialogRateBinding!!.rateSuccessPanel.visibility = View.GONE
            dialogRateBinding!!.loadingPanel.visibility = View.GONE
            dialogRate!!.show()
        }


        dialogRateBinding!!.add.setOnClickListener { presenter.addRate(eventId, dialogRateBinding!!.rating.rating.toInt(), dialogRateBinding!!.comments.text.toString()) }
        dialogRateBinding!!.dialogOk.setOnClickListener { dialogRate!!.dismiss() }

        presenter.getFeedback(eventId)

        //TODO: list reload
        binding.swipeRefreshLayout.setOnRefreshListener { presenter.getFeedbacksList(eventId) }
    }

    override fun createPresenter(): FeedbacksPresenter {
        return FeedbacksPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }


    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }


    override fun startLoadingDialog() {
        dialogRateBinding!!.loadingPanel.visibility = View.VISIBLE
    }

    override fun stopLoadingDialog() {
        dialogRateBinding!!.loadingPanel.visibility = View.GONE
    }

    override fun setList(list: List<Feedback>) {
        feedbackListAdapter!!.setList(list)
        if (list.isEmpty()) {
            binding.noResult!!.noResultLayout.visibility = View.VISIBLE
            binding.noResult!!.resultText.text = "No reviews yet"
            binding.noResult!!.resultImage.setImageDrawable(getDrawable(R.drawable.ic_feedback))
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.noResult!!.noResultLayout.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun refreshList() {

    }

    override fun feedbackSuccess() {
        dialogRateBinding!!.rateInputPanel.visibility = View.GONE
        dialogRateBinding!!.rateSuccessPanel.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
