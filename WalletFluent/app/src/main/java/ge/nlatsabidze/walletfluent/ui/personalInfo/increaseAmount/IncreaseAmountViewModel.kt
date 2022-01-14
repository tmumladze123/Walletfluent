package ge.nlatsabidze.walletfluent.ui.personalInfo.increaseAmount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncreaseAmountViewModel @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
    var firebaseUser: FirebaseUser
) : ViewModel() {

    private val _setAmount = MutableSharedFlow<Int>()
    val setAmount: MutableSharedFlow<Int> get() = _setAmount

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private val _showDialogError = MutableSharedFlow<Boolean>()
    val showDialogError: MutableSharedFlow<Boolean> get() = _showDialogError


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

    fun changeUserAmount(amount: String, database: DatabaseReference) {

        var convertedValueToInt = amount.dropLast(1).toInt()

        viewModelScope.launch {
            _setAmount.emit(convertedValueToInt)
        }

        val user = HashMap<String, Any>()
        user.put("balance", convertedValueToInt)
        database.updateChildren(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("sdadasdas", convertedValueToInt.toString())
            }
        }
    }

    fun pushTransaction(amount: Long, purpose: String) {
        if (amount.toString().isNotEmpty() && purpose.isNotEmpty()) {
            database.get().addOnCompleteListener {
                if (it.result?.exists() == true) {
                    val currentBalance = it.result!!.child("balance").value.toString()
                    if (amount > currentBalance.toLong()) {
                        viewModelScope.launch {
                            _showDialogError.emit(true)
                        }
                    } else {
                        val transaction = UserTransaction(amount, purpose)
                        database.child("userTransaction").push().setValue(transaction)
                    }
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
}