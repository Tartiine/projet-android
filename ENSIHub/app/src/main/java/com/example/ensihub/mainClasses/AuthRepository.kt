package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository {
    val currentUser:FirebaseUser? = Firebase.auth.currentUser

    fun hasUser():Boolean = Firebase.auth.currentUser != null

    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    fun resetPassword(email: String) = Firebase.auth.sendPasswordResetEmail(email)



    suspend fun createUser(
        username:String,
        email:String,
        password:String,
        onComplete:(Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val userId = Firebase.auth.currentUser?.uid
                    if (userId != null) {
                        val db = Firebase.firestore
                        val user = hashMapOf(
                            "email" to email,
                            "id" to Firebase.auth.currentUser?.uid,
                            "kind" to "user",
                            "username" to username
                        )
                        db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error writing document", e)
                            }
                    }
                    onComplete.invoke(true)
                }else{
                    onComplete.invoke(false)
                }
            }.await()
    }

    suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    val isEmailVerified = user?.isEmailVerified ?: false

                    if (isEmailVerified) {
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(true) //Pour le moment il y pas de verification d'email
                        //donc faut le laisser a true sinon la variable de connection
                        // ne s'actualise pas
                    }
                } else {
                    onComplete.invoke(false)
                }
            }.await()
    }


}