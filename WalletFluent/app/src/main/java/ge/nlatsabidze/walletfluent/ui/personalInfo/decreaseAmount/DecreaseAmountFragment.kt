package ge.nlatsabidze.walletfluent.ui.personalInfo.decreaseAmount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.DecreaseAmountFragmentBinding
import ge.nlatsabidze.walletfluent.databinding.IncreaseAmountFragmentBinding

class DecreaseAmountFragment : BottomSheetDialogFragment() {

    private var _binding: DecreaseAmountFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DecreaseAmountFragmentBinding.inflate(inflater, container, false)
        start()
        return binding.root
    }

    private fun start() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}