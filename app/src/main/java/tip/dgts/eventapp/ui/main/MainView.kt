package tip.dgts.eventapp.ui.main

import com.hannesdorfmann.mosby.mvp.MvpView

import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.User


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

interface MainView : MvpView {

    fun stopLoading()

    fun startLoading()

    fun displayUserData(user: User)

    fun showAlert(s: String?)

    fun setEvents(eventToday: List<Event>, eventUpcoming: List<Event>)

    fun refreshList()

    fun setTags(interests: RealmResults<Interest>)

    fun onInterestClicked(interest: Interest)
}
