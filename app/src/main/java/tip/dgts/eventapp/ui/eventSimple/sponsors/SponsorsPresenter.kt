package tip.dgts.eventapp.ui.eventSimple.sponsors

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Sponsor

class SponsorsPresenter : MvpNullObjectBasePresenter<SponsorsView>() {

    lateinit var realm: Realm
    private val sponsors: List<Sponsor>? = null
    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getSponsors(id: Int) {
        Log.e(Event.eventId, "Event ID: $id")
        val event = realm.where(Event::class.java).equalTo(Event.eventId, id).findFirst()
        view.setList(event!!.sponsors)
    }


    fun onStop() {
        realm.close()

    }
}
