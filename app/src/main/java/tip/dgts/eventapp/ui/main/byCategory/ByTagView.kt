package tip.dgts.eventapp.ui.main.byCategory

import com.hannesdorfmann.mosby.mvp.MvpView

import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.User


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

interface ByTagView : MvpView {

    fun stopLoading()

    fun startLoading()

    fun showAlert(s: String)

    fun setEvents(events: List<Event>)

    fun refreshList()

    fun onEventClicked(event: Event)

}
