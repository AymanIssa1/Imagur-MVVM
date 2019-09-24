package com.example.imagurtask.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Activity.launchActivity(activity: Activity, bundle: Bundle? = null) {
    val intent = Intent(this, activity::class.java)
    bundle?.let { intent.putExtras(it) }
    startActivity(intent)
}