package tip.dgts.eventapp.model.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Tag(var id: Int = 0,
               @PrimaryKey
               var title: String? = null) : RealmObject() {
    override fun toString(): String {
        return title!!
    }

    companion object {
        val TAG_ID = "id"
        val TAG_TITLE = "title"
    }
}
