package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Organizer(@PrimaryKey
                     @SerializedName("id")
                     @Expose
                     var id: Int = 0,
                     @SerializedName("name")
                     @Expose
                     var name: String ? = null,
                     @SerializedName("email")
                     @Expose
                     var email: String ? = null,
                     @SerializedName("description")
                     @Expose
                     var description: String ? = null,
                     @SerializedName("address")
                     @Expose
                     var address: String ? = null,
                     @SerializedName("contact")
                     @Expose
                     var contact: String ? = null,
                     @SerializedName("image")
                     @Expose
                     var image: String ? = null,
                     @SerializedName("status")
                     @Expose
                     var status: String ? = null,
                     @SerializedName("tags")
                     @Expose
                     var tags: RealmList<Tag> = RealmList())
    : RealmObject(){

}
