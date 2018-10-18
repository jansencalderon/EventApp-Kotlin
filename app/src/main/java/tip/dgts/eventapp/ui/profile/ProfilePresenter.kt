package tip.dgts.eventapp.ui.profile

import android.annotation.SuppressLint
import android.util.Log

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter

import java.io.File
import java.util.ArrayList

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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import tip.dgts.eventapp.model.data.Ticket
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.model.response.BasicResponse
import tip.dgts.eventapp.model.response.EventListResponse
import tip.dgts.eventapp.model.response.InterestListResponse
import tip.dgts.eventapp.model.response.TagListResponse
import tip.dgts.eventapp.model.response.TicketListResponse

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

class ProfilePresenter : MvpNullObjectBasePresenter<ProfileView>() {
    private var realm: Realm? = null
    private var user: User? = null
    private var ticketRealmResults: RealmResults<Ticket>? = null
    private var interestRealmResults: RealmResults<Interest>? = null
    private var tagRealmResults: RealmResults<Tag>? = null
    private var likedEventRealmResults: RealmResults<Event>? = null

    fun onStart() {
        realm = Realm.getDefaultInstance()
        user = App.user

        ticketRealmResults = realm!!.where(Ticket::class.java).findAllAsync()
        ticketRealmResults!!.addChangeListener { tickets -> view.setList(realm!!.copyFromRealm(tickets))}

        interestRealmResults = realm!!.where(Interest::class.java).findAllAsync()
        interestRealmResults!!.addChangeListener { interests -> view.setInterests(realm!!.copyFromRealm(interests)) }

        tagRealmResults = realm!!.where(Tag::class.java).findAllAsync()
        tagRealmResults!!.addChangeListener { tags -> view.setTags(tags) }

        likedEventRealmResults = realm!!.where(Event::class.java).equalTo(Event.eventLiked, true).findAllAsync()
        likedEventRealmResults!!.addChangeListener { eventRealmResults -> view.setLikedEvents(eventRealmResults) }

    }

    fun getTickets(id: Int) {
        App.instance.apiInterface.getTickets(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!)
                .flatMap { ticketListResponse -> saveTicketListObservable(ticketListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    //getView().startLoading();
                }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("getTickets", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getTickets", e.message)
                        if (e is HttpException) {
                            view.showAlert(Constants.INTERNET_ERROR)
                        } else {
                            view.showAlert(Constants.SERVER_ERROR)
                        }
                        view.stopLoading()
                    }
                })
    }

    //get tags
    fun getInterests(id: Int) {
        App.instance.apiInterface.getInterests(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!)
                .flatMap { interestListResponse -> saveInterestListObservable(interestListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // getView().startLoading();
                }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("getInterests", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getInterests", e.message)
                        if (e is HttpException) {
                            view.showAlert(Constants.INTERNET_ERROR)
                        } else {
                            view.showAlert(Constants.SERVER_ERROR)
                        }
                        view.stopLoading()
                    }
                })
    }


    fun getTags() {
        App.instance.apiInterface.getTags(Constants.BEARER + App.token.tokenKey, Constants.APPJSON)
                .flatMap { tagListResponse -> saveTagListObservable(tagListResponse.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // getView().startLoading();
                }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("getTags", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getTags", e.message)
                        if (e is HttpException) {
                            view.showAlert(Constants.INTERNET_ERROR)
                        } else {
                            view.showAlert(Constants.SERVER_ERROR)
                        }
                    }
                })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString)
    }


    @SuppressLint("CheckResult")
    fun getLikedEvents(i: Int) {
        App.instance.apiInterface.getAllLikedEvents(Constants.BEARER + App.token.tokenKey, Constants.APPJSON)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventListResponse>() {
                    override fun onSuccess(eventListResponse: EventListResponse) {
                        view.stopLoading()
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransactionAsync({  realm.copyToRealmOrUpdate(eventListResponse.data) }, { realm.close() }, { realm.close() })
                    }

                    override fun onError(e: Throwable) {
                        Log.e("likedEvents", e.message)
                        view.stopLoading()
                        view.showAlert(e.message)
                    }
                })
    }

    fun addInterest(tagId: Int) {
        App.instance.apiInterface.saveInterest(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!, tagId)
                .flatMap { basicResponse -> checkResult(basicResponse) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("addInterest", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("addInterest", e.message)
                        view.stopLoading()
                        view.showAlert(e.message)
                    }
                })
    }

    fun deleteInterest(interest: Interest) {
        App.instance.apiInterface.deleteInterest(Constants.BEARER + App.token.tokenKey, Constants.APPJSON, App.user.id!!, interest.id)
                .flatMap { basicResponse -> checkResult(basicResponse) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .doFinally { view.stopLoading() }
                .subscribe(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean) {
                        Log.d("deleteInterest", "success")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("deleteInterest", e.message)
                        view.stopLoading()
                        view.showAlert(e.message)
                    }
                })
    }


    //Start of realm functions
    private fun checkResult(basicResponse: BasicResponse): SingleSource<Boolean> {
        return Single.create {
            if (basicResponse.isSuccess) {
                getInterests(0)
                view.dismissAddInterestDialog()
            } else
                view.showAlert(basicResponse.message)
        }
    }


    private fun saveTagListObservable(data: List<Tag>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    Log.d("saveTags", Thread.currentThread().name)
                    realm.executeTransaction {
                        realm.delete(Tag::class.java)
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

    private fun saveInterestListObservable(data: List<Interest>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    Log.d("saveInterest", Thread.currentThread().name)
                    realm.executeTransaction {
                        realm.delete(Interest::class.java)
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


    fun onStop() {
        realm!!.removeAllChangeListeners()
        realm!!.close()
    }

    companion object {

        private val TAG = ProfilePresenter::class.java.simpleName
    }
}
