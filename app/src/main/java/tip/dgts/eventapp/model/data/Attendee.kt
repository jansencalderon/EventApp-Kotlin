package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Attendee : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("order_number")
    @Expose
    var orderNumber: Long? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("contact")
    @Expose
    var contact: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("attended")
    @Expose
    var attended: Int? = null
    @SerializedName("prize_id")
    @Expose
    var prizeId: Int? = null
}
