package app.horecafy.com.activities.wholesalers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import app.horecafy.com.R
import app.horecafy.com.activities.wholesalers.fragments.WholesalerNotificationsFragment
import app.horecafy.com.activities.wholesalers.fragments.WholesalerSubmitProposalsFragment
import kotlinx.android.synthetic.main.activity_wholesaler_commercial_visits.*

class WholesalerCommercialVisitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_commercial_visits)

        setupViewPager(vpWholesalerCommercialVisits)
        tlWholesalerCommercialVisits.setupWithViewPager(vpWholesalerCommercialVisits)
    }

    private fun setupViewPager(pager: ViewPager?) {
        val adapter = Adapter(supportFragmentManager)

        val f1 = WholesalerSubmitProposalsFragment.newInstance()
        adapter.addFragment(f1, "Presentar propuestas")

        val f2 = WholesalerNotificationsFragment.newInstance()
        adapter.addFragment(f2, "Notificaciones")

        pager?.adapter = adapter
    }

    private class Adapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        override fun getItem(position: Int): Fragment = fragments.get(position)

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = titles.get(position)

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }
}
