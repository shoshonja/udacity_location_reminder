package com.udacity.project4.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.udacity.project4.ACTION_GEOFENCE_EVENT
import com.udacity.project4.GEOFENCE_DURATION_MILLIS
import com.udacity.project4.GEOFENCE_RADIUS_METERS
import com.udacity.project4.LOG_TAG
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

@SuppressLint("MissingPermission")
fun createGeofence(activity: Activity, reminderDataItem: ReminderDataItem) {
    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(
            activity,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    val geofencingClient = LocationServices.getGeofencingClient(activity)

    geofencingClient.removeGeofences(geofencePendingIntent)?.run {
        addOnCompleteListener {
            Log.d(LOG_TAG, "Removing geofence")
            geofencingClient.addGeofences(
                createGeofencingRequest(reminderDataItem),
                geofencePendingIntent
            )?.run {
                addOnSuccessListener {
                    Log.d(LOG_TAG, "Success in adding geofence with ID:${reminderDataItem.id}")
                }
                addOnFailureListener {
                    Log.d(LOG_TAG, "Error in adding geofence with ID:${reminderDataItem.id}")

                }
            }
        }
    }
}

fun createGeofencingRequest(reminderDataItem: ReminderDataItem): GeofencingRequest {
    val geofence = Geofence.Builder().setRequestId(reminderDataItem.id)
        .setCircularRegion(
            reminderDataItem.latitude!!,
            reminderDataItem.longitude!!,
            GEOFENCE_RADIUS_METERS
        ).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .setExpirationDuration(GEOFENCE_DURATION_MILLIS)
        .build()

    return GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
}