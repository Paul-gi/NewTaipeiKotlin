package com.example.newtaipeizookotlin.fragments

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.WebviewActivityBinding

class WebViewFragment:BaseFragment<WebviewActivityBinding>(){
    override val mLayout: Int
        get() = R.layout.webview_activity
    private lateinit var mUrlString: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        super.initView()
        getBundle()
       mDataBinding.mWebView.run {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(mUrlString)
           mProgressDialogCustom?.dismiss()
        }
    }

    override fun getBundle() {
        val iBundle = arguments
        mUrlString = iBundle!!.getString("getUrl").toString()
    }
}