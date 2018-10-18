package tip.dgts.eventapp.ui.eventSimple

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Ticket
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.model.response.BasicResponse
import java.net.UnknownHostException

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

class EventSimpleDetailPresenter : MvpNullObjectBasePresenter<EventSimpleDetailView>() {

    private val eventRealmResults: RealmResults<Event>? = null
    lateinit var realm: Realm
    private val user: User? = null
    lateinit var event: Event
    private val TAG = EventSimpleDetailPresenter::class.java.simpleName

    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getEvent(id: Int): Event {
        event = realm.where(Event::class.java).equalTo(Event.eventId, id).findFirst()!!
        return event
    }


    fun reserve(id: Int, eventId: Int) {
        view.startLoading()
        Handler().postDelayed({
            App.instance.apiInterface.reserveEvent(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId, App.user.id).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    view.stopLoading()
                    if (response.isSuccessful) {
                        if (response.body()!!.isSuccess) {
                            view.showAlert("Reservation Successful")
                            realm.beginTransaction()
                            event.reserved = true
                            realm.commitTransaction()
                            view.refreshEvent()
                        } else {
                            view.showAlert("Failed")
                            view.refreshEvent()

                        }
                    } else {
                        view.refreshEvent()
                        view.showAlert(if (response.message() != null) response.message()
                        else
                            "Unknown Error")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: Error calling login api", t)
                    view.stopLoading()
                    view.showAlert("Error Connecting to Server")
                    view.refreshEvent()
                }
            })
        }, 2000)

    }

    fun like(id: Int, eventId: Int?) {

        App.instance.apiInterface.likeEvent(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id, eventId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    realm.beginTransaction()
                    event.liked = true
                    realm.commitTransaction()
                    view.refreshEvent()
                }
                .subscribe(object : SingleObserver<BasicResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(response: BasicResponse) {
                        if (response.isSuccess) {
                            view.refreshEvent()
                            view.showAlert("Liked")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onFailure: Error calling login api", e)
                        realm.beginTransaction()
                        event!!.liked = false
                        realm.commitTransaction()
                        view.refreshEvent()
                        if (e is HttpException || e is UnknownHostException) {
                            view.showAlert("Can't connect right now, please try again")
                        } else {
                            view.showAlert(e.message)
                        }
                    }
                })

    }

    fun unlike(id: Int, eventId: Int?) {
        App.instance.apiInterface.unlikeEvent(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!, eventId!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    realm.beginTransaction()
                    event.liked = false
                    realm.commitTransaction()
                    view.refreshEvent()
                }
                .subscribe(object : SingleObserver<BasicResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(response: BasicResponse) {
                        if (response.isSuccess)
                            view.refreshEvent()
                        view.showAlert("Unliked")
                    }

                    override fun onError(e: Throwable) {
                        realm.beginTransaction()
                        event.liked = true
                        realm.commitTransaction()
                        view.refreshEvent()
                        Log.e(TAG, "onFailure: Error calling login api", e)
                        if (e is HttpException || e is UnknownHostException) {
                            view.showAlert("Can't connect right now, please try again")
                        } else {
                            view.showAlert(e.message)
                        }
                    }
                })
    }


    fun onStop() {
        realm.removeAllChangeListeners()
        realm.close()

    }


    @SuppressLint("CheckResult")
    fun getTickets() {
        Log.d(TAG, Constants.BEARER + App.token.tokenKey + " " + Constants.APPJSON)
        App.instance.apiInterface.getTickets(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id)
                .flatMap { ticketListResponse -> saveTicketListObservable(ticketListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("getTickets", "success")
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

    fun getEventTicket(eventName: String?): Ticket? {
        return realm.where(Ticket::class.java).equalTo("event.name", eventName).findFirst()
    }

    private fun saveTicketListObservable(data: List<Ticket>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    Log.d("saveTickets", Thread.currentThread().name)
                    realm.executeTransaction {
                        realm.delete(Ticket::class.java)
                        realm.insertOrUpdate(data)
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onSuccess(true)
            }
        }
    }

}
