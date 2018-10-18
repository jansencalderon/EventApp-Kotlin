package tip.dgts.eventapp.ui.main.byCategory

import android.annotation.SuppressLint
import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import com.hannesdorfmann.mosby.mvp.MvpPresenter
import java.util.concurrent.TimeUnit

import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.app.ApiInterface
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.response.EventListResponse
import tip.dgts.eventapp.model.response.InterestListResponse
import tip.dgts.eventapp.ui.main.MainView
import java.net.UnknownHostException

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class ByTagPresenter : MvpNullObjectBasePresenter<ByTagView>() {
    private var realm: Realm? = null
    private val TAG = ByTagPresenter::class.java.simpleName
    private var eventRealmResults: RealmResults<Event>? = null
    fun onStart(title: String) {
        realm = Realm.getDefaultInstance()

        eventRealmResults = realm!!.where(Event::class.java).contains("tags.title", title).findAllAsync()
        eventRealmResults!!.addChangeListener { eventRealmResults -> view.setEvents(eventRealmResults) }

    }


    @SuppressLint("CheckResult")
    fun getEvents(tagId: Int) {
        App.instance.apiInterface.getAllEvents(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!, tagId)
                .flatMap { eventListResponse -> saveEventListObservable(eventListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("getEvents", "success")
                    }
                    override fun onError(e: Throwable) {
                        if (e is HttpException || e is UnknownHostException) {
                            view.showAlert("Can't connect right now, please try again")
                        }else{
                            view.showAlert("Something weird happened, please try again")
                        }
                        Log.e("getEvents", e.message)
                    }
                })
    }


    //realm functions]
    private fun saveEventListObservable(data: List<Event>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction {
                        Log.e("getEvents", Thread.currentThread().name)
                        realm.insertOrUpdate(data) }
                }
            } catch (e: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            } finally {
                emitter.onSuccess(true)
            }
        }
    }


    fun onStop() {
        if (eventRealmResults!!.isValid) {
            eventRealmResults!!.removeAllChangeListeners()
        }
        realm!!.close()
    }


}
