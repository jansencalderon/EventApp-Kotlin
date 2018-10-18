package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Token : RealmObject() {

    @PrimaryKey
    @SerializedName("key")
    @Expose
    var tokenKey: String? = null
    @SerializedName("type")
    @Expose
    var tokenType: String? = null
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null

}
