package tip.dgts.eventapp.ui.login

import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import retrofit2.HttpException
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.User
import java.util.concurrent.TimeUnit


class LoginPresenter : MvpNullObjectBasePresenter<LoginView>() {

    private val TAG = LoginPresenter::class.java.simpleName
    fun login(email: String, password: String) {
        if (email.isEmpty() || email == "") {
            view.showAlert("Please enter email")
        } else if (password.isEmpty() || password == "") {
            view.showAlert("Please enter Password")
        } else {
            App.instance.apiInterface.login(email, password)
                    .flatMap { loginResponse -> saveToRealmObservable(loginResponse.user) }
                    .delay(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.startLoading() }
                    .doFinally { view.stopLoading() }
                    .subscribe(object : DisposableSingleObserver<Boolean>() {
                        override fun onSuccess(t: Boolean) {
                            Log.d("saveUser", "success")
                            view.onLoginSuccess()
                        }
                        override fun onError(e: Throwable) {
                            if (e is HttpException) {
                                view.showAlert(Constants.INTERNET_ERROR)
                            } else {
                                view.showAlert(Constants.SERVER_ERROR)
                            }

                            Log.e(TAG, e.message)
                        }
                    })

        }
    }

    private fun saveToRealmObservable(data: User): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction { realm.insertOrUpdate(data) }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onSuccess(true)
            }
        }
    }

}
