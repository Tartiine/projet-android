package com.example.ensihub.mainClasses


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ensihub.MainActivity
import com.example.ensihub.R
import com.google.firebase.firestore.FirebaseFirestore


object NotificationUtils {
    private const val CHANNEL_ID = "com.example.ensihub.newpostnotifications"

    private var NOTIFICATION_ID = 0

    @RequiresApi(Build.VERSION_CODES.O)
    val channel = NotificationChannel(
        CHANNEL_ID,
        "New Post Notifications",
        NotificationManager.IMPORTANCE_HIGH
    )

    fun checkForNewPostInDatabase(context: Context) {
        val db = FirebaseFirestore.getInstance()
        val lastPostTimestamp = getLastPostTimestamp(context.applicationContext)


        db.collection("posts")
            .whereGreaterThan("timestamp", lastPostTimestamp)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshots != null && !snapshots.isEmpty) {
                    for (document in snapshots) {
                        val post = document.toObject(Post::class.java)
                        if (post.timestamp > lastPostTimestamp) {
                            NOTIFICATION_ID= System.currentTimeMillis().toInt()
                            Log.d(TAG, "New post detected: $post")
                            sendNotification(context, "New post available!")
                            updateLastPostTimestamp(context, post.timestamp)
                        }
                    }
                } else {
                    Log.d(TAG, "No new post detected")
                }
            }
    }



    private fun sendNotification(context: Context, message: String) {
        Log.d(TAG, "sendNotification called with message: $message")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            CHANNEL_ID
        } else {
            ""
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent? = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("New Post")
            .setContentText(message)
            .setSmallIcon(R.drawable.ensihub)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        NOTIFICATION_ID++
        Log.d(TAG, "Notification sent with ID: ${NOTIFICATION_ID - 1}")
    }

    fun getLastPostTimestamp(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("NotificationUtils", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("lastPostTimestamp", 0)
    }

    fun updateLastPostTimestamp(context: Context, newTimestamp: Long) {
        val sharedPreferences = context.getSharedPreferences("NotificationUtils", Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putLong("lastPostTimestamp", newTimestamp)
            commit()
        }
    }

}
