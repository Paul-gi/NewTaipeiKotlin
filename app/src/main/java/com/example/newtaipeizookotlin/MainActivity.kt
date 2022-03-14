package com.example.newtaipeizookotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.newtaipeizookotlin.Fragments.HomePageFragment
import com.example.newtaipeizookotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private lateinit var myApplication : MyApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val actionbar: android.app.ActionBar? = actionBar
        actionbar?.hide()//隱藏暗色主題actionBar
        supportActionBar?.hide()//隱藏亮色主題actionBar


        init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        myApplication = application as MyApplication
        myApplication.setMyFragmentManager(this.supportFragmentManager)
        myApplication.goToNextPage(HomePageFragment(), intent?.extras)
    }

    private fun init() {
        myApplication = application as MyApplication
        myApplication.setMyFragmentManager(this.supportFragmentManager)
        myApplication.goToNextPage(HomePageFragment(), intent.extras)
    }
}