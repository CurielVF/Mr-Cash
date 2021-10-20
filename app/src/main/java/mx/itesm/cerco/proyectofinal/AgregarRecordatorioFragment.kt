package mx.itesm.cerco.proyectofinal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AgregarRecordatorioFragment : Fragment() {

    companion object {
        fun newInstance() = AgregarRecordatorioFragment()
    }

    private lateinit var viewModel: AgregarRecordatorioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agregar_recordatorio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgregarRecordatorioViewModel::class.java)
        // TODO: Use the ViewModel
    }

}