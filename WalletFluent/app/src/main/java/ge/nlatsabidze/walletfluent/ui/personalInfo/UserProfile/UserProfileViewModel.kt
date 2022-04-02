package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository.UserProfileRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    var userProfileRepository: UserProfileRepositoryImpl
) : ViewModel() {

    private var _balance = MutableSharedFlow<String>()
    val balance: MutableSharedFlow<String> get() = _balance

    private var _name = MutableSharedFlow<String>()
    val name: MutableSharedFlow<String> get() = _name

    private var _currentDate = MutableSharedFlow<String>()
    val currentData: MutableSharedFlow<String> get() = _currentDate

    private var _email = MutableSharedFlow<String>()
    val emailHolder: MutableSharedFlow<String> get() = _email

    private val _loaderFlow = MutableSharedFlow<Boolean>()
    val loaderFlow: MutableSharedFlow<Boolean> get() = _loaderFlow

    fun initializeFirebase() {
        userProfileRepository.initializeFirebase()
    }

    fun setInformationFromDatabase() {
        viewModelScope.launch {
            userProfileRepository.setInformationFromDatabase().collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _loaderFlow.emit(true)
                    }

                    is Resource.Success -> {
                        _loaderFlow.emit(false)
                        it.data?.let { it1 -> _balance.emit(it1.balance) }
                        it.data?.let { it1 -> _name.emit(it1.name) }
                        it.data?.let { it1 -> _email.emit(it1.email) }
                        it.data?.let { it1 -> _currentDate.emit(it1.currentDate) }
                    }
                }
            }
        }
    }


}