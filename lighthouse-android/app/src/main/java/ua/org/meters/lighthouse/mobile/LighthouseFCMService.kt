package ua.org.meters.lighthouse.mobile

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.edit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking

/**
 * Receives messages from Firebase Cloud.
 */
class LighthouseFCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            sendNotification(message);
        }

        if (message.data.containsKey("power")) {
            runBlocking {
                saveLastState(message.data["power"].toBoolean())
            }
        }
    }

    /**
     * Creates a notification.
     * @param message   FCM notification message
     */
    private fun sendNotification(message: RemoteMessage) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if (message.data.containsKey("power")) {
            intent.putExtra("power", message.data["power"].toBoolean())
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.fcm_message))
            .setSmallIcon(R.drawable.lighthouse)
            .setContentText(message.notification?.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Мій файний дім",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun sendIntentToApp(powerOn: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("power", powerOn)
        sendBroadcast(intent)
    }

    suspend private fun saveLastState(powerOn: Boolean) {
        baseContext.dataStore.edit { preferences -> preferences[POWER_KEY] = powerOn }
    }

    companion object {
        private const val TAG = "LighthouseFCMService"
    }
}
