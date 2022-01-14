package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(var firebaseAuth: FirebaseAuth,var database: DatabaseReference, var firebaseUser: FirebaseUser ) : ViewModel() {

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private val _transaction = MutableSharedFlow<UserTransaction>()
    val transaction: MutableSharedFlow<UserTransaction> get() = _transaction

    fun setInformationFromDatabase() {

        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {

                val balance = it.result!!.child("balance").value.toString() + "₾"
                val name = it.result!!.child("name").value.toString() +"'s card"


                viewModelScope.launch {
                    _balance.emit(balance)
                }

                viewModelScope.launch {
                    _name.emit(name)
                }
            }
        }
    }

    fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    fun addTransaction() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.childrenCount > 0) {
                    for (data in dataSnapshot.children) {
                        val userTransaction: UserTransaction? = data.getValue(UserTransaction::class.java)
//                        transactionAdapter.userTransactions.add(userTransaction!!)
                        viewModelScope.launch {
                            if (userTransaction != null) {
                                _transaction.emit(userTransaction)
                            }
                        }
                    }
//                    transactionAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}

        }
        database.addChildEventListener(childEventListener)
    }


//    fun changeUserAmount(amount: String, operator: String, database: DatabaseReference) {
//
//        var convertedValueToInt = amount.dropLast(1).toInt()
//
//        if (operator == "+") {
//            convertedValueToInt += 20
//        } else {
//            convertedValueToInt -= 20
//        }
//
//        viewModelScope.launch {
//            _setAmount.emit(convertedValueToInt)
//        }
//
//        val user = HashMap<String, Any>()
//        user.put("balance", convertedValueToInt)
//        database.updateChildren(user).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.d("sdadasdas", convertedValueToInt.toString())
//            }
//        }
//    }
}