package tip.dgts.eventapp.ui.eventSimple.feedbacks

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Feedback
import tip.dgts.eventapp.model.data.Sponsor

interface FeedbacksView : MvpView {

    fun showAlert(message: String)

    fun startLoading()

    fun startLoadingDialog()

    fun stopLoadingDialog()

    fun stopLoading()

    fun setList(list: List<Feedback>)

    fun refreshList()

    fun feedbackSuccess()
}
