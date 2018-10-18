package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */


open class User(
        @PrimaryKey
        @SerializedName("id")
        @Expose
        var id: Int = 0,
        @SerializedName("first_name")
        @Expose
        var firstName: String = "",
        @SerializedName("last_name")
        @Expose
        var lastName: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("contact")
        @Expose
        var contact: String = "",
        @SerializedName("birthday")
        @Expose
        var birthday: String = "",
        @SerializedName("address")
        @Expose
        var address: String = "",
        @SerializedName("image")
        @Expose
        var image: String = ""
) : RealmObject()