package tip.dgts.eventapp.ui.main

import android.annotation.SuppressLint
import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit2.HttpException
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.model.data.Interest
import tip.dgts.eventapp.model.data.Tag
import java.net.UnknownHostException

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class MainPresenter : MvpNullObjectBasePresenter<MainView>() {
    lateinit var realm: Realm
    private val TAG = MainPresenter::class.java.simpleName
    lateinit var eventRealmResults: RealmResults<Event>
    lateinit var interestRealmResults: RealmResults<Interest>

    fun onStart() {
        realm = Realm.getDefaultInstance()

        eventRealmResults = realm.where(Event::class.java).findAllAsync()
        eventRealmResults.addChangeListener(RealmChangeListener { view.refreshList() })

        interestRealmResults = realm.where(Interest::class.java).findAllAsync()
        interestRealmResults.addChangeListener { interests -> view.setTags(interests) }


    }

    fun getTag(tagId: Int): Tag? {
        return realm.where(Tag::class.java).equalTo(Tag.TAG_ID, tagId).findFirst()
    }

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
                        Log.d("getTickets", "success")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException || e is UnknownHostException) {
                            view.showAlert("Can't connect right now, please try again")
                        }else{
                            view.showAlert("Something weird happened, please try again")
                        }
                        Log.e("getTickets", e.message)
                    }
                })


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
                        Log.d("getTickets", "success")
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


    fun onStop() {
        if (eventRealmResults.isValid) {
            eventRealmResults.removeAllChangeListeners()
        }
        realm.close()
    }

    //realm functions

    private fun saveInterestListObservable(data: List<Interest>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction {
                        realm.copyToRealmOrUpdate(data)
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onSuccess(true)
            }
        }
    }

    private fun saveEventListObservable(data: List<Event>): Single<Boolean> {
        return Single.create { emitter ->
            try {
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction {
                        realm.delete(Event::class.java)
                        realm.copyToRealmOrUpdate(data)
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
