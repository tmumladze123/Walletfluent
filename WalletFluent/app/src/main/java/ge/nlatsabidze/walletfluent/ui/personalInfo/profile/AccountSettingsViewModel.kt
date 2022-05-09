package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository.AccountsRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    var firebaseRepository: FirebaseUserRepositoryImpl,
    var profileRepository: AccountsRepositoryImpl
) : ViewModel() {

    private var _userName = MutableStateFlow("")
    val userName: MutableStateFlow<String> get() = _userName

    private var _showChangePasswordDialog = MutableSharedFlow<String>()
    val showChangePasswordDialog: MutableSharedFlow<String> get() = _showChangePasswordDialog

    private var _loaderFlow = MutableStateFlow(false)
    val loaderFlow: MutableStateFlow<Boolean> get() = _loaderFlow

    private val _state = MutableStateFlow<Resource<String>>(Resource.EmptyData())
    val state: MutableStateFlow<Resource<String>> get() = _state

    fun initializeFirebase() {
        profileRepository.initializeFirebase()
    }

    fun changeUserPassword() {
        viewModelScope.launch {
            profileRepository.changeUserPassword().collect { dialog ->
                _showChangePasswordDialog.emit(dialog.data.toString())
            }
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            profileRepository.getUserName().collect {
                when (it) {
                    is Resource.Loading -> {
                        _loaderFlow.value = true
                    }
                    is Resource.Success -> {
                        _loaderFlow.value = false
                        _userName.value = it.data.toString()
                    }
                }
            }
        }
    }

    fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

    override fun onCleared() {
        super.onCleared()
        d("AAA", "OnCleared Called")
    }

}