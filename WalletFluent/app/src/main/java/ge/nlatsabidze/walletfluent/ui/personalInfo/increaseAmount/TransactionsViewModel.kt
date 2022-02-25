package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
class  TransactionsViewModel @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference
) : ViewModel() {

    private val _setAmount = MutableSharedFlow<Int>()
    val setAmount: MutableSharedFlow<Int> get() = _setAmount

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private val _showDialogError = MutableSharedFlow<String>()
    val showDialogError: MutableSharedFlow<String> get() = _showDialogError


    fun setInformationFromDatabase() {

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").value.toString() + "₾"
                val name = snapshot.child("name").value.toString() +"'s card"

                viewModelScope.launch {
                    _balance.emit(balance)
                }

                viewModelScope.launch {
                    _name.emit(name)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun pushTransaction(amount: String, purpose: String, currentTime: String, checker: Boolean) {
        if (amount.isNotEmpty() && purpose.isNotEmpty()) {
            database.get().addOnCompleteListener {
                if (it.result?.exists() == true) {
                    val currentBalance = it.result!!.child("balance").value.toString()
                    if (amount.toLong() * -1 > currentBalance.toLong() && checker) {
                        viewModelScope.launch {
                            _showDialogError.emit("The amount you have entered exceeds the current balance")
                        }
                    } else if(amount.toLong() > 5000 && !checker) {
                        viewModelScope.launch {
                            _showDialogError.emit("The amount you entered exceeds the norm")
                        }
                    } else {
                        val transaction = UserTransaction(amount, purpose, currentTime)
                        database.child("userTransaction").push().setValue(transaction)
                        var convertedBalance = currentBalance.toLong()
                        convertedBalance += amount.toLong()
                        val updatedBalance = HashMap<String, Any>()
                        updatedBalance.put("balance", convertedBalance)
                        database.updateChildren(updatedBalance)
                    }
                }
            }
        }
    }


    fun initializeFirebase() {
        val firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }
}