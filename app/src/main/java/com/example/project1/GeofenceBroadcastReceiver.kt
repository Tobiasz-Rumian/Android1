package com.example.project1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private var context: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        this.context = context
        if (geofencingEvent.hasError()) {
            Log.e("xxx", geofencingEvent.errorCode.toString())
            return
        }
        Log.e("xxx", intent.toString())

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences)
            sendNotification(geofenceTransitionDetails!!)
            Log.i("xxx", geofenceTransitionDetails)
        } else { // Log the error.
            Log.e("xxx", geofenceTransition.toString())
        }
    }

    private fun getGeofenceTransitionDetails(
        geofenceTransition: Int,
        triggeringGeofences: List<Geofence>
    ): String? {
        val geofenceTransitionString = getTransitionString(geofenceTransition)
        val triggeringGeofencesIdsList: ArrayList<String?> = ArrayList()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList)
        return "$geofenceTransitionString: $triggeringGeofencesIdsString"
    }

    private fun sendNotification(notificationDetails: String) { // Get an instance of the Notification manager
        val mNotificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = context!!.getString(R.string.app_name)
        val mChannel = NotificationChannel("channel_01", name, NotificationManager.IMPORTANCE_DEFAULT)
        mNotificationManager.createNotificationChannel(mChannel)
        val notificationIntent = Intent(context, MainActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context!!)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)
        val notificationPendingIntent: PendingIntent? =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context!!.resources, R.drawable.ic_launcher_foreground))
            .setColor(Color.RED)
            .setContentTitle(notificationDetails)
            .setContentText("Miłego dnia!")
            .setContentIntent(notificationPendingIntent)
        builder.setChannelId("channel_01") // Channel ID
        builder.setAutoCancel(true)
        mNotificationManager.notify(0, builder.build())
    }

    private fun getTransitionString(transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "Jesteś w: "
            Geofence.GEOFENCE_TRANSITION_EXIT -> "Opuściłeś: "
            else -> "Błąd"
        }
    }
}
