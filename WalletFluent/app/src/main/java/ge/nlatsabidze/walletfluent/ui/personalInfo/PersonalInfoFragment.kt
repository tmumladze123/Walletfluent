package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    @Inject lateinit var firebaseAuth: FirebaseAuth
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var firebaseUser: FirebaseUser


    override fun start() {

        initializeFirebase()
        personalInfoViewModel.setInformationFromDatabase(database)

        binding.btnIncrease.setOnClickListener {
//            personalInfoViewModel.changeUserAmount(
//                binding.balance.text.toString(),
//                binding.btnIncrease.text.toString(),
//                database
//            )
            val actionToIncrease = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment()
            findNavController().navigate(actionToIncrease)
        }

        binding.btnDecrease.setOnClickListener {
//            personalInfoViewModel.changeUserAmount(
//                binding.balance.text.toString(),
//                binding.btnDecrease.text.toString(),
//                database
//            )

            val actionToDecrease = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToDecreaseAmountFragment()
            findNavController().navigate(actionToDecrease)
        }

        observes()
    }

    private fun observes() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            personalInfoViewModel.setAmount.collect {
//                binding.balance.text = it.toString() + "₾"
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.balance.collect {
                binding.balance.text = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.name.collect {
                binding.tvName.text = it
            }
        }
    }

    private fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }
}