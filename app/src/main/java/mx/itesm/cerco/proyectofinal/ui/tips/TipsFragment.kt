package mx.itesm.cerco.proyectofinal.ui.tips

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.itesm.cerco.proyectofinal.R

class TipsFragment : Fragment() {

    companion object {
        fun newInstance() = TipsFragment()
    }

    private lateinit var viewModel: TipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tips, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TipsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}