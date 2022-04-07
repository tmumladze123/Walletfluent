package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository.UserProfileRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    var userProfileRepository: UserProfileRepositoryImpl
) : ViewModel() {

    init {
        userProfileRepository.initializeFirebase()
        setInformationFromDatabase()
    }

    private var _balance = MutableStateFlow("")
    val balance: MutableStateFlow<String> get() = _balance

    private var _name = MutableStateFlow("")
    val name: MutableStateFlow<String> get() = _name

    private var _currentDate = MutableStateFlow("")
    val currentData: MutableStateFlow<String> get() = _currentDate

    private var _email = MutableStateFlow("")
    val emailHolder: MutableStateFlow<String> get() = _email

    private fun setInformationFromDatabase() {
        viewModelScope.launch {
            userProfileRepository.setInformationFromDatabase().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> _balance.emit(it1.balance) }
                        it.data?.let { it1 -> _name.emit(it1.name) }
                        it.data?.let { it1 -> _email.emit(it1.email) }
                        it.data?.let { it1 -> _currentDate.emit(it1.currentDate) }
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


}