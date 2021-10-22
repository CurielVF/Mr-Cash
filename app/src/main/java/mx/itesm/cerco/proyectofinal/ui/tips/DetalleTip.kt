package mx.itesm.cerco.proyectofinal.ui.tips

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import mx.itesm.cerco.proyectofinal.databinding.DetalleTipFragmentBinding
import mx.itesm.cerco.proyectofinal.ui.tips.model.DetalleTipViewModel

class DetalleTip : Fragment() {

    companion object {
        fun newInstance() = DetalleTip()
    }

    private lateinit var viewModel: DetalleTipViewModel
    private lateinit var  binding: DetalleTipFragmentBinding

    private val  args : DetalleTipArgs by navArgs<DetalleTipArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetalleTipFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetalleTipViewModel::class.java)
        // TODO: Use the ViewModel

        binding.webView.webChromeClient = object : WebChromeClient(){

        }
        binding.webView.webViewClient = object  : WebViewClient(){

        }

        val URL = args.tipSeleccionado.URL

        binding.webView.loadUrl(URL)

    }

}