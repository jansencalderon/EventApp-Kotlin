package tip.dgts.eventapp.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import tip.dgts.eventapp.model.data.Token
import tip.dgts.eventapp.model.data.User

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */

class LoginResponse(@SerializedName("data")
                    @Expose
                    var user: User) : BasicResponse() {

}
