package tip.dgts.eventapp.ui.profile

import com.hannesdorfmann.mosby.mvp.MvpView

import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Ticket

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

interface ProfileView : MvpView {


    fun showAlert(message: String?)

    fun onEdit()

    fun startLoading()

    fun stopLoading()

    fun finishAct()

    /*void onBirthdayClicked();

    void onPhotoClicked();*/

    fun finish()

    fun setList(tickets: List<Ticket>)

    fun setInterests(interests: List<Interest>)

    fun setTags(tags: List<Tag>)

    fun dismissAddInterestDialog()

    fun setLikedEvents(eventRealmResults: RealmResults<Event>)

    fun onInterestClicked(interest: Interest)
}
