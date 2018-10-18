package tip.dgts.eventapp.app

/**
 * Created by Cholo Mia on 12/4/2016.
 */

object Endpoints {

    const val _ID = "{id}/"
    //public static final String BASE_URL = "http://10.3.32.204:8000";
    const val BASE_URL = "https://eventsapp.dgts.ph"


    const val API_URL = "$BASE_URL/api/v2/"
    const val IMAGE_LINK = "https://eventsapp.dgts.ph/storage/images/"
    const val LOGIN = "loginUser"

    const val VERIFY = "verifyUser"
    const val VERIFY_RESEND_EMAIL = "resendEmail"


    const val GET_USER_EVENTS = "getAllUserEvents"
    const val GET_PACKAGES = "getPackages"
    const val GET_LOCATIONS = "getAllLocations"
    const val ADD_EVENT = "addEventApp"


    const val SAVE_USER_TOKEN = "saveUserToken"
    const val DELETE_USER_TOKEN = "deleteUserToken"
    const val GET_SINGLE_EVENT = "getEventById"

    //using

    const val USER_LOGIN = "login"
    const val USER_REGISTER = "register"
    const val USER_UPDATE = "update-profile"

    const val GET_ALL_EVENTS = "organizer-events"
    const val GET_ALL_LIKED_EVENTS = "organizer-events"
    const val RESERVE_EVENT = "events/reserve"
    const val GET_USER_TICKETS = "users/{user_id}/tickets"
    const val GET_USER_SINGLE_TICKET = "users/{user_id}/tickets/{ticket_id}"
    const val LIKE_EVENT = "users/{user_id}/bookmarks"
    const val UNLIKE_EVENT = "users/{user_id}/bookmarks/{event_id}"
    const val GET_USER_INTERESTS = "users/{user_id}/interests"
    const val SAVE_INTEREST = "users/{user_id}/interests"
    const val DELETE_INTEREST = "users/{user_id}/interests/{interest_id}"

    const val ADD_FEEDBACK = "organizer-events/{event_id}/feedbacks"
    const val GET_EVENT_FEEDBACK = "organizer-events/{event_id}/feedbacks"

    const val GET_EVENT_POLLS = "organizer-events/{event_id}/voting/"
    const val GET_EVENT_POLLS_NOMINEE = "organizer-events/{event_id}/voting/{voting_id}/nominees"
    const val ADD_EVENT_POLL_VOTE = "organizer-events/{event_id}/voting/{voting_id}/votes"


    const val GET_TAGS = "tags"
}
