package ge.nlatsabidze.walletfluent.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.adapters.CurrencyViewPager
import ge.nlatsabidze.walletfluent.databinding.FragmentCurrencyBinding
@AndroidEntryPoint
class CurrencyFragment : BaseFragment<FragmentCurrencyBinding>(FragmentCurrencyBinding::inflate) {
    override fun start() {
    createAdapter()
    }
    fun createAdapter(){
        val pager=binding.viewPager
        val tabLayout=binding.tabLayout
        //pager.adapter = galleryAdapter(this)
        pager.adapter = CurrencyViewPager()
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(tabLayout, pager) {tab, position ->
            when (position){
                0 -> tab.text = "კურსები"
                1 -> tab.text = "ისტორია"
                2 -> tab.text = "კალკულატორი"
            }
        }.attach()

    }


}