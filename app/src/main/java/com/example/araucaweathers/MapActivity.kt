package com.example.araucaweathers

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
 
        webView = findViewById(R.id.web_view_map)
        setupWebView()
        loadMap()
    }

    private fun setupWebView() {
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        // Para asegurarse de que la navegación ocurra dentro de WebView
        webView.webViewClient = WebViewClient()
    }

    private fun loadMap() {
        // Carga el archivo HTML local del mapa
        webView.loadUrl("file:///android_asset/map.html")
    }
}
