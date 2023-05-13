package com.asthana.rapidapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asthana.rapidapi.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MovieAdapter
    lateinit var myarrayList: ArrayList<OTTDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiKey = "YOURAPIKEY"

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        myarrayList = arrayListOf()

        // Initialize the adapter
        adapter = MovieAdapter(this, mutableListOf())
        recyclerView.adapter = adapter


        // Make the API call
        makeApiCall(apiKey)
    }

    private fun makeApiCall(apiKey: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://ott-details.p.rapidapi.com/advancedsearch?start_year=1970&end_year=2020&min_imdb=6&max_imdb=7.8&genre=action&language=english&type=movie&sort=latest&page=1")
            .get()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", "ott-details.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@MainActivity, "Error Fetching data", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Response Body", responseBody.toString())

                if (response.isSuccessful) {
                    val jsonObject = JSONObject(responseBody)
                    val resultsArray = jsonObject.getJSONArray("results")

                    val ottList = mutableListOf<OTTDetails>()
                    for (i in 0 until resultsArray.length()) {
                        val resultObject = resultsArray.getJSONObject(i)
                        val title = resultObject.getString("title")
                        val released = resultObject.getInt("released")
                        val imdbrating = resultObject.getDouble("imdbrating").toFloat()
                        val imageurl = if (resultObject.has("imageurl") && resultObject["imageurl"] is JSONArray) {
                            val imageArray = resultObject.getJSONArray("imageurl")
                            if (imageArray.length() > 0) {
                                imageArray.getString(0)
                            } else {
                                null
                            }
                        } else {
                            null
                        }
                        val ottDetail = OTTDetails(title, released, imdbrating, imageurl)
                        ottList.add(ottDetail)
                    }

                    runOnUiThread {
                        adapter.setData(ottList)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

        })
    }
}
