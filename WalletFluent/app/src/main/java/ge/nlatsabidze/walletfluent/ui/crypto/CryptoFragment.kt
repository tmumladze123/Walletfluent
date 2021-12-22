package ge.nlatsabidze.walletfluent.ui.crypto

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.FragmentCryptoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CryptoFragment : BaseFragment<FragmentCryptoBinding>(FragmentCryptoBinding::inflate) {

    private val cryptoViewModel: CryptoViewModel by viewModels()

    override fun start() {
        cryptoViewModel.getCryptoExchangeValues()

        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.cryptoExchangedValues.collectLatest {
                d("asdsaaaaadsa", it.toString())
            }
        }
    }
}

