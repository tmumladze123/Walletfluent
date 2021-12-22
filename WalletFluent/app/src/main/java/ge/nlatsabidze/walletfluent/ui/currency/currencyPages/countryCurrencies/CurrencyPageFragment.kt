package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import android.app.ProgressDialog
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.CurrencyPageFragmentBinding
import ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies.currencyAdapter.CurrencyAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyPageFragment :
    BaseFragment<CurrencyPageFragmentBinding>(CurrencyPageFragmentBinding::inflate) {
    private lateinit var pb : ProgressBar
    private val currencyPageViewModel: CurrencyPageViewModel by viewModels()
    private var currencyAdapter = CurrencyAdapter()
    override fun start() {
        ((activity))!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initAdapter()
        setDataFromApi()
        prog()
    }

    private fun prog(){
    }

    private fun initAdapter() {
        binding.rvCurrency.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setDataFromApi() {
        currencyPageViewModel.getCommercialRates()

        viewLifecycleOwner.lifecycleScope.launch {
            currencyPageViewModel.commercialRates.collectLatest {
                currencyAdapter.currencies = it
            }
        }
    }
}