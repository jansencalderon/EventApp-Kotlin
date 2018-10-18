package tip.dgts.eventapp.ui.eventSimple.polls

import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Poll
import tip.dgts.eventapp.model.data.Ticket
import tip.dgts.eventapp.model.response.BasicResponse
import tip.dgts.eventapp.model.response.NomineeListResponse
import tip.dgts.eventapp.model.response.PollListResponse
import java.net.UnknownHostException

class PollPresenter : MvpNullObjectBasePresenter<PollView>() {

    lateinit var realm: Realm
    lateinit var pollRealmResults: RealmResults<Poll>

    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getPolls(eventId: Int) {
        pollRealmResults = realm.where(Poll::class.java).equalTo("eventId", eventId).findAllAsync()
        view.setList(pollRealmResults)
        pollRealmResults.addChangeListener { polls -> view.setList(polls) }
        App.instance.apiInterface.getPolls(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId, App.user.id)
                .flatMap { pollListResponse -> savePollListObservable(pollListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(t: Boolean) {
                        Log.d("getPolls", "success")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException || e is UnknownHostException) {
                            view.showAlert("Can't connect right now, please try again")
                        } else {
                            view.showAlert("Something weird happened, please try again")
                        }
                        Log.e("getPolls", e.message)
                    }

                })

    }


    fun getNominees(eventId: Int, pollId: Int) {
        App.instance.apiInterface.getNominees(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId, pollId, App.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoadingDialog() }
                .doFinally { view.stopLoadingDialog() }
                .subscribe(object : SingleObserver<NomineeListResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onSuccess(nomineeListResponse: NomineeListResponse) {
                        if (nomineeListResponse.success!!) {
                            view.setNomineeList(nomineeListResponse.data)
                        } else {
                            view.showAlert("Something weird happened, please try again")
                        }
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

    fun addVote(eventId: Int, pollId: Int, userId: Int, nomineeId: Int) {
        App.instance.apiInterface.addVote(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId, pollId, userId, nomineeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoadingDialog() }
                .doFinally { view.stopLoadingDialog() }
                .subscribe(object : SingleObserver<BasicResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(basicResponse: BasicResponse) {
                        if (basicResponse.isSuccess) {
                            view.votingSuccess(eventId, pollId)
                        } else {
                            view.showAlert("Something weird happened, please try again")
                        }
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


    fun onStop() {
        if (pollRealmResults.isValid) {
            pollRealmResults.removeAllChangeListeners()
        }
        realm.close()
    }


    //realm
    private fun savePollListObservable(data: List<Poll>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    Log.d("getPolls", Thread.currentThread().name)
                    realm.executeTransaction {
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
