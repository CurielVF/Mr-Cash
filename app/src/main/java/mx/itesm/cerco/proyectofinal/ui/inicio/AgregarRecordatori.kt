package mx.itesm.cerco.proyectofinal.ui.inicio

import android.R
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mx.itesm.cerco.proyectofinal.databinding.ActivityAgregarRecordatoriBinding
import mx.itesm.cerco.proyectofinal.ui.estadisticas.TipoRecordatorios
import mx.itesm.cerco.proyectofinal.ui.inicio.NotificacionWorkManager.Companion.NOTIFICATION_ID
import mx.itesm.cerco.proyectofinal.ui.inicio.NotificacionWorkManager.Companion.NOTIFICATION_WORK
import mx.itesm.cerco.proyectofinal.ui.model.Recordatorio
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AgregarRecordatori : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarRecordatoriBinding
    private lateinit var baseDatos: FirebaseDatabase
    private lateinit var key: String
    private lateinit var uuidRecordatorio: String
    var numeroNotificaciones = 0;
    private lateinit var frecuencia: String
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var opcionTipo: Spinner
    lateinit var tipoRecordatorio: String
    var opcionesRecordatorio = arrayOf(
        TipoRecordatorios.Agua,
        TipoRecordatorios.Colegiatura,
        TipoRecordatorios.Entretenimiento,
        TipoRecordatorios.Gimnasio,
        TipoRecordatorios.Hogar,
        TipoRecordatorios.Tarjetas,
        TipoRecordatorios.Telefonia,
        TipoRecordatorios.Luz,
        TipoRecordatorios.Otro
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarRecordatoriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        baseDatos = Firebase.database
        configurarObservadores()
        configurarEventos()
    }

    private fun configurarObservadores() {
        opcionTipo = binding.sORecordatorioTipo
        opcionTipo.adapter =
            ArrayAdapter<TipoRecordatorios>(this, R.layout.simple_list_item_1, opcionesRecordatorio)
        binding.dateP.minDate = System.currentTimeMillis() - 1000;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarEventos() {

        binding.btnAgregarRecordatorio.setOnClickListener {
            if(validarFecha()){
                agregarRecordatorio()
            }
            else{
                try {
                    Double.parseDouble(binding.etMontoR.text.toString())
                } catch (e: NumberFormatException) {
                    binding.etMontoR.setError("Monto inválido")
                }
                if (binding.etNombreR.text.toString().isBlank()) {
                    binding.etNombreR.setError("Nombre inválido")
                }
                Toast.makeText(baseContext, "Fecha y hora no validas", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        opcionTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tipoRecordatorio = opcionesRecordatorio.get(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("Selecciona una opción")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun validarFecha(): Boolean {
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.dateP.year, binding.dateP.month, binding.dateP.dayOfMonth,
            binding.timeP.hour, binding.timeP.minute, 0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        return customTime > currentTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun agregarRecordatorio() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        if (!binding.cbMensual.isChecked){
            frecuencia="Unico"
        }
        else{
            frecuencia="Mensual"
        }
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.dateP.year, binding.dateP.month, binding.dateP.dayOfMonth,
            binding.timeP.hour, binding.timeP.minute, 0
        )
        //Crea el formato del Fecha
        val format = SimpleDateFormat("yyyy-MM-dd")
        val strDate = format.format(customCalendar.time)
        //binding.etFecha.setText(strDate)

        //Crea el formato del Fecha
        //binding.etHora.setText(String.format("%02d:%02d", binding.timeP.hour, binding.timeP.minute))

        key = database.getReference(uid + "/Recordatorios").push().getKey().toString()
        val myRef = database.getReference(uid + "/Recordatorios/" + key)

        try {
            val nombre = binding.etNombreR.text.toString()
            val monto = binding.etMontoR.text.toString().toDouble()
            val tipo = tipoRecordatorio
            val hora = String.format("%02d:%02d", binding.timeP.hour, binding.timeP.minute)
            val recordatorio = Recordatorio(nombre, strDate, monto, tipo, hora, null,null, frecuencia )
            myRef.setValue(recordatorio)
            userInterface()
            super.onBackPressed();
        } catch (e: Exception) {
            try {
                Double.parseDouble(binding.etMontoR.text.toString())
            } catch (e: NumberFormatException) {
                binding.etMontoR.setError("Monto inválido")
            }
            if (binding.etNombreR.text.toString().isBlank()) {
                binding.etNombreR.setError("Nombre inválido")
            }
            Toast.makeText(baseContext, "Debes introducir todos los campos", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun userInterface() {
        setSupportActionBar(binding.toolbar)
        val titleNotification = getString(mx.itesm.cerco.proyectofinal.R.string.notification_title)
        //binding.collapsingToolbarL.title = titleNotification


        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            binding.dateP.year, binding.dateP.month, binding.dateP.dayOfMonth,
            binding.timeP.hour, binding.timeP.minute, 0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        if (customTime > currentTime) {
            val data = Data.Builder().putInt(NOTIFICATION_ID, 0)
                .putString("Nombre",binding.etNombreR.text.toString())
                .putString("Monto",binding.etMontoR.text.toString())
                .putString("Frecuencia",frecuencia)
                .putString("Llave",key).build()

            val delay = customTime - currentTime
            scheduleNotification(delay, data)

            val titleNotificationSchedule = getString(mx.itesm.cerco.proyectofinal.R.string.notification_schedule_title)
            val patternNotificationSchedule = getString(mx.itesm.cerco.proyectofinal.R.string.notification_schedule_pattern)
            Snackbar.make(
                binding.coordinatorL,
                titleNotificationSchedule + SimpleDateFormat(
                    patternNotificationSchedule, Locale.getDefault()
                ).format(customCalendar.time).toString(),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            val errorNotificationSchedule = getString(mx.itesm.cerco.proyectofinal.R.string.notification_schedule_error)
            Snackbar.make(binding.coordinatorL, errorNotificationSchedule, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val notificationWork = OneTimeWorkRequest.Builder(NotificacionWorkManager::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data)
            .addTag(uid!!)
            .build()
        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            notificationWork.id.toString(),
            androidx.work.ExistingWorkPolicy.APPEND, notificationWork
        ).enqueue()

        uuidRecordatorio = notificationWork.id.toString()
        val uuid = notificationWork.id.toString()
        val database = FirebaseDatabase.getInstance()
        val urlRecordatorios = uid+"/Recordatorios/"+key
        val myRef =database.getReference(urlRecordatorios+"/uuidRecordatorio")
        myRef.setValue(uuid)

    }

}


