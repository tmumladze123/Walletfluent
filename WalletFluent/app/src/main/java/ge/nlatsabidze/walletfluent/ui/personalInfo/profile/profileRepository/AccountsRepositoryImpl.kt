package ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    var firebaseRepository: FirebaseUserRepositoryImpl,
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference
) : AccountsRepository {

    override fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun changeUserPassword(): Flow<Resource<String>> = callbackFlow {
        val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").value.toString()
                firebaseRepository.resetUserPassword(email)

                trySend(Resource.Success("Follow the instructions in the email."))
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        awaitClose {
            database.removeEventListener(listener)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserName(): Flow<Resource<String>> = callbackFlow {
        val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.child("name").value.toString()
                trySend(Resource.Success(userName))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error("Exception"))
            }
        })

        awaitClose {
            database.removeEventListener(listener)
        }
    }

    override fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

}