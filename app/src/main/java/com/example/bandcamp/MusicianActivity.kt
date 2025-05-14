package com.example.bandcamp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import kotlin.concurrent.thread
import java.text.SimpleDateFormat
import java.util.*


class MusicianActivity:AppCompatActivity(){

    private lateinit var textViewName:TextView
    private lateinit var textViewSurnames:TextView
    private lateinit var textViewDateOfBirth:TextView
    private lateinit var textViewNameData:TextView
    private lateinit var textViewSurnamesData:TextView
    private lateinit var textViewDateOfBirthData:TextView

    // New Age fields
    private lateinit var textViewAge:TextView
    private lateinit var textViewAgeData:TextView

    private lateinit var textViewDateOfDeath:TextView
    private lateinit var textViewCross:TextView
    private lateinit var layoutDateOfDeath:LinearLayout
    private lateinit var imageViewMusician:ImageView
    private lateinit var textViewStatus:TextView

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musician_detail)

        textViewName=findViewById(R.id.textViewName)
        textViewSurnames=findViewById(R.id.textViewSurnames)
        //textViewDateOfBirth=findViewById(R.id.textViewDateOfBirth)

        // New text fields for data display
        textViewNameData=findViewById(R.id.textViewNameData)
        textViewSurnamesData=findViewById(R.id.textViewSurnamesData)
        //textViewDateOfBirthData=findViewById(R.id.textViewDateOfBirthData)

        // New Age fields
        textViewAge=findViewById(R.id.textViewAge)
        //textViewAgeData=findViewById(R.id.textViewAgeData)

        //textViewDateOfDeath=findViewById(R.id.textViewDateOfDeath)
        //textViewCross=findViewById(R.id.textViewCross)
        //layoutDateOfDeath=findViewById(R.id.layoutDateOfDeath)
        imageViewMusician=findViewById(R.id.image_musician)

        thread {
            Log.d("MusicianActivity","Starting HTTP connection")
            val gestor=GestorSQLExtern()
            val jsonArray=gestor.connectar(this@MusicianActivity,"http://10.0.2.2api_bandcamp//get_musicians.php")

            runOnUiThread {
                if(jsonArray!=null){
                    Log.d("MusicianActivity","Connection successful: data received")
                    processarResultats(jsonArray)
                }else{
                    Log.e("MusicianActivity","Error: No data received from server")
                    textViewStatus.text="Error: Could not fetch data. Is XAMPP running?"

                    Toast.makeText(this@MusicianActivity,
                        "Can't connect to DB. Please start XAMPP.", Toast.LENGTH_LONG).show()

                    Log.d("MusicianActivity","Can't connect to DB. Please start XAMPP.")
                }
            }
        }
    }

    private fun processarResultats(jsonArray:JSONArray){
        try{
            if(jsonArray.length()>0){
                val musician=jsonArray.getJSONObject(0)
                val name=musician.getString("nom")
                val surname=musician.getString("cognoms")
                val dateOfBirth=musician.getString("data_naix")
                val dateOfDeath = musician.optString("data_mort", "")
                val baseUrl="http://10.0.2.2/"
                val imageUrl=baseUrl+musician.getString("ruta_foto")

                Log.d("MusicianActivity","Musician found: $surname $name")

                // Populate the new text fields
                textViewNameData.text=name
                textViewSurnamesData.text=surname
                textViewDateOfBirthData.text=dateOfBirth

                // Calculate and display age
                textViewAgeData.text=calculateAge(dateOfBirth)

                if(!dateOfDeath.isNullOrEmpty()&&dateOfDeath!="0000-00-00"){
                    layoutDateOfDeath.visibility=LinearLayout.VISIBLE
                    textViewDateOfDeath.text="Died: $dateOfDeath"
                    textViewCross.visibility=TextView.VISIBLE
                }else{
                    layoutDateOfDeath.visibility=LinearLayout.GONE
                }

                if(imageUrl.isNotEmpty()){
                    loadImageFromUrl(imageUrl)
                }else{
                    imageViewMusician.setImageResource(R.drawable.play)
                }
            }else{
                textViewStatus.text="No musician found."
            }
        }catch(e:Exception){
            Log.e("MusicianActivity","Error processing JSON",e)
            e.printStackTrace()
        }
    }

    // Function to calculate age based on birthdate
    private fun calculateAge(dateOfBirth:String):String{
        return try{
            val sdf=SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            val birthDate=sdf.parse(dateOfBirth)
            val today=Calendar.getInstance()

            val birthCalendar=Calendar.getInstance().apply { time=birthDate!! }

            var age=today.get(Calendar.YEAR)-birthCalendar.get(Calendar.YEAR)

            if(today.get(Calendar.DAY_OF_YEAR)<birthCalendar.get(Calendar.DAY_OF_YEAR)){
                age--
            }

            "$age years"
        }catch(e:Exception){
            "N/A"
        }
    }

    // Function to load image from URL using Coroutines
    private fun loadImageFromUrl(imageUrl:String){

        Log.d("MusicianActivity","imageUrl: "+imageUrl)

        CoroutineScope(Dispatchers.IO).launch{
            val bitmap=try{
                val url=URL(imageUrl)
                val connection=url.openConnection() as HttpURLConnection
                connection.doInput=true
                connection.connect()
                val inputStream=connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            }catch(e:Exception){
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main){
                if(bitmap!=null){
                    imageViewMusician.setImageBitmap(bitmap)
                }else{
                    imageViewMusician.setImageResource(R.drawable.play)
                }
            }
        }
    }
}
