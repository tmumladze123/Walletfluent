package ge.nlatsabidze.walletfluent.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

     private var _userMutableLive= MutableStateFlow<Boolean>(false)
     val userMutableLiveFlow :MutableStateFlow<Boolean> get()= _userMutableLive

    private var _dialogError = MutableStateFlow(false)
    val dialogError: MutableStateFlow<Boolean> get() = _dialogError


     fun login(email: String, password: String) {
         appRepository.login(email, password)
         viewModelScope.launch {
             appRepository.userMutableLiveFlow.collectLatest {
                 _userMutableLive.value = it
             }
         }
     }
     fun  changeToFalse(){
             appRepository.changeFlowToFalse()
     }
        /* _userMutableLiveFlow.value=false
         viewModelScope.launch {
             appRepository.dialogError.collectLatest {
                 _dialogError.value=it
             }
         }*/
    }




