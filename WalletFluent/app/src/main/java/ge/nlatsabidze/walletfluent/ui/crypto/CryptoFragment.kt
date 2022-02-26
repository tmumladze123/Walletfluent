package ge.nlatsabidze.walletfluent.ui.crypto

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckLiveConnection
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.FragmentCryptoBinding
import ge.nlatsabidze.walletfluent.extensions.showDialogError
import ge.nlatsabidze.walletfluent.ui.crypto.cryptoAdapter.CryptoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CryptoFragment : BaseFragment<FragmentCryptoBinding>(FragmentCryptoBinding::inflate) {

    private val cryptoViewModel: CryptoViewModel by viewModels()
    private lateinit var cryptoAdapter: CryptoAdapter

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun start() {
        initRecycler()
        displayProgressBar()
        navigateToDetails()

        getCryptoValuesFromLocalDatabase()
    }

    override fun observes() {
        observeCryptoValues()
    }

    private fun displayProgressBar() {

        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.showLoadingViewModel.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collectLatest {
                val loadingBar = binding.spinKit
                if (it) {
                    loadingBar.visibility = View.VISIBLE
                } else {
                    loadingBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initRecycler() {
        cryptoAdapter = CryptoAdapter()
        binding.rvCrypto.adapter = cryptoAdapter
        binding.rvCrypto.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun navigateToDetails() {
        cryptoAdapter.onItemClick = { currentItem ->
            val action =
                CryptoFragmentDirections.actionCryptoFragmentToDetailCryptoFragment(currentItem)
            findNavController().navigate(action)
        }
    }

    private fun observeCryptoValues() {
        cryptoViewModel.getCryptoExchangeValues()
        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.marketValues.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collectLatest {
                if (it.isNotEmpty()) {
                    binding.spinKit.visibility = View.GONE
                }
                cryptoAdapter.cryptoExchanges = it
                binding.rvCrypto.startLayoutAnimation()
            }
        }
    }

    private fun getCryptoValuesFromLocalDatabase() {
        if (!checkInternetConnection.isOnline(requireContext())) {
            showDialogError(
                resources.getString(R.string.NoInternetConnection),
                requireContext()
            )
            viewLifecycleOwner.lifecycleScope.launch {
                cryptoViewModel.getCryptoValues.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collectLatest {
                    if (it.isNotEmpty()) {
                        cryptoAdapter.cryptoExchanges = it
                        binding.rvCrypto.startLayoutAnimation()
                        binding.spinKit.visibility = View.GONE
                    } else {
                        binding.spinKit.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

