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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.databinding.CurrencyPageFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies.currencyAdapter.CurrencyAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyPageFragment :
    BaseFragment<CurrencyPageFragmentBinding>(CurrencyPageFragmentBinding::inflate) {

    private val currencyPageViewModel: CurrencyPageViewModel by viewModels()
    private var currencyAdapter = CurrencyAdapter()

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection


    override fun start() {

        initAdapter()
        setDataFromApi()
        displayProgressBar()

        if (!checkInternetConnection.isOnline(requireContext())) {
            showDialogError(
                "In Order to use our application, you should be connected to internet",
                requireContext()
            )
            viewLifecycleOwner.lifecycleScope.launch {
                currencyPageViewModel.currencyValues.collectLatest {
                    if (it.isNotEmpty()) {
                        currencyAdapter.currencies = it
                        binding.spinKit.visibility = View.GONE
                        binding.tvLatest.visibility = View.VISIBLE
                    } else {
                        binding.spinKit.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun displayProgressBar() {

        viewLifecycleOwner.lifecycleScope.launch {
            currencyPageViewModel.showLoadingViewModel.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
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
        currencyPageViewModel.getCommercialRates()
        viewLifecycleOwner.lifecycleScope.launch {
            currencyPageViewModel.commercialRates.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                currencyAdapter.currencies = it
            }
        }
    }
}