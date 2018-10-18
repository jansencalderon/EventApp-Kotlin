package tip.dgts.eventapp.ui.main.tabs

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Event

/**
 * Created by Sen on 2/28/2017.
 */

interface MainPageView : MvpView {
    fun setEvents(events: List<Event>)

    fun internet(status: Boolean?)

    fun checkResult(count: Int)

    fun onEventClicked(event: Event)

    fun showAlert(s: String?)
}
