package tip.dgts.eventapp.ui.eventSimple.sponsors

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivitySponsorsBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Sponsor

class SponsorsActivity : MvpActivity<SponsorsView, SponsorsPresenter>(), SponsorsView {


    lateinit var binding: ActivitySponsorsBinding
    lateinit var sponsorListAdapter: SponsorListAdapter
    var eventId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onStart()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sponsors)

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_dark_gray)

        eventId = intent.getIntExtra(Event.eventId, -1)
        if (eventId == -1) {
            finish()
        }
        sponsorListAdapter = SponsorListAdapter(mvpView)
        presenter.getSponsors(eventId)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = sponsorListAdapter

    }

    override fun createPresenter(): SponsorsPresenter {
        return SponsorsPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun showAlert(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {

    }

    override fun stopLoading() {

    }

    override fun setList(sponsors: List<Sponsor>) {
        sponsorListAdapter.setList(sponsors)
        checkResult(sponsors.size)
    }

    private fun checkResult(count: Int) {
        binding.noResult!!.resultText.text = "This event has no sponsors yet"
        binding.noResult!!.resultImage.setImageResource(R.drawable.ic_ticket)
        if (count > 0) {
            binding.noResult!!.noResultLayout.visibility = View.GONE
        } else {
            binding.noResult!!.noResultLayout.visibility = View.VISIBLE
        }
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
