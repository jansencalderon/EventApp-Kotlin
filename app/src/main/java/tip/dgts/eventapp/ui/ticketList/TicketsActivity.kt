package tip.dgts.eventapp.ui.ticketList

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.hannesdorfmann.mosby.mvp.MvpActivity
import com.journeyapps.barcodescanner.BarcodeEncoder

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivityTicketsBinding
import tip.dgts.eventapp.databinding.DialogTicketDetailBinding
import tip.dgts.eventapp.model.data.Ticket
import tip.dgts.eventapp.model.data.User

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

class TicketsActivity : MvpActivity<TicketsListView, TicketsPresenter>(), TicketsListView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    lateinit var binding: ActivityTicketsBinding
    private val user: User? = null
    private var ticketListAdapter: TicketListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tickets)
        binding.view = mvpView
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        ticketListAdapter = TicketListAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ticketListAdapter
        presenter.onStart()


        binding.swipeRefreshLayout.setOnRefreshListener(this)


        val dropdown = binding.spinner
        dropdown.onItemSelectedListener = this
        val items = arrayOf("All", "Past", "Future")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        presenter.loadTickets()
    }

    override fun onNewViewStateInstance() {
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.isRefreshing = true
            onRefresh()
        }
    }

    override fun createPresenter(): TicketsPresenter {
        return TicketsPresenter()
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

    override fun showAlert(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun stopLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun startLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun setTickets(ticketList: List<Ticket>) {
        if (ticketListAdapter != null) {
            ticketListAdapter!!.setList(ticketList)
        }
        checkResult(ticketList.size)
    }


    private fun checkResult(count: Int) {
        binding.noResult!!.resultText.text = "You have no tickets yet"
        binding.noResult!!.resultImage.setImageResource(R.drawable.ic_ticket)
        if (count > 0) {
            binding.noResult!!.noResultLayout.visibility = View.GONE
        } else {
            binding.noResult!!.noResultLayout.visibility = View.VISIBLE
        }
    }

    override fun onRefresh() {
        presenter.loadTickets()
    }

    override fun onTicketClicked(ticket: Ticket) {
        /* Intent intent = new Intent(this, EventSimpleDetailActivity.class);
        intent.putExtra(Constants.ID, ticket.getEventId());
        startActivity(intent);*/
        val ticketDetailBinding = DataBindingUtil.inflate<DialogTicketDetailBinding>(LayoutInflater.from(this), R.layout.dialog_ticket_detail, null, false)
        ticketDetailBinding.ticket = ticket
        val ticketDialog = Dialog(this)
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
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the  action bar if it is present.
        // getMenuInflater().inflate(R.menu.ticket, menu);


        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = parent.getItemAtPosition(position).toString()
        //    presenter.filterList(item);

        (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
}
