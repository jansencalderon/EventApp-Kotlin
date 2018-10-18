package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Nominee : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("voting_id")
    @Expose
    var votingId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("voted")
    @Expose
    var voted: Boolean? = null
}
