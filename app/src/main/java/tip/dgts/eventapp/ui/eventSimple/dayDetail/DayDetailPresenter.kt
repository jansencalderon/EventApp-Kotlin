package tip.dgts.eventapp.ui.eventSimple.dayDetail

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import java.sql.Time

import io.realm.Realm
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Schedule
import tip.dgts.eventapp.model.data.Timestamp

class DayDetailPresenter : MvpNullObjectBasePresenter<DayDetailView>() {
    lateinit var realm: Realm
    private val schedules: List<Schedule> = ArrayList()

    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getSchedules(id: Int) {
        Log.e(Event.eventId, "Event ID: $id")
        val timestamp = realm.where(Timestamp::class.java).equalTo(Timestamp.timestampId, id).findFirst()
        view.setList(timestamp?.schedules!!)
    }


    fun onStop() {
        realm.close()
    }

    fun getDay(id: Int): Timestamp? {
        return realm.where(Timestamp::class.java).equalTo(Timestamp.timestampId, id).findFirst()
    }
}
