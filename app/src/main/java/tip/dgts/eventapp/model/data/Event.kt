package tip.dgts.eventapp.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Mark Jansen Calderon on 1/27/2017.
 */

open class Event(
        @PrimaryKey
        @SerializedName("id")
        @Expose
        var id: Int = 0,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("description")
        @Expose
        var description: String? = null,

        @SerializedName("image")
        @Expose
        var image: String? = null,

        @SerializedName("price")
        @Expose
        var price: String? = null,

        @SerializedName("ticket_max")
        @Expose
        var ticketMax: Int = 0,

        @SerializedName("tags")
        @Expose
        var tags: RealmList<Tag> = RealmList(),

        @SerializedName("organizer")
        @Expose
        var organizer: Organizer? = null,
        @SerializedName("location")
        @Expose
        var location: RealmList<Location> = RealmList(),
        @SerializedName("timestamp")
        @Expose
        var timestamp: RealmList<Timestamp> = RealmList(),
        @SerializedName("contact")
        @Expose
        var contact: RealmList<Contact> = RealmList(),
        @SerializedName("feedback")
        var feedbacks: RealmList<Feedback> = RealmList(),
        @SerializedName("speakers")
        @Expose
        var speakers: RealmList<Speaker> = RealmList(),
        @SerializedName("sponsors")
        @Expose
        var sponsors: RealmList<Sponsor> = RealmList(),
        @SerializedName("schedules")
        @Expose
        var schedules: RealmList<Schedule> = RealmList(),

        @SerializedName("attendees")
        @Expose
        var attendees: RealmList<Attendee> = RealmList(),

        @SerializedName("liked")
        var liked: Boolean = false,

        @SerializedName("reserve")
        var reserved: Boolean = true,
        var speakerSize: Int = 0,
        var totalLikes: Int = 0,
        var slotsAvailable: Int = 0) : RealmObject() {


    fun speakerSize(): Int {
        return speakers.size
    }

    fun attendeeSize(): Int {
        return attendees.size
    }

    companion object {
        var eventId = "id"
        var eventLiked = "reserved"
    }
}
