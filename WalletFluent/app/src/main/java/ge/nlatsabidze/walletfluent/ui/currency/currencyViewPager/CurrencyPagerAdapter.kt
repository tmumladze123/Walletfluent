package ge.nlatsabidze.walletfluent.ui.currency.currencyViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies.CalculatorPageFragment
import ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies.CurrencyPageFragment


class CurrencyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        const val NUM_TABS = 2
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return CurrencyPageFragment()
        }
        return CalculatorPageFragment()
    }
}

