package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.FirebaseUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
    var firebaseUser: FirebaseUser,
    var firebaseRepository: FirebaseUserRepository
) : ViewModel() {

    private val _userName = MutableStateFlow<String>("")
    val userName: MutableStateFlow<String> get() = _userName

    private val _showChangePasswordDialog = MutableSharedFlow<String>()
    val showChangePasswordDialog: MutableSharedFlow<String> get() = _showChangePasswordDialog

    private val _showInternetErrorDialog = MutableSharedFlow<String>()
    val showInternetErrorDialog: MutableSharedFlow<String> get() = _showInternetErrorDialog


    fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    fun getUserName() {
        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val name = it.result!!.child("name").value.toString()
                viewModelScope.launch {
                    _userName.value = name
                }
            }
        }
    }

    fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }


    fun changeUserPassword() {
        database.get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val email = it.result!!.child("email").value.toString()
                firebaseRepository.resetUserPassword(email)
                viewModelScope.launch {
                    _showChangePasswordDialog.emit("Change password")
                }
            }
        }
    }
}