package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference,
) {

    fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!
        database = database.child(firebaseUser.uid)
    }

    fun UserDatabase(): DatabaseReference {
        return database
    }

}