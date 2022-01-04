package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import android.util.Log.d
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.CalculatorPageFragmentBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CalculatorPageFragment :
    BaseFragment<CalculatorPageFragmentBinding>(CalculatorPageFragmentBinding::inflate) {

    private val calculatorViewModel: CalculatorPageViewModel by viewModels()

    override fun start() {
        makeResultEditTextNotClickable()
        setValueToResult()

    }

    override fun onResume() {
        super.onResume()

        val countries = resources.getStringArray(R.array.countries)

        val countriesFrom = ArrayAdapter(requireContext(), R.layout.dropdown_item, countries)
        binding.autoCompleteFrom.setAdapter(countriesFrom)
        binding.autoCompleteTo.setAdapter(countriesFrom)

        var firstValue = binding.textInputLayoutWrapper.editText?.text.toString()
        var secondValue = binding.secondTextInputLayoutWrapper.editText?.text.toString()
        binding.autoCompleteFrom.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = countriesFrom.getItem(position)
                firstValue = selectedValue!!
                if (binding.etNumber.text?.isNotEmpty() == true) {
                    calculatorViewModel.getConvertedValue(
                        binding.etNumber.text.toString().toDouble(), firstValue, secondValue
                    )
                }

            }


        binding.autoCompleteTo.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = countriesFrom.getItem(position)
                secondValue = selectedValue!!
                if (binding.etNumber.text?.isNotEmpty() == true) {
                    calculatorViewModel.getConvertedValue(
                        binding.etNumber.text.toString().toDouble(), firstValue, secondValue
                    )
                }
            }

        binding.etNumber.doAfterTextChanged {
            val amountAsString = binding.etNumber.text.toString()
            if (amountAsString.isNotEmpty() && amountAsString[0] == '0') {
                binding.etConvertedNumber.setText("0");
            } else if (amountAsString.isNotEmpty()) {
                val amount = binding.etNumber.text.toString().toDouble()
                calculatorViewModel.getConvertedValue(amount, firstValue, secondValue)
            } else if (amountAsString.isEmpty() && binding.etConvertedNumber.text.toString()
                    .isNotEmpty()
            ) {
                binding.etConvertedNumber.text?.clear()
            }
        }

    }

    private fun setValueToResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            calculatorViewModel.convertedValue.collectLatest {
                if (binding.etNumber.text?.isNotEmpty() == true) {
                    binding.etConvertedNumber.setText(it.value.toString())
                }
            }
        }
    }

    private fun makeResultEditTextNotClickable() {
        binding.etConvertedNumber.isEnabled = false
    }
}