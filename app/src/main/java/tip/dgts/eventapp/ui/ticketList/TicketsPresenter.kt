package tip.dgts.eventapp.ui.ticketList

import android.annotation.SuppressLint
import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

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
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Ticket
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.model.response.TicketListResponse

/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

class TicketsPresenter : MvpNullObjectBasePresenter<TicketsListView>() {
    private var ticketsRealmResult: RealmResults<Ticket>? = null
    private val query: String? = null
    private var realm: Realm? = null
    private val user: User? = null

    fun onStart() {
        realm = Realm.getDefaultInstance()

        ticketsRealmResult = realm!!.where(Ticket::class.java).findAllAsync()
        ticketsRealmResult!!.addChangeListener(RealmChangeListener { view.setTickets(realm!!.copyFromRealm(ticketsRealmResult!!)) })

        view.setTickets(ticketsRealmResult!!)


    }

    fun onStop() {
        if (ticketsRealmResult!!.isValid) {
            ticketsRealmResult!!.removeAllChangeListeners()
        }
        realm!!.close()
    }

    @SuppressLint("CheckResult")
    fun loadTickets() {
        view.startLoading()
        Log.d(TAG, Constants.BEARER + App.token.tokenKey + " " + Constants.APPJSON)
        App.instance.apiInterface.getTickets(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!)
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
                        Log.e("getTickets", e.message)
                    }
                })
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

    companion object {
        private val TAG = TicketsPresenter::class.java.simpleName
    }


}

