package mx.itesm.cerco.proyectofinal.ui.inicio

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.FLAG_MUTABLE
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
import androidx.core.app.NotificationCompat.*
import androidx.work.ListenableWorker.Result.success
import mx.itesm.cerco.proyectofinal.LLAVE_NOTIFICACION
import mx.itesm.cerco.proyectofinal.MainActivity
import mx.itesm.cerco.proyectofinal.PREFS_NOTIFICACION
import mx.itesm.cerco.proyectofinal.R
import java.util.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class NotificacionWorkManager (val context: Context, params: WorkerParameters) : Worker(context, params){

    lateinit var subtitleNot: String
    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
        val nombre = inputData.getString("Nombre").toString()
        val monto = inputData.getString("Monto").toString()
        val frecuencia = inputData.getString("Frecuencia").toString()
        val llave = inputData.getString("Llave").toString()
        sendNotification(id,nombre,monto,frecuencia,llave)

        return success()
    }
    private fun setSubtitle(subtitle: String) {
        subtitleNot = subtitle
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendNotification(id: Int, nombre: String, monto: String, Frecuencia: String,Llave: String) {

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

        val bitmap = applicationContext.vectorToBitmap(R.drawable.logo_mr_cash_cuadro)
        val titleNotification = "Pago \""+nombre+"\""
        val subtitleNotification = "¡Tienes un pago de $" +monto+" que hacer!"
        val pendingIntent = getActivity(applicationContext, numeroNotificaciones, intent, FLAG_MUTABLE)
        val notification = Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_outline_cash)
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
        val idNot = getIntID()
        println("Idnot: "+idNot.toString())


        notificationManager.notify(idNot, notification.build())
        if (Frecuencia == "Mensual"){
            userInterface(nombre,monto,Llave)

        }

    }
    private fun scheduleNotification(delay: Long, data: Data, customCalendar: Calendar,llave: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val notificationWork = OneTimeWorkRequest.Builder(NotificacionWorkManager::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data)
            .addTag(uid!!)
            .build()
        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.beginUniqueWork(
            notificationWork.id.toString(),
            androidx.work.ExistingWorkPolicy.APPEND, notificationWork
        ).enqueue()
        val uuid = notificationWork.id.toString()
        val database = FirebaseDatabase.getInstance()
        val urlRecordatorios = uid+"/Recordatorios/"+llave
        println("url: "+urlRecordatorios)
        var myRef =database.getReference(urlRecordatorios+"/uuidRecordatorio")
        myRef.setValue(uuid)
        val format = SimpleDateFormat("yyyy-MM-dd")
        val formatHour = SimpleDateFormat("HH:mm")
        val strDate = format.format(customCalendar.time)
        val hora:String = formatHour.format(customCalendar.time)
        val myRefFecha =database.getReference(urlRecordatorios+"/fechaPago")
        myRefFecha.setValue(strDate)
        val myRefHora =database.getReference(urlRecordatorios+"/hora")
        myRefHora.setValue(hora)

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun userInterface(nombre:String,monto: String,llave: String) {


        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            customCalendar.get(Calendar.YEAR),
            customCalendar.get(Calendar.MONTH),
            customCalendar.get(Calendar.DAY_OF_MONTH),
            customCalendar.get(Calendar.HOUR_OF_DAY),
            customCalendar.get(Calendar.MINUTE))
        customCalendar.add(Calendar.MONTH,1)

        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()



        val hora:String = customCalendar.get(Calendar.MONTH).toString()
        val minuto:String = customCalendar.get(Calendar.DAY_OF_MONTH).toString()
        val minutoCu:String = customCalendar.get(Calendar.MINUTE).toString()
        println("Hora"+hora+minuto)
        println("fragmento")
        println(currentTime)
        println(customTime)
        val data = Data.Builder().putInt(NOTIFICATION_ID, 0)
            .putString("Nombre",nombre)
            .putString("Monto",monto)
            .putString("Frecuencia","Mensual")
            .putString("Llave",llave)
            .build()
        println("frecuencia:")
        val delay = customTime - currentTime
        println("delay"+delay.toString())
        scheduleNotification(delay, data,customCalendar,llave)

    }

    fun getIntID(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis
        return calendar.timeInMillis.toInt()
    }
    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }}