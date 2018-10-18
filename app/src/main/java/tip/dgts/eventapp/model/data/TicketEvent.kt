package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TicketEvent : RealmObject() {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("ticket_max")
    @Expose
    var ticketMax: Int? = null
}
