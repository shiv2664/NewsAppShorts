package com.shivam.newsappshorts.fragments.detail.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shivam.newsappshorts.databinding.FragmentDetailsBinding
import com.shivam.newsappshorts.fragments.detail.viewmodel.DetailsViewModel
import com.shivam.newsappshorts.utility.Utility.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            settings.useWideViewPort = true
            getSettings().javaScriptEnabled = true
        }

        val url=arguments?.getString("url")

        if (requireContext().isInternetAvailable()){
            binding.webView.loadUrl(url.toString())
        }else{
            Toast.makeText(requireActivity(), "No Internet", Toast.LENGTH_SHORT).show()
        }

        binding.ivBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String?
            ): Boolean {
                view?.loadUrl(url.toString())
                return super.shouldOverrideUrlLoading(view, url)

            }

        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
