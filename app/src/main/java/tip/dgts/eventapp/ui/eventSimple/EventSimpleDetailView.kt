package tip.dgts.eventapp.ui.eventSimple

import com.hannesdorfmann.mosby.mvp.MvpView

import tip.dgts.eventapp.model.data.Location
import tip.dgts.eventapp.model.data.Sponsor
import tip.dgts.eventapp.model.data.Timestamp


/**
 * Created by Mark Jansen Calderon on 1/26/2017.
 */

interface EventSimpleDetailView : MvpView {

    fun showAlert(message: String?)

    fun startLoading()

    fun stopLoading()

    fun refreshEvent()

    fun onLocationClicked(location: Location)

    fun onSponsorClicked(sponsor: Sponsor)

    fun showSnackbar(s: String)
}
