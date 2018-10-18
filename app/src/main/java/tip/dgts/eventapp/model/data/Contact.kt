package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

open class Contact : RealmObject() {

    @SerializedName("contacts")
    @Expose
    var contacts: String? = null

}