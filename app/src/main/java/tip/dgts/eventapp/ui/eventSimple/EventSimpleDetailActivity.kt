package tip.dgts.eventapp.ui.eventSimple

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import com.bumptech.glide.Glide

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.hannesdorfmann.mosby.mvp.MvpActivity
import com.journeyapps.barcodescanner.BarcodeEncoder

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.app.Endpoints
import tip.dgts.eventapp.databinding.ActivitySimpleEventDetailBinding
import tip.dgts.eventapp.databinding.DialogMapBinding
import tip.dgts.eventapp.databinding.DialogReservationBinding
import tip.dgts.eventapp.databinding.DialogTicketDetailBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Location
import tip.dgts.eventapp.model.data.Sponsor
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.ui.eventSimple.feedbacks.FeedbacksActivity
import tip.dgts.eventapp.ui.eventSimple.polls.PollActivity
import tip.dgts.eventapp.ui.eventSimple.sponsors.SponsorsActivity
import tip.dgts.eventapp.utils.DateTimeUtils
import java.util.*

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

class EventSimpleDetailActivity : MvpActivity<EventSimpleDetailView, EventSimpleDetailPresenter>(), EventSimpleDetailView, OnMapReadyCallback {

    lateinit var binding: ActivitySimpleEventDetailBinding
    lateinit var event: Event
    private val user: User? = null
    private var eventId: Int = 0
    private val fromMain: Boolean? = null
    private val progressDialog: ProgressDialog? = null
    lateinit var menu: Menu
    lateinit var dialogReservationBinding: DialogReservationBinding
    lateinit var dialogReservation: Dialog
    lateinit var daySmallListAdapter: DaySmallListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_event_detail)
        binding.view = mvpView
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.collapsingToolBar.setExpandedTitleColor(Color.TRANSPARENT)

        presenter.onStart()
        eventId = intent.getIntExtra(Event.eventId, -1)
        event = presenter.getEvent(eventId)
        presenter.getTickets()

        //TODO: Image


        Log.e("Image :", Endpoints.IMAGE_LINK + event.image)

        Glide.with(this)
                .load(Endpoints.IMAGE_LINK + event.organizer!!.image)
                .error(R.color.lightestGray)
                .placeholder(R.color.lightestGray)
                .dontAnimate()
                .into(binding.organizerImage)


        val locations = ArrayList<String?>()
        for (location1 in event.location.toList()) {
            locations.add(location1.name)
        }

        var tags = ""
        for (tags1 in event.tags.toList()) {
            tags = tags + "\n#" + tags1.title
        }
        binding.tags.text = tags.trim { it <= ' ' }

        binding.event = event

        val locationListAdapter = LocationListAdapter(mvpView)
        binding.locationList.layoutManager = LinearLayoutManager(this)
        binding.locationList.adapter = locationListAdapter
        locationListAdapter.setList(event.location.toList())


        daySmallListAdapter = DaySmallListAdapter(mvpView)
        daySmallListAdapter.setReserved(event.reserved)
        daySmallListAdapter.setList(event.timestamp.toList())
        binding.timeList.layoutManager = LinearLayoutManager(this)
        binding.timeList.adapter = daySmallListAdapter


        val sponsorSmallListAdapter = SponsorSmallListAdapter(mvpView)
        sponsorSmallListAdapter.setList(event.sponsors.toList())
        binding.sponsorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.sponsorsRecyclerView.adapter = sponsorSmallListAdapter

        val contactListAdapter = ContactListAdapter(mvpView)
        contactListAdapter.setList(event.contact.toList())
        binding.contactRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contactRecyclerView.adapter = contactListAdapter

        val sessionSmallListAdapter = SessionSmallListAdapter(mvpView)
        sessionSmallListAdapter.setList(event.schedules.toList())
        binding.sessionList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.sessionList.adapter = sessionSmallListAdapter

        val feedbackSmallListAdapter = FeedbackSmallListAdapter(mvpView)
        feedbackSmallListAdapter.setList(event.feedbacks.toList())
        binding.reviewList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.reviewList.adapter = feedbackSmallListAdapter

        binding.reserve.setOnClickListener {
            if (event.isValid) {
                showConfirmDialog()
            }
        }

        checkIfReserved(event)


        dialogReservationBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_reservation, null, false)
        dialogReservation = Dialog(this)
        dialogReservation.setContentView(dialogReservationBinding.root)

        binding.viewTicket.setOnClickListener(View.OnClickListener {
            val ticket = presenter.getEventTicket(event.name) ?: return@OnClickListener
            val ticketDetailBinding = DataBindingUtil.inflate<DialogTicketDetailBinding>(LayoutInflater.from(this@EventSimpleDetailActivity), R.layout.dialog_ticket_detail, null, false)
            ticketDetailBinding.ticket = ticket
            val ticketDialog = Dialog(this@EventSimpleDetailActivity)
            ticketDialog.setContentView(ticketDetailBinding.root)
            val text = ticket.orderNumber!!.toString() + ""// Whatever you need to encode in the QR code
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                ticketDetailBinding.barcode.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }

            ticketDialog.show()
        })

        binding.sponsorsLayout.setOnClickListener { startActivity(Intent(this@EventSimpleDetailActivity, SponsorsActivity::class.java).putExtra(Event.eventId, event.id)) }

        binding.reviewsLayout.setOnClickListener { startActivity(Intent(this@EventSimpleDetailActivity, FeedbacksActivity::class.java).putExtra(Event.eventId, event.id)) }

        binding.pollsLayout.setOnClickListener { startActivity(Intent(this@EventSimpleDetailActivity, PollActivity::class.java).putExtra(Event.eventId, event.id)) }

    }

    private fun checkIfReserved(event: Event) {
        if (event.reserved) {
            binding.viewTicket.visibility = View.VISIBLE
            binding.reservedPanel.visibility = View.VISIBLE
            binding.reserve.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            presenter.getTickets()

            //clickable day

            daySmallListAdapter.setReserved(event.reserved)
            daySmallListAdapter.setList(event.timestamp.toList())
            binding.timeList.layoutManager = LinearLayoutManager(this)
            binding.timeList.adapter = daySmallListAdapter
        } else {
            binding.reservedPanel.visibility = View.GONE
            binding.viewTicket.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.reserve.visibility = View.VISIBLE

        }
    }

    private fun showConfirmDialog() {
        var location = ""
        for (location1 in event.location.toList()) {
            location = location + "\n" + location1.name
        }

        var timeStamp = ""
        for (timestamp1 in event.timestamp.toList()) {
            timeStamp = timeStamp + "\n" + DateTimeUtils.toReadableString(timestamp1.timestampStart) + " to " + DateTimeUtils.toReadableString(timestamp1.timestampEnd)
        }

        var tags = ""
        for (tags1 in event.tags.toList()) {
            tags = tags + " #" + tags1.title
        }
        dialogReservationBinding.location.text = location.trim { it <= ' ' }
        dialogReservationBinding.time.text = timeStamp.trim { it <= ' ' }
        dialogReservationBinding.event = event
        dialogReservationBinding.confirm.setOnClickListener {
            dialogReservation.dismiss()
            presenter.reserve(1, event.id)
        }
        dialogReservationBinding.cancel.setOnClickListener { dialogReservation.dismiss() }
        dialogReservationBinding.checkbox.setOnCheckedChangeListener { _, b ->
            dialogReservationBinding.confirm.isEnabled = b
        }
        dialogReservation.show()
    }

    override fun refreshEvent() {
        event = presenter.getEvent(eventId)
        checkIfReserved(event)

        if (event.liked)
            menu.getItem(0).icon = resources.getDrawable(R.drawable.ic_like_colored, theme)
        else
            menu.getItem(0).icon = resources.getDrawable(R.drawable.ic_like, theme)


    }

    override fun onLocationClicked(location: Location) {

        val binding = DataBindingUtil.inflate<DialogMapBinding>(LayoutInflater.from(this), R.layout.dialog_map, null, false)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        binding.name.text = location.name
        binding.address.text = location.address

        val mMapView = dialog.findViewById<View>(R.id.mapView) as MapView
        MapsInitializer.initialize(this)

        mMapView.onCreate(dialog.onSaveInstanceState())
        mMapView.onResume()// needed to get the map to display immediately
        mMapView.getMapAsync { googleMap ->
            val pos = LatLng(java.lang.Double.parseDouble(location.lat), java.lang.Double.parseDouble(location.lng)) ////your lat lng
            googleMap.addMarker(MarkerOptions().position(pos).title(location.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 2000, null)
        }



        dialog.show()
    }

    override fun onSponsorClicked(sponsor: Sponsor) {
        startActivity(Intent(this@EventSimpleDetailActivity, SponsorsActivity::class.java).putExtra(Event.eventId, event.id))
    }

    override fun showSnackbar(s: String) {
        val snackbar = Snackbar
                .make(binding.coordinatorLayout, s, Snackbar.LENGTH_LONG)

        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        snackbar.show()
    }


    /* private void setEventData(final Event event) {
         if (!event.isValid()) {
             return;
         }
         this.event = event;
         user = App.getUser();
         binding.setEvent(event);
         binding.setUser(user);

         *//*if (user.getUserId() == event.getUserId()) {
            binding.eventResponsePanel.setVisibility(View.GONE);
        } else {
            binding.inviteGuests.setVisibility(View.GONE);
            if (event.getGuests().where().equalTo("email", user.getEmail()).findFirst() == null) {
                binding.eventResponsePanel.setVisibility(View.GONE);
                binding.notInvited.setVisibility(View.VISIBLE);
            } else {
                Guest guest = event.getGuests().where().equalTo("email", user.getEmail()).findFirst();
                onResponseSuccessful(guest.getResponse());
                showAlert(guest.getResponse());
            }
        }*//*




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu
        menuInflater.inflate(R.menu.menu_event, menu)

        if (event.liked)
            menu.getItem(0).icon = resources.getDrawable(R.drawable.ic_like_colored, theme)
        else
            menu.getItem(0).icon = resources.getDrawable(R.drawable.ic_like, theme)


        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_like -> {
                if (!event.liked)
                    presenter.like(1, event.id)
                else
                    presenter.unlike(1, event.id)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showAlert(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        if (!event.reserved) {
            binding.reserve.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun stopLoading() {
        if (event.isValid) {
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun createPresenter(): EventSimpleDetailPresenter {
        return EventSimpleDetailPresenter()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        /*LatLng latLng = new LatLng(event.getLocation().getLocLat(), event.getLocation().getLocLong());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(event.getLocation().getLocName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        googleMap.getUiSettings().setAllGesturesEnabled(false);*/
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }
}