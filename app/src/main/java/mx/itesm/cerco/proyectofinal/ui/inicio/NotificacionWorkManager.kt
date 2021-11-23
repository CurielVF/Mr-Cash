package mx.itesm.cerco.proyectofinal.ui.inicio

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import androidx.work.ListenableWorker.Result.success
import mx.itesm.cerco.proyectofinal.LLAVE_NOTIFICACION
import mx.itesm.cerco.proyectofinal.MainActivity
import mx.itesm.cerco.proyectofinal.PREFS_NOTIFICACION
import mx.itesm.cerco.proyectofinal.R


class NotificacionWorkManager (val context: Context, params: WorkerParameters) : Worker(context, params){

    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
        sendNotification(id)

        return success()
    }

    private fun sendNotification(id: Int) {

        val preferencias = context.getSharedPreferences(PREFS_NOTIFICACION, AppCompatActivity.MODE_PRIVATE)
        var numeroNotificaciones= preferencias.getInt(LLAVE_NOTIFICACION,0)
        numeroNotificaciones++
        preferencias.edit().apply(){
            putInt(LLAVE_NOTIFICACION,numeroNotificaciones)
            commit()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_notifications_black_24dp)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val pendingIntent = getActivity(applicationContext, numeroNotificaciones, intent, 0)
        val notification = Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_schedule_white)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }}