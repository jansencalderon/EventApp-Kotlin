package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Feedback : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("event_id")
    @Expose
    var eventId: Int? = null
    @SerializedName("feedback")
    @Expose
    var feedback: String? = null
    @SerializedName("rate")
    @Expose
    var rate: Int? = null
    @SerializedName("user")
    var otherUser: OtherUser? = null

    companion object {
        const val KEY_EVENT_ID = "eventId";
    }


}
