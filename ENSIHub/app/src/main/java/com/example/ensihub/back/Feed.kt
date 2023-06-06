package com.example.ensihub.back

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Feed {
    private val data = mutableListOf<String>()
    private val db = Firebase.firestore
    private val i = 10

    init {
        db.collection("posts").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
         }

    fun load(number: Int) {
    }

    fun getData(): MutableList<String> {
        return this.data
    }

    fun search(key: String): List<String> {
        return this.data.filter { p ->  p.user.username == key || p.text?.contains(key) == true }
    }

}