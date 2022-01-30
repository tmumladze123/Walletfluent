package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepository
import ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository.AccountsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    var firebaseRepository: FirebaseUserRepository,
    var profileRepository: AccountsRepository,
    application: Application,
    var checkInternetConnection: CheckInternetConnection
) : AndroidViewModel(application) {

    private val _userName = MutableStateFlow<String>("")
    val userName: MutableStateFlow<String> get() = _userName

    private val _showChangePasswordDialog = MutableSharedFlow<String>()
    val showChangePasswordDialog: MutableSharedFlow<String> get() = _showChangePasswordDialog

    fun initializeFirebase() {
        profileRepository.initializeFirebase()
    }


    fun changeUserPassword() {
        profileRepository.UserDatabase().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").value.toString()
                firebaseRepository.resetUserPassword(email)
                viewModelScope.launch {
                    _showChangePasswordDialog.emit("Change password")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserName() {
        profileRepository.UserDatabase().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.child("name").value.toString()
                viewModelScope.launch {
                    _userName.value = userName
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun checkConnection(): Boolean {
        return checkInternetConnection.isOnline(getApplication())
    }

    fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

}