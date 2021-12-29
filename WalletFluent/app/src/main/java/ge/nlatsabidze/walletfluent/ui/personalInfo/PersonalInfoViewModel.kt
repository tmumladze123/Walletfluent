package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PersonalInfoViewModel : ViewModel() {

    private val _setAmount = MutableSharedFlow<Int>()
    val setAmount: MutableSharedFlow<Int> get() = _setAmount

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name


    fun setInformationFromDatabase(database: DatabaseReference) {

        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {

                val balance = it.result!!.child("balance").value.toString() + "₾"
                val name = it.result!!.child("name").value.toString()


                viewModelScope.launch {
                    _balance.emit(balance)
                }

                viewModelScope.launch {
                    _name.emit(name)
                }
            }
        }
    }

    fun changeUserAmount(amount: String, operator: String, database: DatabaseReference) {

        var convertedValueToInt = amount.dropLast(1).toInt()

        if (operator == "+") {
            convertedValueToInt += 20
        } else {
            convertedValueToInt -= 20
        }

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
}