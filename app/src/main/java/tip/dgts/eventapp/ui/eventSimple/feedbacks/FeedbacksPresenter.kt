package tip.dgts.eventapp.ui.eventSimple.feedbacks

import android.annotation.SuppressLint
import android.support.v7.widget.AppCompatRatingBar
import android.util.Log
import android.widget.EditText

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import java.net.UnknownHostException

import io.reactivex.Completable
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
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Feedback
import tip.dgts.eventapp.model.data.Sponsor
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.response.BasicResponse
import tip.dgts.eventapp.model.response.FeedbackListResponse
import tip.dgts.eventapp.model.response.TicketListResponse

class FeedbacksPresenter : MvpNullObjectBasePresenter<FeedbacksView>() {

    private var realm: Realm? = null
    lateinit var feedbackRealmResults: RealmResults<Feedback>
    private val TAG = FeedbacksPresenter::class.simpleName

    fun onStart() {
        realm = Realm.getDefaultInstance()
    }

    fun getFeedback(id: Int) {
        Log.e(Event.eventId, "Event ID: $id")
        feedbackRealmResults = realm!!.where<Feedback>(Feedback::class.java).equalTo(Feedback.KEY_EVENT_ID, id).findAll()
        view.setList(feedbackRealmResults)
        feedbackRealmResults.addChangeListener { feedbacks, changeSet -> view.setList(feedbacks) }

    }


    fun onStop() {
        realm!!.close()
    }

    @SuppressLint("CheckResult")
    fun addRate(eventId: Int, rating: Int, comments: String) {
        if (comments == "") {
            view.showAlert("Please input comments")
            return
        }
        App.instance.apiInterface.addFeedback(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId, App.user.id!!, comments, rating)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoadingDialog() }
                .doFinally { view.stopLoadingDialog() }
                .subscribeWith(object : DisposableSingleObserver<BasicResponse>() {
                    override fun onSuccess(basicResponse: BasicResponse) {
                        if (basicResponse.isSuccess) {
                            view.feedbackSuccess()
                            getFeedbacksList(eventId)
                        } else
                            view.showAlert("Failed")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("addRate", e.message)

                        if (e is HttpException || e is UnknownHostException)
                            view.showAlert("Can't connect right now, please try again")
                        else
                            view.showAlert(e.message!!)
                    }
                })

    }

    fun getFeedbacksList(eventId: Int) {
        App.instance.apiInterface.getFeedbacks(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, eventId)
                .flatMap { feedbackListResponse -> saveFeedbackListObservable(feedbackListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(t: Boolean) {
                        Log.d("deleteInterest", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("deleteInterest", e.message)
                        view.stopLoading()
                        view.showAlert(e.message!!)
                    }
                })


    }


    private fun saveFeedbackListObservable(data: List<Feedback>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    Log.d("saveTags", Thread.currentThread().name)
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
