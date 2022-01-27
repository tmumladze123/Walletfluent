package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction
import ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository.UserProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var firebaseUser: FirebaseUser,
    private val internetConnection: CheckInternetConnection,
    application: Application,
    var userProfileRepository: UserProfileRepository
) : AndroidViewModel(application) {

    private val _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private val _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private val _currentDate = MutableSharedFlow<String>()
    val currentData: MutableSharedFlow<String> get() = _currentDate

    private val _email = MutableSharedFlow<String>()
    val emailHolder: MutableSharedFlow<String> get() = _email

    fun initializeFirebase() {
        userProfileRepository.initializeFirebase()
    }


    fun setInformationFromDatabase() {
        userProfileRepository.UserDatabase().get().addOnCompleteListener {
            if (it.result?.exists() == true) {

                val balance = it.result!!.child("balance").value.toString() + "₾"
                val name = it.result!!.child("name").value.toString()
                val currentDate = it.result!!.child("expireDate").value.toString()
                val email = it.result!!.child("email").value.toString()

                viewModelScope.launch {
                    _balance.emit(balance)
                }

                viewModelScope.launch {
                    _name.emit(name)
                }

                viewModelScope.launch {
                    _currentDate.emit(currentDate)
                }

                viewModelScope.launch {
                    _email.emit(email)
                }
            }
        }
    }

    fun checkConnection(): Boolean {
        return internetConnection.isOnline(getApplication())
    }


}