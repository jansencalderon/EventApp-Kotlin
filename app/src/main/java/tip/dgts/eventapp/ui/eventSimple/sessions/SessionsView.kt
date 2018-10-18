package tip.dgts.eventapp.ui.eventSimple.sessions

import com.hannesdorfmann.mosby.mvp.MvpView

import io.realm.RealmList
import tip.dgts.eventapp.model.data.Schedule

interface SessionsView : MvpView {

    fun showAlert(message: String?)

    fun startLoading()

    fun stopLoading()

    fun setList(schedules: List<Schedule>)
}
