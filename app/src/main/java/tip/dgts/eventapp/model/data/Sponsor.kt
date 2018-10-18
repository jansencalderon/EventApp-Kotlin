package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

open class Sponsor : RealmObject() {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("event_id")
    @Expose
    var eventId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("details")
    @Expose
    var details: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
}
