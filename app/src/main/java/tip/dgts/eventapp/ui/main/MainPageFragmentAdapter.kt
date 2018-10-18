package tip.dgts.eventapp.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import tip.dgts.eventapp.ui.main.tabs.MainPageFragment

/**
 * Created by Mark Jansen Calderon on 2/8/2017.
 */

class MainPageFragmentAdapter(fm: FragmentManager, private val strings: List<String>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return strings.size
    }

    override fun getItem(position: Int): Fragment {
        return MainPageFragment.newInstance(position + 1, strings[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        return strings[position]
    }
}