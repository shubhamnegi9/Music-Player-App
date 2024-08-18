package com.example.musicplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var musicAdapter: MusicAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var greetingTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.musicRV)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        greetingTv = findViewById(R.id.greetingTv)

        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        // Setting greeting TextView as per the current time
        if(hourOfDay >=0 && hourOfDay < 12) {
            greetingTv.text = "Good Morning"
        }
        else if(hourOfDay >= 12 && hourOfDay < 17) {
            greetingTv.text = "Good Afternoon"
        }
        else {
            greetingTv.text = "Good Evening"
        }

        // Music API: https://rapidapi.com/deezerdevs/api/deezer-1/playground/53aa5087e4b0b60946a2f5ea

        // Create Retrofit Builder
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getMusicData("eminem")

        // Inside enqueue: Shortcut for object = (ctrl+shift+space)
        retrofitData.enqueue(object : Callback<MusicData?> {
            override fun onResponse(call: Call<MusicData?>, response: Response<MusicData?>) {
                if(response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    val musicData = response.body()
                    Log.d("MainActivity", "onResponse: " + musicData)

                    musicData?.let {
                        musicAdapter = MusicAdapter(this@MainActivity, it.data)
                        recyclerView.adapter = musicAdapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }
            }

            override fun onFailure(call: Call<MusicData?>, throwable: Throwable) {
                Log.d("MainActivity", "onFailure: " + throwable)
            }
        })

    }
}