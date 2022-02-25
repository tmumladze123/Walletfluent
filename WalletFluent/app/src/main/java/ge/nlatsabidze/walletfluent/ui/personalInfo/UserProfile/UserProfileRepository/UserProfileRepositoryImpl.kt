package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
): UserProfileRepository {

    private val _balance = MutableStateFlow("")
    val balance: MutableStateFlow<String> get() = _balance

    private val _name = MutableStateFlow("")
    val name: MutableStateFlow<String> get() = _name

    private val _currentDate = MutableStateFlow("")
    val currentData: MutableStateFlow<String> get() = _currentDate

    private val _email = MutableStateFlow("")
    val emailHolder: MutableStateFlow<String> get() = _email

    override fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    override fun setInformationFromDatabase() {
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").value.toString() + "₾"
                val name = snapshot.child("name").value.toString()
                val currentDate = snapshot.child("expireDate").value.toString()
                val email = snapshot.child("email").value.toString()

                _balance.value = balance
                _name.value = name
                _currentDate.value = currentDate
                _email.value = email

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "onCancelled ${error.message}" )
            }

        })
    }


}