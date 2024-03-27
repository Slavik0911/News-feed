package com.example.newsfeed

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfeed.data.Api
import com.example.newsfeed.data.Article
import com.example.newsfeed.databinding.ActivityMainBinding
import com.example.newsfeed.databinding.InfoBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    //add binding
    private lateinit var infoBinding: InfoBinding
    private lateinit var binding: ActivityMainBinding

    private val adapter = NewsAdapter()

    private val apiKey = "bb0b2f50e12d492f8a82e796291f0982"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infoBinding = InfoBinding.inflate(layoutInflater)

        //json file
        val newsApiUrl = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=$apiKey"
        makeApiCall(newsApiUrl)
    }

    //create Api call
    private fun makeApiCall(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("UUID", "bc50c16d-65f0-45ba-ada5-42f69cf0996f")
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonString = response.body?.string()
                    runOnUiThread {
                        handleJsonData(jsonString)
                    }
                } else {
                    Log.e("tag", "The request failed: ${response.message}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Log.e("tag", "The request failed: ${e.message}")
                }
            }
        })
    }

    //handle JsonData
    private fun handleJsonData(jsonString: String?) {
        if (jsonString.isNullOrEmpty()) {
            Log.e("tag", "Empty or invalid JSON")
            return
        }

        val gson = Gson()
        val data = gson.fromJson(jsonString, Api::class.java)

        val articles = data.articles

        // Declaring a map to store news items by their IDs
        val articleMap = mutableMapOf<String, Article>()

        // Adding new news to the map during JSON processing
        for ((index, article) in articles.withIndex()) {
            articleMap[index.toString()] = article
        }

        // Click handler for the adapter
        adapter.setOnClickListener(object : NewsAdapter.onItemClickListener {
            override fun onItemClick(position: Int, article: Article) {

                val title = article.title
                val author = article.author
                val content = article.content
                val imageUrl = article.urlToImage

                // Output of complete information about the news
                setContentView(infoBinding.root)
                infoBinding.tvContent.text = content
                infoBinding.tvTitle.text = title
                infoBinding.tvInsider.text = author
                Picasso.get().load(imageUrl).into(infoBinding.imageView)
            }
        })

        //Click handler for the button "BACK"
        infoBinding.bBack.setOnClickListener{
            setContentView(binding.root)
        }

        for (item: Article in articles) {

            Picasso.get().load(item.urlToImage).into(object : com.squareup.picasso.Target {
                //add news
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    adapter.addNews(item)
                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    Log.e("TAG", "Failed to load image: $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            })
        }

        // Installing LayoutManager and adapter for RecyclerView
        binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rcView.adapter = adapter
    }
}

// Мене нервують люди ті шо думають мало