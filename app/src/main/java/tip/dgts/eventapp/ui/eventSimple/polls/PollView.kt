package tip.dgts.eventapp.ui.eventSimple.polls

import com.hannesdorfmann.mosby.mvp.MvpView

import io.realm.RealmResults
import tip.dgts.eventapp.model.data.Nominee
import tip.dgts.eventapp.model.data.Poll

interface PollView : MvpView {

    fun showAlert(message: String)

    fun onItemPollClicked(poll: Poll)

    fun startLoading()

    fun stopLoading()

    fun setList(polls: RealmResults<Poll>)

    fun startLoadingDialog()

    fun stopLoadingDialog()

    fun setNomineeList(data: List<Nominee>)

    fun onNomineeClicked(nominee: Nominee)

    fun votingSuccess(eventId: Int, pollId: Int)

    fun alreadyVoted()
}
