package ge.nlatsabidze.walletfluent.ui.login

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.AppRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {


    private var userMutableLiveData: MutableLiveData<Boolean>? = null
    private var showDialogError: MutableLiveData<Boolean>? = null
    public var userMutableLiveFlow = MutableStateFlow<Boolean>(false);

 /*   fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val login = async {
                    appRepository.login(email, password)
                }
                val setValue = async {
                    userMutableLiveFlow.value = appRepository.getFlow().value
                }
                login.start()
                login.await()

                while(login.isCompleted){
                    setValue.start()
                }
                *//*appRepository.login(email, password)
                userMutableLiveFlow.value = appRepository.getFlow().value*//*
            }
        }
    }

*/
    /*fun getMutableLiveData(): MutableLiveData<Boolean>? {
       *//*  userMutableLiveData = appRepository.getUserMutableLiveData()
        d("value ", userMutableLiveData!!.value.toString() )*//*
        return userMutableLiveData
    }

    fun showDialogError(): MutableLiveData<Boolean>? {
        showDialogError = appRepository.getTaskDialogError()
        return showDialogError
    }*/
}
