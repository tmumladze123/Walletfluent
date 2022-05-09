package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
) : UserProfileRepository {

    override fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setInformationFromDatabase(): Flow<Resource<UserState>> = callbackFlow {
        trySend(Resource.Loading())
        val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").value.toString() + "₾"
                val name = snapshot.child("name").value.toString()
                val currentDate = snapshot.child("expireDate").value.toString()
                val email = snapshot.child("email").value.toString()

                val userState = UserState(balance, name, currentDate, email)
                trySend(Resource.Success(userState))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "onCancelled ${error.message}")
            }

        })

        awaitClose {
            database.removeEventListener(listener)
        }
    }


}
