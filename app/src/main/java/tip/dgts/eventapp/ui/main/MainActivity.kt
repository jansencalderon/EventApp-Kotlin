package tip.dgts.eventapp.ui.main

import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.hannesdorfmann.mosby.mvp.MvpActivity

import java.util.ArrayList

import de.hdodenhof.circleimageview.CircleImageView
import io.realm.Realm
import io.realm.RealmResults
import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivityMainBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.ui.login.LoginActivity
import tip.dgts.eventapp.ui.main.byCategory.ByTagActivity
import tip.dgts.eventapp.ui.profile.ProfileActivity
import tip.dgts.eventapp.ui.ticketList.TicketsActivity


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView, NavigationView.OnNavigationItemSelectedListener {
    private val realm: Realm? = null
    lateinit var binding: ActivityMainBinding
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private val client: GoogleApiClient? = null
    private val progressDialog: ProgressDialog? = null
    internal var strings: MutableList<String> = ArrayList()
    private val notifs: TextView? = null
    private var mainInterestListAdapter: MainInterestListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = mvpView
        presenter.onStart()
        setSupportActionBar(binding.toolbar)


        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)


        //display data
        binding.navigationView.getHeaderView(0).findViewById<View>(R.id.viewProfile).setOnClickListener { startActivity(Intent(this@MainActivity, ProfileActivity::class.java)) }

        binding.navigationView.menu.getItem(0).isChecked = true

        //MenuItem notifsItem = binding.navigationView.findViewById(R.id.notifications);
        // notifs = (TextView)notifsItem.getActionView();


        strings.add("All")
        strings.add("Liked")
        strings.add("Booked")
        binding.viewpager.adapter = MainPageFragmentAdapter(supportFragmentManager, strings)
        binding.viewpager.offscreenPageLimit = strings.size
        binding.slidingTabs.setupWithViewPager(binding.viewpager)
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter.getEvents(0)
            presenter.getInterests(1)
        }



        //getData
        presenter.getEvents(0)
        presenter.getInterests(1)


        binding.tagsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainInterestListAdapter = MainInterestListAdapter(mvpView)
        binding.tagsRecyclerView.adapter = mainInterestListAdapter

    }

    override fun onResume() {
        super.onResume()
        val realm = Realm.getDefaultInstance()
        val user = realm.where(User::class.java).findFirst()
        if (user != null)
            displayUserData(user)
        realm.close()
    }

    /*private void sendTokenToServer() {
        User user = App.getUser();
        final SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this, Constants.FIREBASE);
        Boolean sent = sharedPreferencesUtil.getBooleanValue(Constants.FIREBASE + "_sent", false);
        String token = sharedPreferencesUtil.getStringValue(Constants.FIREBASE + "_token", "");
        if (!sent) {
            if (!token.equals("")) {
                App.getInstance().getApiInterface().saveUserToken(user.getUserId() + "", token).enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        if(response.isSuccessful()){
                            if (response.body().equals(Constants.SUCCESS)) {
                                sharedPreferencesUtil.putBooleanValue(Constants.FIREBASE + "_sent", true);
                            } else {
                                Log.e(TAG, "Token Not Updated");
                            }
                        }else {
                            showAlert("Website is sleeping");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        Log.d(TAG, t.toString());
                    }
                });
            } else {
                Log.e(TAG, "No Token Yet");
            }
        } else {
            Log.e(TAG, "Token is already sent");
        }
    }*/


    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }


    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun displayUserData(user: User) {
        val name = binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.name)
        val circleImageView = binding.navigationView.getHeaderView(0).findViewById<CircleImageView>(R.id.userImage)
        name.text = "${user.firstName} ${user.lastName}"
        Glide.with(this)
                .load(R.drawable.user)
                .into(circleImageView)
    }

    override fun showAlert(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun setEvents(eventToday: List<Event>, eventUpcoming: List<Event>) {
        binding.viewpager.adapter = MainPageFragmentAdapter(supportFragmentManager, strings)
    }

    override fun refreshList() {

    }

    override fun setTags(interests: RealmResults<Interest>) {
        mainInterestListAdapter!!.setList(interests)

    }

    override fun onInterestClicked(interest: Interest) {
        val intent = Intent(this, ByTagActivity::class.java)
        intent.putExtra(Tag.TAG_ID, interest.id)
        intent.putExtra(Tag.TAG_TITLE, interest.title)
        startActivity(intent)
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.home -> {

            }
            R.id.tickets -> {
                startActivity(Intent(this, TicketsActivity::class.java))
                binding.navigationView.menu.getItem(0).isChecked = true
            }
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Log Out")
                builder.setMessage("Are you sure?")
                builder.setPositiveButton("YES") { dialog, which ->
                    // Do nothing but close the dialog
                    val realm = Realm.getDefaultInstance()
                    realm.executeTransactionAsync({ realm -> realm.deleteAll() }, {
                        realm.close()
                        // TODO: 12/4/2016 add flag to clear all task
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        this@MainActivity.finish()
                    }, { error ->
                        realm.close()
                        Log.e(TAG, "onError: Error Logging out (deleting all data)", error)
                    })
                    finish()
                }
                builder.setNegativeButton("NO") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()

            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    public override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


}
