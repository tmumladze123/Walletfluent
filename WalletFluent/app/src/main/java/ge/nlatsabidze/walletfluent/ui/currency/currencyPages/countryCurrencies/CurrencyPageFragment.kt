package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.Log.d
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.CurrencyPageFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies.currencyAdapter.CurrencyAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyPageFragment : BaseFragment<CurrencyPageFragmentBinding>(CurrencyPageFragmentBinding::inflate) {

    private val currencyPageViewModel: CurrencyPageViewModel by viewModels()
    private var currencyAdapter = CurrencyAdapter()

    @Inject lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {

        initAdapter()
        setDataFromApi()
        displayProgressBar()

        if (checkInternetConnection.isOnline(activity!!.application).toString() == "false") {
            showDialogError(
                "In Order to use our application, you should be connected to internet",
                requireContext()
            )
        }

    }

    private fun displayProgressBar() {
        
        viewLifecycleOwner.lifecycleScope.launch {
            currencyPageViewModel.showLoadingViewModel.collectLatest {
                val bar = binding.spinKit
                if (it) {
                    bar.visibility = View.VISIBLE
                } else {
                    bar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        binding.rvCurrency.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setDataFromApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            currencyPageViewModel.getCountryCurrencies().collect {
                if (it.data?.commercialRatesList != null) {
                    currencyAdapter.currencies = it.data.commercialRatesList
                }
            }
        }
    }
}