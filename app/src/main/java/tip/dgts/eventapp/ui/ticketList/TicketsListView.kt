package tip.dgts.eventapp.ui.ticketList

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Ticket

interface TicketsListView : MvpView {
    fun onNewViewStateInstance()

    fun showAlert(s: String?)

    fun stopLoading()

    fun startLoading()

    fun setTickets(ticketList: List<Ticket>)

    fun onRefresh()

    fun onTicketClicked(ticket: Ticket)
}
