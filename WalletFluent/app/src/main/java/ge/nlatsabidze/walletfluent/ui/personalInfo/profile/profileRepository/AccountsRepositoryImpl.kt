package ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    var firebaseRepository: FirebaseUserRepositoryImpl,
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference
): AccountsRepository {

    private val _userName = MutableStateFlow<String>("")
    val userName: MutableStateFlow<String> get() = _userName

    private val _showChangePasswordDialog = MutableStateFlow<String>("Follow the instructions in the email.")
    val showChangePasswordDialog: MutableStateFlow<String> get() = _showChangePasswordDialog


    override fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    override fun changeUserPassword() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").value.toString()
                firebaseRepository.resetUserPassword(email)


                _showChangePasswordDialog.value = ("Follow the instructions in the email.")


            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun getUserName() {
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.child("name").value.toString()
                _userName.value = userName

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

}