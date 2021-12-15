package ge.nlatsabidze.walletfluent.ui.loginAndRegister

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseRepository @Inject constructor() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val showDialogError: MutableLiveData<Boolean> = MutableLiveData()

    fun register(email: String?, password: String?) {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userMutableLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        showDialogError.postValue(true)
                    }
                }
        }
    }

    fun login(email: String?, password: String?) {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user!!.isEmailVerified) {
                            userMutableLiveData.postValue(firebaseAuth.currentUser)
                        } else {
                            user.sendEmailVerification()
                            showDialogError.postValue(true)
                        }
                    } else {
                        showDialogError.postValue(true)
                    }
                }
        }
    }


    fun getUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return userMutableLiveData
    }

    fun getDialogError(): MutableLiveData<Boolean> {
        return showDialogError
    }

}