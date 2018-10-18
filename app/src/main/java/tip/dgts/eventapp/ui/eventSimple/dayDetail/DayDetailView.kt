package tip.dgts.eventapp.ui.eventSimple.dayDetail

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Schedule

interface DayDetailView : MvpView {

    fun showAlert(message: String)

    fun startLoading()

    fun stopLoading()

    fun setList(schedules: List<Schedule>)

    fun checkResult(count: Int)
}
