package ge.nlatsabidze.walletfluent.ui.currency

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.adapters.ViewPagerAdapter
import ge.nlatsabidze.walletfluent.databinding.FragmentCurrencyBinding
import ge.nlatsabidze.walletfluent.ui.crypto.CryptoFragment


@AndroidEntryPoint
class CurrencyFragment : BaseFragment<FragmentCurrencyBinding>(FragmentCurrencyBinding::inflate) {

    private val currencyViewModel: CurrencyViewModel by viewModels()

    override fun start() {
//        currencyViewModel.getCommercialRates()
//
//        lifecycleScope.launch {
//            currencyViewModel.commercialRates.collectLatest {
//                d("dasdas", it.toString())
//            }
//        }
        val currencyPages = arrayOf("კურსები", "კალკულატორი")

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = currencyPages[position]
        }.attach()
    }

}