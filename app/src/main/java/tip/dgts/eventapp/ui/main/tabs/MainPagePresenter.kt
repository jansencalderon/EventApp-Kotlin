package tip.dgts.eventapp.ui.main.tabs

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.utils.DateTimeUtils

/**
 * Created by Sen on 2/28/2017.
 */

class MainPagePresenter : MvpNullObjectBasePresenter<MainPageView>() {
    private var realm: Realm? = null
    private var eventRealmResults: RealmResults<Event>? = null
    private var type: String? = null

    fun onStart(type: String?) {
        realm = Realm.getDefaultInstance()
        this.type = type
        eventRealmResults = realm!!.where(Event::class.java).findAll()
        eventRealmResults!!.addChangeListener(RealmChangeListener { filterList() })
    }

    private fun filterList() {
        if (eventRealmResults!!.isLoaded && eventRealmResults!!.isValid) {
            val eventList: List<Event>
            if (type == "Liked") {
                eventList = realm!!.copyFromRealm(eventRealmResults!!.where().equalTo("liked", true).findAll())
            } else if (type == "Booked") {
                eventList = realm!!.copyFromRealm(eventRealmResults!!.where().equalTo("reserved", true).findAll())
            } else {
                eventList = realm!!.copyFromRealm(eventRealmResults!!)
            }
            view.setEvents(eventList)
        }
    }

    fun onStop() {
        if (eventRealmResults!!.isValid) {
            eventRealmResults!!.removeAllChangeListeners()
        }
        realm!!.close()
    }

}
