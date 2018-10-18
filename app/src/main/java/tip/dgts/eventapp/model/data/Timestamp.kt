package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Timestamp : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("event_id")
    var eventId: Int = 0

    @SerializedName("timestamp_start")
    @Expose
    var timestampStart: String? = null
    @SerializedName("timestamp_end")
    @Expose
    var timestampEnd: String? = null

    @SerializedName("schedule")
    var schedules: RealmList<Schedule>? = null

    var toolbarTitle: String? = null

    companion object {

        var timestampId = "id"
    }
}
