package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Speaker : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("event_id")
    @Expose
    var eventId: Int? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("contact")
    @Expose
    var contact: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
}
