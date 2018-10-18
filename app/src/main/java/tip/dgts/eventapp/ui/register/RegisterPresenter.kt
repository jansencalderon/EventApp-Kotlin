package tip.dgts.eventapp.ui.register

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import java.io.IOException

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.response.BasicResponse
import java.net.UnknownHostException

class RegisterPresenter : MvpNullObjectBasePresenter<RegisterView>() {
    private val TAG = RegisterPresenter::class.java.simpleName

    fun register(email: String,
                 password: String,
                 confirmPassword: String,
                 firstName: String,
                 lastName: String,
                 birthday: String,
                 contact: String,
                 address: String) {
        if (email == "" || password == "" || confirmPassword == "" || firstName == "" || lastName == "" || birthday == "" ||
                contact == "" || address == "") {
            view.showAlert("Fill-up all fields")
        } else if (!password.contentEquals(confirmPassword)) {
            view.showAlert("Password does not match")
        } else if (password.length <= 6) {
            view.showAlert("Password must be minimum of 6 characters")
        } else {
            view.startLoading()
            App.instance.apiInterface.register(firstName, lastName, email, contact, birthday, address, password, confirmPassword, Constants.APPJSON)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe{ view.startLoading()}
                    .doFinally { view.stopLoading() }
                    .subscribe(object : SingleObserver<BasicResponse>{
                        override fun onSuccess(basicResponse: BasicResponse) {
                            if (basicResponse.isSuccess || basicResponse.message.equals("User successfully registered")) {
                                view.onRegistrationSuccess()
                            }
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            if (e is HttpException || e is UnknownHostException) {
                                view.showAlert("Can't connect right now, please try again")
                            } else {
                                view.showAlert("Something weird happened, please try again")
                            }
                            Log.e("getTickets", e.message)
                        }
                    })

        }
    }
}
