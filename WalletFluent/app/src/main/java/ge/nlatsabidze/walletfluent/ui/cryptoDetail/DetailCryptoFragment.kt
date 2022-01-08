package ge.nlatsabidze.walletfluent.ui.cryptoDetail

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.DetailCryptoFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCryptoFragment : BaseFragment<DetailCryptoFragmentBinding>(DetailCryptoFragmentBinding::inflate) {

    private val args: DetailCryptoFragmentArgs by navArgs()
    private val cryptoViewModel: DetailCryptoViewModel by viewModels()

    override fun start() {
        cryptoViewModel.getChartValues()
        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.chartValues.collect {
                d("sdadasdas", it.toString())
            }
        }
    }
}