package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import tip.dgts.eventapp.model.data.Nominee

class NomineeListResponse {

    @SerializedName("data")
    @Expose
    var data: List<Nominee> = ArrayList()
    @SerializedName("success")
    @Expose
    var success: Boolean? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

}
