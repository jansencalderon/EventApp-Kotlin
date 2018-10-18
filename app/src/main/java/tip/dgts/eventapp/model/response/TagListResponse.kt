package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import tip.dgts.eventapp.model.data.Tag

class TagListResponse {

    @SerializedName("data")
    @Expose
    var data: List<Tag> = ArrayList()
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

}
