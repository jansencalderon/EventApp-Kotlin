package tip.dgts.eventapp.ui.main.tabs

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpFragment

import io.realm.Realm
import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.Constants
import tip.dgts.eventapp.databinding.FragmentMainPageBinding
import tip.dgts.eventapp.model.data.Event
import tip.dgts.eventapp.ui.eventSimple.EventSimpleDetailActivity

/**
 * Created by Sen on 2/28/2017.
 */

class MainPageFragment : MvpFragment<MainPageView, MainPagePresenter>(), MainPageView {
    private val eventList: List<Event>? = null
    private var mPage: Int = 0
    private var type: String? = null
    private var binding: FragmentMainPageBinding? = null
    private var realm: Realm? = null
    private var adapter: MainListAdapter? = null

    override fun createPresenter(): MainPagePresenter {
        return MainPagePresenter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments!!.getInt(ARG_PAGE)
        type = arguments!!.getString(ARG_TYPE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_page, container, false)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MainListAdapter(mvpView)
        binding!!.recyclerView.adapter = adapter
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        realm = Realm.getDefaultInstance()
    }

    override fun onResume() {
        super.onResume()
        presenter.onStart(type)
    }

    override fun setEvents(events: List<Event>) {
        adapter!!.setEvents(events)
        checkResult(events.size)
    }


    override fun internet(status: Boolean?) {
        if (status!!) {
            binding!!.noInternet!!.noInternetLayout.visibility = View.VISIBLE
        } else {
            binding!!.noInternet!!.noInternetLayout.visibility = View.GONE
        }
    }

    override fun checkResult(count: Int) {
        if (type == "Today") {
            binding!!.noResult!!.resultText.text = "No Events for Today\nSee Upcoming"
        } else {
            binding!!.noResult!!.resultText.text = "No Upcoming Events"
        }
        if (count > 0) {
            binding!!.noResult!!.noResultLayout.visibility = View.GONE
        } else {
            binding!!.noResult!!.noResultLayout.visibility = View.VISIBLE
        }
    }


    override fun onEventClicked(event: Event) {
        val intent = Intent(activity, EventSimpleDetailActivity::class.java)
        intent.putExtra(Constants.ID, event.id)
        intent.putExtra("fromMain", true)
        startActivity(intent)
    }

    override fun showAlert(s: String?) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
        realm!!.close()
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"
        val ARG_TYPE = "ARG_TYPE"
        private val TAG = MainPageFragment::class.java.simpleName

        fun newInstance(page: Int, s: String): MainPageFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            args.putString(ARG_TYPE, s)
            val mainPageFragment = MainPageFragment()
            mainPageFragment.arguments = args
            return mainPageFragment
        }
    }
}
