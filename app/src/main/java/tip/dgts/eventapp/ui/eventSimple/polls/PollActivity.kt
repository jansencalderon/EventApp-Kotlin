package tip.dgts.eventapp.ui.eventSimple.polls

import android.app.Dialog
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import io.realm.RealmResults
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.databinding.ActivityPollsBinding
import tip.dgts.eventapp.databinding.DialogPollBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Nominee
import tip.dgts.eventapp.model.data.Poll
import tip.dgts.eventapp.ui.login.LoginActivity
import tip.dgts.eventapp.ui.main.MainActivity

class PollActivity : MvpActivity<PollView, PollPresenter>(), PollView {

    lateinit var binding: ActivityPollsBinding
    lateinit var votingListAdapter: PollListAdapter
    private var eventId: Int = 0
    private val TAG = PollActivity::class.java.simpleName
    lateinit var dialogPollBinding: DialogPollBinding
    lateinit var dialogPoll: Dialog
    lateinit var nomineeListAdapter: NomineeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_polls)
        binding.view = mvpView
        presenter.onStart()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_dark_gray)

        eventId = intent.getIntExtra(Event.eventId, -1)
        if (eventId == -1) {
            finish()
        }
        votingListAdapter = PollListAdapter(mvpView)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = votingListAdapter
        presenter.getPolls(eventId)
        binding.swipeRefreshLayout.setOnRefreshListener { presenter.getPolls(eventId) }

        dialogPollBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_poll, null, false)
        dialogPoll = Dialog(this, R.style.AppTheme)
        dialogPoll.setContentView(dialogPollBinding.root)
        dialogPollBinding.toolbar.setNavigationOnClickListener { dialogPoll.dismiss() }


    }

    override fun createPresenter(): PollPresenter {
        return PollPresenter()
    }

    override fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemPollClicked(poll: Poll) {
        dialogPoll.show()
        dialogPollBinding.poll = poll
        presenter.getNominees(eventId, poll.id)
        dialogPollBinding.add.background = ContextCompat.getDrawable(this, R.drawable.rounded_slight_accent)
        dialogPollBinding.add.text = "SUBMIT A VOTE!"
        dialogPollBinding.add.setOnClickListener {
            val nominee = nomineeListAdapter.selectedNominee
            if (nominee == null) {
                showAlert("Please choose a nominee!")
            } else {
                val builder = AlertDialog.Builder(this@PollActivity)
                builder.setTitle("Voting")
                builder.setMessage("Are you sure you want to vote " + nominee.name + " for " + poll.title + "?")
                builder.setPositiveButton("YES") { dialog, which ->
                    presenter.addVote(eventId, poll.id, App.user.id, nominee.id!!)
                }
                builder.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }
        }
    }


    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun setList(polls: RealmResults<Poll>) {
        Log.d(TAG, "Poll list size " + polls.size)
        votingListAdapter.setList(polls)
        checkResult(polls.size)
    }

    fun checkResult(count: Int) {
        binding.noResult?.resultImage?.setImageResource(R.drawable.ic_vote)
        binding.noResult?.resultText?.text = "No polls available"
        if (count > 0) {
            binding.noResult?.noResultLayout?.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.noResult?.noResultLayout?.visibility = View.VISIBLE
        }
    }

    override fun startLoadingDialog() {
        dialogPollBinding.loadingPanel.visibility = View.VISIBLE
        dialogPollBinding.voteInputPanel.visibility = View.GONE
    }

    override fun stopLoadingDialog() {
        dialogPollBinding.loadingPanel.visibility = View.GONE
        dialogPollBinding.voteInputPanel.visibility = View.VISIBLE
    }

    override fun setNomineeList(data: List<Nominee>) {
        nomineeListAdapter = NomineeListAdapter(mvpView)
        dialogPollBinding.nomineeList.layoutManager = LinearLayoutManager(this)
        dialogPollBinding.nomineeList.adapter = nomineeListAdapter
        nomineeListAdapter.setList(data)
    }

    override fun onNomineeClicked(nominee: Nominee) {
        showAlert(nominee.name!!)
    }

    override fun votingSuccess(eventId: Int, pollId: Int) {
        if (dialogPoll.isShowing) {
            presenter.getNominees(eventId, pollId)
            presenter.getPolls(eventId)
        }
        alreadyVoted()

    }

    override fun alreadyVoted() {
        //disable controls
        dialogPollBinding.add.background = ContextCompat.getDrawable(this, R.drawable.rounded_slight_green)
        dialogPollBinding.add.text = "VOTE SUBMITTED"
        dialogPollBinding.add.setOnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
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
