package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.databinding.PersonalInfoFragmentBinding
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


const val TAG = "TAGD"

@AndroidEntryPoint
class PersonalInfoFragment :
    BaseFragment<PersonalInfoFragmentBinding>(PersonalInfoFragmentBinding::inflate) {

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

//    @Inject lateinit var firebaseAuth: FirebaseAuth
//    @Inject lateinit var database: DatabaseReference
//    @Inject lateinit var firebaseUser: FirebaseUser

    private lateinit var transactionAdapter: TransactionsAdapter


    override fun start() {

        initializeRecyclerView()

        personalInfoViewModel.initializeFirebase()
        personalInfoViewModel.setInformationFromDatabase()
        personalInfoViewModel.addTransaction()

//        val childEventListener = object : ChildEventListener {
//            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                if (dataSnapshot.childrenCount > 0) {
//                    for (data in dataSnapshot.children) {
//                        val userTransaction: UserTransaction? = data.getValue(UserTransaction::class.java)
//                        transactionAdapter.userTransactions.add(userTransaction!!)
//                    }
//                    transactionAdapter.notifyDataSetChanged()
//                }
//            }
//
//            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
//
//            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//
//        }
//        database.addChildEventListener(childEventListener)


        binding.btnIncrease.setOnClickListener {
            val actionToIncrease = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToIncreaseAmountFragment()
            findNavController().navigate(actionToIncrease)
        }

        binding.btnDecrease.setOnClickListener {
            val actionToDecrease = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToDecreaseAmountFragment()
            findNavController().navigate(actionToDecrease)
        }

        observes()
    }

    private fun observes() {

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
        viewLifecycleOwner.lifecycleScope.launch {
            personalInfoViewModel.transaction.collect {
                transactionAdapter.userTransactions.add(it)
                transactionAdapter.notifyDataSetChanged()
            }
        }
    }

//    private fun initializeFirebase() {
//        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseUser = firebaseAuth.currentUser!!
//
//        database =
//            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
//                .getReference("Users").child(firebaseUser.uid)
//    }

    private fun initializeRecyclerView() {
        transactionAdapter = TransactionsAdapter()
        binding.rvItems.adapter = transactionAdapter
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
    }
}