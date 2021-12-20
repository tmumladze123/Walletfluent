package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import android.util.Log.d
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.CalculatorPageFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculatorPageFragment : BaseFragment<CalculatorPageFragmentBinding>(CalculatorPageFragmentBinding::inflate) {

    private val calculatorViewModel: CalculatorPageViewModel by viewModels()

    override fun start() {
        ((activity))!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        calculatorViewModel.getConvertedValue(45.1, "USD", "eur")


        lifecycleScope.launch {
            calculatorViewModel.convertedValue.collectLatest {
                d("dsadas", it.value.toString())
            }
        }
    }
}