package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository.UserProfileRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
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

    init {
        _balance = userProfileRepository.balance
        _name = userProfileRepository.name
        _currentDate = userProfileRepository.currentData
        _email = userProfileRepository.emailHolder
    }

    fun initializeFirebase() {
        userProfileRepository.initializeFirebase()
    }

    fun setInformationFromDatabase() {
        userProfileRepository.setInformationFromDatabase()
    }


}