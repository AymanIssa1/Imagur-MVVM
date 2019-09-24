package com.example.imagurtask.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import com.zplesac.connectionbuddy.models.ConnectivityState

abstract class BaseActivity : AppCompatActivity(), ConnectivityChangeListener {

    override fun onStart() {
        super.onStart()
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this)
    }

    override fun onStop() {
        super.onStop()
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this)
    }

    override fun onConnectionChange(event: ConnectivityEvent) {
        connectivityState = event.state
        if (event.state.value == ConnectivityState.CONNECTED) {
            if (!isInternetConnectedViewShowed) {
                Toast.makeText(this, "Internet Connected", Toast.LENGTH_LONG).show()
                isInternetConnectedViewShowed = true
            }
        } else if (event.state.value == ConnectivityState.DISCONNECTED) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
            isInternetConnectedViewShowed = false
        }
    }

    companion object {
        private var isInternetConnectedViewShowed = true
        private var connectivityState: ConnectivityState? = null
    }
}
