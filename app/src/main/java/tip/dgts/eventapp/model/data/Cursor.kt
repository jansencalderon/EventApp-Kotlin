package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

open class Cursor : RealmObject() {
    @SerializedName("current")
    @Expose
    var current: Int = 0
    @SerializedName("prev")
    @Expose
    var prev: Int = 0
    @SerializedName("next")
    @Expose
    var next: Int = 0
    @SerializedName("count")
    @Expose
    var count: Int = 0
}



