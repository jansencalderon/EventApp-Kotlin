package tip.dgts.eventapp.ui.venue

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View

import com.bumptech.glide.Glide
import com.hannesdorfmann.mosby.mvp.MvpActivity

import java.util.Arrays
import java.util.Locale

import io.realm.Realm
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.databinding.ActivityLocationBinding
import tip.dgts.eventapp.model.data.Location

class LocationActivity : MvpActivity<LocationView, LocationPresenter>(), LocationView {

    lateinit var binding: ActivityLocationBinding
    private var location: Location? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)
        binding.view = mvpView
        realm = Realm.getDefaultInstance()

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val i = intent
        val id = i.getIntExtra(Constants.ID, -1)
        if (id != -1) {
            location = realm!!.where(Location::class.java).equalTo(Constants.ID, id).findFirst()
            binding.location = location
            //set lists
            /* List<String> list = Arrays.asList(location.getLocCateredEvents().split("-"));
            binding.cateredEvents.setLayoutManager(new LinearLayoutManager(this));
            binding.cateredEvents.setAdapter(new LocationListAdapter(list));

            List<String> list2 = Arrays.asList(location.getLocVenueType().split("-"));
            binding.venueTypes.setLayoutManager(new LinearLayoutManager(this));
            binding.venueTypes.setAdapter(new LocationListAdapter(list2));

            List<String> list3 = Arrays.asList(location.getLocFeatures().split("-"));
            binding.features.setLayoutManager(new LinearLayoutManager(this));
            binding.features.setAdapter(new LocationListAdapter(list3));
            Glide.with(this).load(Constants.URL_IMAGE + location.getLocImage()).into(binding.locationImage);

*/
        }

        binding.selectVenue.visibility = View.GONE


    }


    override fun openOnGoogleMaps() {
        val uri = String.format(Locale.ENGLISH, "geo:%f,%f", location!!.lat, location!!.lng)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)

    }

    override fun openOnWaze() {
        val url = String.format(Locale.ENGLISH, "waze://?ll=%f,%f&navigate=yes", location!!.lat, location!!.lng)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
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


    override fun onBackPressed() {

        finish()
    }

    override fun createPresenter(): LocationPresenter {
        return LocationPresenter()
    }
}
