package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.extensions.collect
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserState
import ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository.UserProfileRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    var userProfileRepository: UserProfileRepositoryImpl,
    private val dispatchers: ge.nlatsabidze.walletfluent.util.Dispatchers
) : ViewModel() {

    init {
        userProfileRepository.initializeFirebase()
        setInformationFromDatabase()
    }

    private val _userState = MutableStateFlow(UserState())
    val userState: MutableStateFlow<UserState> get() = _userState

    private fun setInformationFromDatabase() = dispatchers.launchBackground(viewModelScope) {
        collect(userProfileRepository.setInformationFromDatabase()) { userState ->
            when (userState) {
                is Resource.Success -> {
                    _userState.value = userState.data!!
                }
                is Resource.Error -> {
                    Log.d("Error", "REALTIME DB ERROR")
                }
                else -> {
                    Log.d("Exception", "REALTIME DB ERROR")
                }
            }
        }
    }

}


