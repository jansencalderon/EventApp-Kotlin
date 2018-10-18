package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import tip.dgts.eventapp.model.data.Event

class EventListResponse {

    @SerializedName("data")
    @Expose
    var data: List<Event> = ArrayList()
    @SerializedName("meta")
    @Expose
    private val meta: Meta? = null

}
