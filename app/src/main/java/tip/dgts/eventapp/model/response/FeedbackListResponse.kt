package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import tip.dgts.eventapp.model.data.Feedback

class FeedbackListResponse : BasicResponse() {

    @SerializedName("data")
    @Expose
    var data: List<Feedback> = ArrayList()

}
