package tip.dgts.eventapp.model.response


open class BasicResponse {

    var success: Boolean = false
    var message: String? = null


    val isSuccess: Boolean
        get() {
            return success
        }
}
