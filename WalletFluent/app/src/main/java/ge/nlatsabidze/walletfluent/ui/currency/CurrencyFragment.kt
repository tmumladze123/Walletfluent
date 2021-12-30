package ge.nlatsabidze.walletfluent.ui.currency

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.adapters.CurrencyPagerAdapter
import ge.nlatsabidze.walletfluent.databinding.FragmentCurrencyBinding


@AndroidEntryPoint
class CurrencyFragment : BaseFragment<FragmentCurrencyBinding>(FragmentCurrencyBinding::inflate) {

    override fun start() {
        val currencyPages = arrayOf("Currencies", "Calculator")

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = CurrencyPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = currencyPages[position]
        }.attach()
    }

}