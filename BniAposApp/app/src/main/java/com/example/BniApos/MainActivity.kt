package com.example.BniApos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.paymentsdk.ClientSdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clientSdk = ClientSdk()
        Toast.makeText(this, clientSdk.getToastMessage(), Toast.LENGTH_LONG).show()
    }
}