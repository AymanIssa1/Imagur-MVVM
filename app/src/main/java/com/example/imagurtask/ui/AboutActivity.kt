package com.example.imagurtask.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imagurtask.BuildConfig
import com.example.imagurtask.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        versionNumberTextView.text = "Version: ${BuildConfig.VERSION_NAME}"

    }

}
