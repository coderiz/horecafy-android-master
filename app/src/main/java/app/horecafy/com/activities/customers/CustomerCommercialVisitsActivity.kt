package app.horecafy.com.activities.customers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import app.horecafy.com.R
import app.horecafy.com.activities.customers.fragments.CustomerAddAvailabilityFragment
import app.horecafy.com.activities.customers.fragments.CustomerNotificationsFragment
import kotlinx.android.synthetic.main.activity_customer_commercial_visits.*

class CustomerCommercialVisitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_commercial_visits)

        setupViewPager(vpCustomerCommercialVisits)
        tlCustomerCommercialVisits.setupWithViewPager(vpCustomerCommercialVisits)
    }

    fun setupViewPager(pager: ViewPager?) {
        val adapter = Adapter(supportFragmentManager)

        val f1 = CustomerAddAvailabilityFragment.newInstance()
        adapter.addFragment(f1, "Agregar disponibilidad")

        val f2 = CustomerNotificationsFragment.newInstance()
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
