package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import tip.dgts.eventapp.model.data.Poll

class PollListResponse {

    @SerializedName("data")
    @Expose
    var data: List<Poll> = ArrayList()
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

}
