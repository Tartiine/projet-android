package com.example.ensihub.mainClasses

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    val currentUser:FirebaseUser? = Firebase.auth.currentUser

    /**
     * @brief Vérifie si un utilisateur est connecté.
     *
     * @return Boolean indiquant si un utilisateur est connecté.
     */
    fun hasUser():Boolean = Firebase.auth.currentUser != null
    /**
     * @brief Réinitialise le mot de passe de l'utilisateur en envoyant un e-mail de réinitialisation.
     *
     * @param email Adresse e-mail de l'utilisateur.
     */
    fun resetPassword(email: String) = Firebase.auth.sendPasswordResetEmail(email)


    /**
     * @brief Crée un nouvel utilisateur avec l'adresse e-mail et le mot de passe donnés.
     *
     * @param username Nom d'utilisateur de l'utilisateur.
     * @param email Adresse e-mail de l'utilisateur.
     * @param password Mot de passe de l'utilisateur.
     * @param onComplete Fonction de rappel appelée une fois la création de l'utilisateur terminée.
     * @return Résultat de l'opération de création d'utilisateur.
     */
    suspend fun createUser(
        username:String,
        email:String,
        password:String,
        onComplete:(Boolean) -> Unit
    ): AuthResult = withContext(dispatcher){
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
                            "role" to "USER",
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

    /**
     * @brief Connecte l'utilisateur avec l'adresse e-mail et le mot de passe donnés.
     *
     * @param email Adresse e-mail de l'utilisateur.
     * @param password Mot de passe de l'utilisateur.
     * @param onComplete Fonction de rappel appelée une fois la connexion terminée.
     * @return Résultat de l'opération de connexion.
     */
    suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ): AuthResult = withContext(dispatcher) {
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete.invoke(true)
                } else {
                    onComplete.invoke(false)
                }
            }.await()
    }


}