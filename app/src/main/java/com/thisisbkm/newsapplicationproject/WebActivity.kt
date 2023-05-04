package com.thisisbkm.newsapplicationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.thisisbkm.newsapplicationproject.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWebBinding
    private val TAG="WebActivity"
    private lateinit var webView: WebView
    private lateinit var url:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webview
        if(intent !=null){
           url = intent.getStringExtra("url_key").toString()
           webView.loadUrl(url)
        }
    }
}