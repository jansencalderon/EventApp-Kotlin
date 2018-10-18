package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Schedule : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("event_id")
    @Expose
    var eventId: Int? = null
    @SerializedName("speaker_id")
    @Expose
    var speakerId: String? = null
    @SerializedName("map_id")
    @Expose
    var mapId: Int? = null
    @SerializedName("coordinate_id")
    @Expose
    var coordinateId: Int? = null
    @SerializedName("time_start")
    @Expose
    var timeStart: String? = null
    @SerializedName("time_end")
    @Expose
    var timeEnd: String? = null
    @SerializedName("details")
    @Expose
    var details: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null

    companion object {

        var ID = "id"
    }
}
