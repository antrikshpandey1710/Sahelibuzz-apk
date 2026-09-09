package com.sahelibuzz.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var root: FrameLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge ko manually handle karenge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.BLACK

        root = FrameLayout(this)

        // ---------------- SPLASH SCREEN ----------------

        val splash = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "SaheliBuzz"
            textSize = 38f
            setTextColor(Color.rgb(210, 20, 110))
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "BY VAIBHAV"
            textSize = 16f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 0)
        }

        splash.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        splash.addView(
            subtitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            splash,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // ---------------- WEBVIEW ----------------

        webView = WebView(this).apply {

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                useWideViewPort = true
                loadWithOverviewMode = true
            }

            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            // Initially hidden behind splash
            visibility = View.INVISIBLE
        }

        val webParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        root.addView(webView, webParams)

        setContentView(root)

        // ---------------- SYSTEM BAR FIX ----------------

        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->

            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            val params = webView.layoutParams as FrameLayout.LayoutParams

            // WebView ko status bar aur navigation bar ke bahar rakho
            params.topMargin = bars.top
            params.bottomMargin = bars.bottom

            params.leftMargin = 0
            params.rightMargin = 0

            webView.layoutParams = params

            insets
        }

        ViewCompat.requestApplyInsets(root)

        // ---------------- OPEN WEBSITE ----------------

        android.os.Handler(mainLooper).postDelayed({

            splash.visibility = View.GONE
            webView.visibility = View.VISIBLE

            webView.loadUrl("https://saheli-buzz.vercel.app/")

            ViewCompat.requestApplyInsets(root)

        }, 1500)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.destroy()
        }
        super.onDestroy()
    }
}
