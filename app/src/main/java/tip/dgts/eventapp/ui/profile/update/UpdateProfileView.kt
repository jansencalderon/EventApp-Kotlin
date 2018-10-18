package tip.dgts.eventapp.ui.profile.update

import com.hannesdorfmann.mosby.mvp.MvpView

interface UpdateProfileView : MvpView {
    fun onSubmit()

    fun showAlert(message: String)


    fun startLoading()

    fun stopLoading()

    fun onUpdateSuccess()

    fun onBirthdayClicked()
}
