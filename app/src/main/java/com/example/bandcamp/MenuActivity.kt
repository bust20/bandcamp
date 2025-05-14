package com.example.bandcamp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.albumParis2016).setOnClickListener {
            val intent = Intent(this, Album2016Activity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.albumBackToRockport).setOnClickListener {
            val intent = Intent(this, Album2016Activity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.albumKeoland).setOnClickListener {
            val intent = Intent(this, Album2016Activity::class.java)
            startActivity(intent)
        }



    }
}