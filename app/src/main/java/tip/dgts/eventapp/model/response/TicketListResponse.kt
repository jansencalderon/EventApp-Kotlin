package tip.dgts.eventapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList

import tip.dgts.eventapp.model.data.Ticket

class TicketListResponse {

    @SerializedName("data")
    @Expose
    var data: RealmList<Ticket> = RealmList()

}
