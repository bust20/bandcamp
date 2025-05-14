package com.example.bandcamp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread


class Musician : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val musicianList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musician)

        listView = findViewById(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicianList)
        listView.adapter = adapter

        // Executa la connexió en un nou fil
        thread {
            val gestor = GestorSQLExtern()
            val jsonArray = gestor.connectar("http://10.0.2.2/api_bandcamp/get_musicians.php")

            if (jsonArray != null) {
                runOnUiThread {
                    processarResultats(jsonArray)
                }
            }
        }
    }

    private fun processarResultats(jsonArray: JSONArray) {
        try {
            for (i in 0 until jsonArray.length()) {
                val musician = jsonArray.getJSONObject(i)
                val name = musician.getString("nom")
                val surname = musician.getString("cognoms")
                musicianList.add("$surname $name")
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}