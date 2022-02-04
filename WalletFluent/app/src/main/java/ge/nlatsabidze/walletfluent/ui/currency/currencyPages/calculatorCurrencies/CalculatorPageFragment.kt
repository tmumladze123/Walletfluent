package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.calculatorCurrencies

import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.CalculatorPageFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CalculatorPageFragment :
    BaseFragment<CalculatorPageFragmentBinding>(CalculatorPageFragmentBinding::inflate) {

    private val calculatorViewModel: CalculatorPageViewModel by viewModels()

    override fun start() {
        makeResultEditTextNotClickable()
        convertValues()
        setValueToResult()

    }

    private fun convertValues() {

        var firstValue = binding.autoCompleteFrom.selectedItem.toString()
        var secondValue = binding.autoCompleteTo.selectedItem.toString()

        binding.autoCompleteFrom.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val selectedValue = parent?.getItemAtPosition(position).toString()
                    firstValue = selectedValue
                    if (binding.etNumber.text?.isNotEmpty() == true) {
                        calculatorViewModel.getConvertedValue(
                            binding.etNumber.text.toString().toDouble(), firstValue, secondValue
                        )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }

        binding.autoCompleteTo.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val selectedValue = parent?.getItemAtPosition(position).toString()
                    secondValue = selectedValue

                    if (binding.etNumber.text?.isNotEmpty() == true) {
                        calculatorViewModel.getConvertedValue(
                            binding.etNumber.text.toString().toDouble(), firstValue, secondValue
                        )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.btnSwap.setOnClickListener {
            val firstSpinnerIndex = binding.autoCompleteFrom.getSelectedItemPosition()
            binding.autoCompleteFrom.setSelection(binding.autoCompleteTo.getSelectedItemPosition())
            binding.autoCompleteTo.setSelection(firstSpinnerIndex)
        }

        binding.etNumber.doAfterTextChanged {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
                val amountAsString = binding.etNumber.text.toString()
                if (amountAsString.isNotEmpty() && amountAsString[0] == '0' && calculatorViewModel.containsError(amountAsString)) {
                    binding.etConvertedNumber.setText("0")
                } else if (amountAsString.isNotEmpty() && !calculatorViewModel.containsError(amountAsString)) {
                    val amount = binding.etNumber.text.toString().toDouble()
                    calculatorViewModel.getConvertedValue(amount, firstValue, secondValue)
                } else if (amountAsString.isEmpty() && binding.etConvertedNumber.text.toString()
                        .isNotEmpty()
                ) {
                    binding.etConvertedNumber.text?.clear()
                }
            }
        }
    }

    private fun setValueToResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            calculatorViewModel.convertedValue.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                var convertedValue = it.value.toString()
                convertedValue = convertedValue.dropLast(2)
                if (binding.etNumber.text?.isNotEmpty() == true && binding.autoCompleteFrom.selectedItem.toString() == binding.autoCompleteTo.selectedItem.toString()) {
                    binding.etConvertedNumber.setText(it.value.toString())
                } else if (binding.etNumber.text?.isNotEmpty() == true && binding.etNumber.text.toString() != convertedValue) {
                    binding.etConvertedNumber.setText(it.value.toString())
                }
            }
        }
    }

    private fun makeResultEditTextNotClickable() {
        binding.etConvertedNumber.isEnabled = false
    }
}