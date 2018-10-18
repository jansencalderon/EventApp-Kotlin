package tip.dgts.eventapp.ui.profile

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import io.realm.RealmResults
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.databinding.ActivityProfileBinding
import tip.dgts.eventapp.databinding.DialogAddInterestBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Ticket
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.ui.profile.update.UpdateProfileActivity

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class ProfileActivity : MvpActivity<ProfileView, ProfilePresenter>(), ProfileView, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: ActivityProfileBinding
    private val TAG = ProfileActivity::class.java.simpleName
    private var progressDialog: ProgressDialog? = null
    lateinit var user: User
    lateinit var dialogAddInterestBinding: DialogAddInterestBinding
    lateinit var tag: Tag
    lateinit var dialogAddInterest: Dialog
    lateinit var ticketListAdapter: ProfileTicketListAdapter
    lateinit var profileLikedEventListAdapter: ProfileLikedEventListAdapter
    lateinit var profileInterestListAdapter: ProfileInterestListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.view = mvpView
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        user = App.user
        binding.user = user


        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Updating...")
        progressDialog!!.setCancelable(false)


        binding.collapsingToolBar.title = "${user?.firstName} ${user?.lastName}"
        binding.toolbar.title = "${user?.firstName} ${user?.lastName}"



        binding.addInterest.setOnClickListener { showAddInterestDialog() }

        dialogAddInterestBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_interest, null, false)
        dialogAddInterest = Dialog(this)
        dialogAddInterest!!.setContentView(dialogAddInterestBinding!!.root)


        ticketListAdapter = ProfileTicketListAdapter(this)
        binding.tickets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.tickets.adapter = ticketListAdapter


        profileLikedEventListAdapter = ProfileLikedEventListAdapter(this)
        binding.likedEvents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.likedEvents.adapter = profileLikedEventListAdapter


        profileInterestListAdapter = ProfileInterestListAdapter(mvpView)
        binding.interests.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.interests.adapter = profileInterestListAdapter

        binding.swipeRefreshLayout.setOnRefreshListener(this)


        presenter.onStart()
        presenter.getTickets(1)
        presenter.getInterests(1)
        presenter.getLikedEvents(1)
    }

    private fun showAddInterestDialog() {
        presenter.getTags()
        dialogAddInterest.show()
        dialogAddInterestBinding.add.setOnClickListener {
            if (tag != null)
                presenter.addInterest(tag.id)
            else
                showAlert("Please select interest")
        }
    }

    /***
     * Start of MvpViewStateActivity
     */

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter()
    }


    /***
     * End of MvpViewStateActivity
     */


    /***
     * Start of ProfileView
     */
    override fun onEdit() {
        startActivity(Intent(this@ProfileActivity, UpdateProfileActivity::class.java))
    }

    override fun showAlert(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun startLoading() {
        progressDialog!!.show()
    }

    override fun stopLoading() {
        if (progressDialog!!.isShowing) progressDialog!!.dismiss()
        if (binding.swipeRefreshLayout.isRefreshing)
            binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun finishAct() {
        finish()
        showAlert("Profile Updated")
    }


    override fun setList(tickets: List<Ticket>) {
        ticketListAdapter.setList(tickets)
    }

    override fun setInterests(interests: List<Interest>) {
        profileInterestListAdapter.setList(interests)
    }

    override fun setLikedEvents(eventRealmResults: RealmResults<Event>) {
        profileLikedEventListAdapter.setList(eventRealmResults)
    }

    override fun onInterestClicked(interest: Interest) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete " + interest.title + " ?")
        builder.setPositiveButton("YES") { dialog, which -> presenter.deleteInterest(interest) }
        builder.setNegativeButton("NO") { dialog, which ->
            // Do nothing
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }


    override fun setTags(tags: List<Tag>) {
        Log.d(TAG, "Size of Results: " + tags.size)
        val spinner = dialogAddInterestBinding!!.spinnerInterests
        val dataAdapter = ArrayAdapter<Tag>(supportActionBar!!.themedContext, R.layout.spinner_list_style, tags)
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = dataAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                tag = dataAdapter.getItem(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun dismissAddInterestDialog() {
        dialogAddInterest.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_edit -> {
                onEdit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun onRefresh() {
        presenter.getTickets(1)
        presenter.getInterests(1)
        presenter.getLikedEvents(1)
    }
}
