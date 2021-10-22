package mx.itesm.cerco.proyectofinal.ui.Perfil

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.cerco.proyectofinal.Login
import mx.itesm.cerco.proyectofinal.R
import mx.itesm.cerco.proyectofinal.databinding.FragmentInicioBinding
import mx.itesm.cerco.proyectofinal.databinding.FragmentPerfilBinding
import mx.itesm.cerco.proyectofinal.ui.view.AdaptadorListaMetas

class PerfilFragment : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val mAuth = FirebaseAuth.getInstance()
    private val binding get() = _binding!!
    private lateinit var perfilViewModel: PerfilViewModel

    companion object {
        fun newInstance() = PerfilFragment()
    }

    private lateinit var viewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        perfilViewModel =
            ViewModelProvider(this).get(PerfilViewModel::class.java)

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarEventos()
        obtenerDatosPerfil()


    }

    private fun configurarEventos() {
        binding.btnLogout.setOnClickListener{
            mAuth.signOut()
            AuthUI.getInstance().signOut(requireContext())
            val intLogin = Intent(context, Login::class.java)
            startActivity(intLogin)
        }
    }


    fun obtenerDatosPerfil() {
        val database = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val nombre = FirebaseAuth.getInstance().currentUser?.displayName
        val correo = FirebaseAuth.getInstance().currentUser?.email
        val foto = FirebaseAuth.getInstance().currentUser?.photoUrl

        binding.tvNombre.text=nombre
        binding.tvEmail.text=correo



        DownloadImageFromInternet(binding.ivFotoPerfil).execute(foto.toString())
        //binding.ivFotoPerfil.setImageBitmap(image)
    }

    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }


}