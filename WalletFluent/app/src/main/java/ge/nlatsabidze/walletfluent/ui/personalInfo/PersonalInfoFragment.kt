package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser


    override fun start() {
        initializeFirebase()
        activity!!.actionBar?.hide()
        personalInfoViewModel.setInformationFromDatabase(database)

        binding.btnIncrease.setOnClickListener {
            personalInfoViewModel.changeUserAmount(
                binding.balance.text.toString(),
                binding.btnIncrease.text.toString(),
                database
            )
        }

        binding.btnDecrease.setOnClickListener {
            personalInfoViewModel.changeUserAmount(
                binding.balance.text.toString(),
                binding.btnDecrease.text.toString(),
                database
            )
        }

        observes()
    }

    private fun observes() {
        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.setAmount.collect {
                binding.balance.text = it.toString() + "₾"
            }
        }

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