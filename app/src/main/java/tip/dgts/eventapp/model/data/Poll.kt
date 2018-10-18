package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Poll(
        @PrimaryKey
        @SerializedName("id")
        @Expose
        var id: Int = 0,
        @SerializedName("event_id")
        @Expose
        var eventId: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String ?= null,
        @SerializedName("description")
        @Expose
        var description: String ?= null,
        @SerializedName("image")
        @Expose
        var image: String ?= null ,
        @SerializedName("nominees")
        @Expose
        var nominees: RealmList<Nominee> = RealmList()
) : RealmObject()
