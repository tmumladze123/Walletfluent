package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.IncreaseAmountFragmentBinding
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class IncreaseAmountFragment : BottomSheetDialogFragment() {

    private var _binding: IncreaseAmountFragmentBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var database: DatabaseReference
    @Inject
    lateinit var firebaseUser: FirebaseUser

    private val increaseAmountViewModel: IncreaseAmountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IncreaseAmountFragmentBinding.inflate(inflater, container, false)
        start()
        observes()
        return binding.root
    }

    private fun start() {
        initializeFirebase()
        binding.btnIncreaseAmount.setOnClickListener {
            if (binding.etEnterAmount.text!!.isNotEmpty() && binding.etPurpose.text!!.isNotEmpty()) {
                val amount = binding.etEnterAmount.text.toString().toLong()
                val purpose = binding.etPurpose.text.toString()
                val transaction = UserTransaction(amount, purpose)
                database.child("userTransaction").push().setValue(transaction)
            }

            val action = IncreaseAmountFragmentDirections.actionIncreaseAmountFragmentToPersonalInfoFragment()
            findNavController().navigate(action)
        }

        increaseAmountViewModel.setInformationFromDatabase(database)
    }

    private fun observes() {
        viewLifecycleOwner.lifecycleScope.launch {
            increaseAmountViewModel.balance.collect {
                binding.tvCurrentBalance.text = it
            }
        }
    }


    private fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        database = FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.uid)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}