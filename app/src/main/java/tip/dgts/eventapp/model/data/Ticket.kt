package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Ticket : RealmObject() {

    @SerializedName("id")
    @Expose
    var id: Int = 0
    @PrimaryKey
    @SerializedName("order_number")
    @Expose
    var orderNumber: Long? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("seat")
    @Expose
    var seat: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("event")
    @Expose
    var event: TicketEvent? = null
}
