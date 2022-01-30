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
import ge.nlatsabidze.walletfluent.databinding.FragmentCryptoBinding
import ge.nlatsabidze.walletfluent.ui.crypto.cryptoAdapter.CryptoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CryptoFragment : BaseFragment<FragmentCryptoBinding>(FragmentCryptoBinding::inflate) {

    private val cryptoViewModel: CryptoViewModel by viewModels()
    private lateinit var cryptoAdapter: CryptoAdapter

    override fun start() {

        displayProgressBar()

        cryptoAdapter = CryptoAdapter()

        binding.rvCrypto.adapter = cryptoAdapter
        binding.rvCrypto.layoutManager = LinearLayoutManager(requireContext())

        cryptoViewModel.getCryptoExchangeValues()
        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.marketValues.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                cryptoAdapter.cryptoExchanges = it
            }
        }

        cryptoAdapter.onItemClick = { currentItem ->
            val action = CryptoFragmentDirections.actionCryptoFragmentToDetailCryptoFragment(currentItem)
            findNavController().navigate(action)
        }
    }

    private fun displayProgressBar() {
        cryptoViewModel.showLoadingBar()
        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.showLoadingViewModel.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                val loadingBar = binding.spinKit
                if (it) {
                    loadingBar.visibility = View.VISIBLE
                } else {
                    loadingBar.visibility = View.INVISIBLE
                }
            }
        }
    }
}

