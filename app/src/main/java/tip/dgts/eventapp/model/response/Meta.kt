package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import tip.dgts.eventapp.model.data.Cursor

open class Meta : RealmObject() {
    @SerializedName("cursor")
    @Expose
    var cursor: Cursor? = null
}
