package ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class AccountsRepository @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var database: DatabaseReference
) {


    fun initializeFirebase() {
        var firebaseUser = firebaseAuth.currentUser!!

        database =
            FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(firebaseUser.uid)
    }

    fun UserDatabase(): DatabaseReference {
        return database
    }

}