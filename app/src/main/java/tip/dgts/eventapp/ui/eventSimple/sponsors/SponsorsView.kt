package tip.dgts.eventapp.ui.eventSimple.sponsors

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Sponsor

interface SponsorsView : MvpView {

    fun showAlert(message: String?)

    fun startLoading()

    fun stopLoading()

    fun setList(sponsors: List<Sponsor>)
}
