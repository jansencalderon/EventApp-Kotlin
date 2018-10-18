package tip.dgts.eventapp.ui.venue;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Mark Jansen Calderon on 2/15/2017.
 */

public interface LocationView extends MvpView {

    void openOnGoogleMaps();

    void openOnWaze();
}
