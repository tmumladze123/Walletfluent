package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
    var firebaseUser: FirebaseUser,
    var checkInternetConnection: CheckInternetConnection,
    application: Application
) : AndroidViewModel(application) {

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private val _transaction = MutableSharedFlow<UserTransaction>()
    val transaction: MutableSharedFlow<UserTransaction> get() = _transaction

    private val _expireYear = MutableSharedFlow<String>()
    val expireYear: MutableSharedFlow<String> get() = _expireYear

    fun setInformationFromDatabase() {

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").value.toString() + "₾"
                val name = snapshot.child("name").value.toString() + "'s card"


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

    fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        database = database.child(firebaseUser.uid)
    }

    fun addTransaction() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.childrenCount > 0) {
                    for (data in dataSnapshot.children) {
                        val userTransaction: UserTransaction? =
                            data.getValue(UserTransaction::class.java)
                        viewModelScope.launch {
                            if (userTransaction != null) {
                                _transaction.emit(userTransaction)
                            }
                        }
                    }
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}

        }
        database.addChildEventListener(childEventListener)
    }

    fun expireDate() {
        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val expireDate = it.result!!.child("expireDate").value.toString() // 15/12/2022 2022/1/15

                val currentMonth = findSlashOccuriences(expireDate)
                val expireYear = getExpireYear(expireDate)

                val showExpire = currentMonth + expireYear

                viewModelScope.launch {
                    _expireYear.emit(showExpire)
                }
            }
        }
    }

    private fun findSlashOccuriences(date: String): String {
        var startSlash = 0
        for (i in 0..date.length) {
            if (date[i] == '/') {
                startSlash = i
                break
            }
        }

        var endSlash = 0
        for (i in date.length - 1 downTo 0) {
            if (date[i] == '/') {
                endSlash = i
                break
            }
        }

        var currentMonth = ""
        for (i in startSlash.. endSlash) {
            currentMonth += date[i]
        }

        currentMonth = currentMonth.drop(1)
        return currentMonth

    }

    private fun getExpireYear(expireDate: String): String {
        var year = expireDate.reversed().substring(0, 4)
        var yearToInt = year.toInt()
        yearToInt += 5
        return yearToInt.toString()
    }

    fun checkConnection(): Boolean {
        return checkInternetConnection.isOnline(getApplication())
    }

}