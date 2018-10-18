package tip.dgts.eventapp.ui.eventSimple.sessions

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import io.realm.Realm
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Feedback
import tip.dgts.eventapp.model.data.Schedule

class SessionsPresenter : MvpNullObjectBasePresenter<SessionsView>() {
    lateinit var realm : Realm
    private val schedules: List<Schedule>? = ArrayList()

    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getSchedules(id: Int) {
        Log.e(Event.eventId, "Event ID: $id")
        val event = realm.where(Event::class.java).equalTo(Event.eventId, id).findFirst()
        view.setList(event!!.schedules)
    }


    fun onStop() {
        realm.close()

    }
}
