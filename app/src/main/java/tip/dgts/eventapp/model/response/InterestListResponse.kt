package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import tip.dgts.eventapp.model.data.Interest

class InterestListResponse {
    @SerializedName("data")
    @Expose
    var data: List<Interest> = ArrayList()
}
