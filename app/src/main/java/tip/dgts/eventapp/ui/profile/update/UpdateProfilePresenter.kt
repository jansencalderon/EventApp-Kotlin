package tip.dgts.eventapp.ui.profile.update

import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.response.BasicResponse
import java.net.UnknownHostException

class UpdateProfilePresenter : MvpNullObjectBasePresenter<UpdateProfileView>() {

    private val TAG = UpdateProfilePresenter::class.java.simpleName

    fun update(email: String, fname: String, lname: String, bday: String, contact: String, address: String) {
        view.startLoading()
        App.instance.apiInterface.updateUser(Constants.BEARER + App.token.tokenKey!!, Constants.APPJSON, fname, lname, email, contact, bday, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .doFinally {  }
                .subscribe(object : SingleObserver<BasicResponse>{
                    override fun onSuccess(basicResponse: BasicResponse) {
                        if (basicResponse.isSuccess) {
                            view.onUpdateSuccess()
                        }
                    }

                    override fun onSubscribe(d: Disposable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
