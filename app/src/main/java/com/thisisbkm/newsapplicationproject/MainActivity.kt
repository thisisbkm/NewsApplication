package com.thisisbkm.newsapplicationproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.firebase.auth.FirebaseAuth
import com.jacksonandroidnetworking.JacksonParserFactory
import com.madrapps.pikolo.RGBColorPicker
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import com.thisisbkm.newsapplicationproject.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    // declaring the views
    private var mProgressBar: ProgressBar? = null
    private var mRecyclerView: RecyclerView? = null

    // declaring an ArrayList of articles
    private var mArticleList: ArrayList<NewsArticle>? = null
    private var mArticleAdapter: ArticleAdapter? = null
    private lateinit var binding:ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // initializing the Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_user_email).text = FirebaseAuth.getInstance().currentUser?.email
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up the navigation drawer
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_sign_out -> {
                    signOut()
                }
            }
            true
        }
        // setting the JacksonParserFactory
        AndroidNetworking.setParserFactory(JacksonParserFactory())
        // assigning views to their ids
        mProgressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView

        // setting the recyclerview layout manager
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)

        // initializing the ArrayList of articles
        mArticleList = ArrayList()

        // calling get_news_from_api()
        get_news_from_api()
    }

    private fun get_news_from_api() {
        mArticleList!!.clear()
        AndroidNetworking.get("https://newsapi.org/v2/top-headlines")
            .addQueryParameter("country", "in")
            .addQueryParameter("apiKey", API_KEY)
            .addHeaders("token", "1234")
            .setTag("test")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // disabling the progress bar
                    mProgressBar!!.visibility = View.GONE

                    // handling the response
                    try {

                        // storing the response in a JSONArray
                        val articles = response.getJSONArray("articles")

                        // looping through all the articles
                        // to access them individually
                        for (j in 0 until articles.length()) {
                            // accessing each article object in the JSONArray
                            val article = articles.getJSONObject(j)

                            // initializing an empty ArticleModel
                            val currentArticle = NewsArticle()

                            // storing values of the article object properties
                            val author = article.getString("author")
                            val title = article.getString("title")
                            val description = article.getString("description")
                            val url = article.getString("url")
                            val urlToImage = article.getString("urlToImage")
                            val publishedAt = article.getString("publishedAt")
                            val content = article.getString("content")

                            // setting the values of the ArticleModel
                            // using the set methods
                            currentArticle.author = author
                            currentArticle.title = title
                            currentArticle.description = description
                            currentArticle.url = url
                            currentArticle.urlToImage = urlToImage
                            currentArticle.publishedAt = publishedAt
                            currentArticle.content = content

                            // adding an article to the articles List
                            mArticleList!!.add(currentArticle)
                        }

                        // setting the adapter
                        mArticleAdapter = ArticleAdapter(applicationContext, mArticleList!!)
                        mRecyclerView!!.adapter = mArticleAdapter
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        // logging the JSONException LogCat
                        Log.d(TAG, "Error : " + e.message)
                    }
                }

                override fun onError(error: ANError) {
                    // logging the error detail and response to LogCat
                    Log.d(TAG, "Error detail : " + error.errorDetail)
                    Log.d(TAG, "Error response : " + error.response)
                }
            })
    }

    companion object {
        // TODO : set the API_KEY variable to your api key
        private const val API_KEY = "5c33cfb4e0a54dbc80b94ea3935f88c9"

        // setting the TAG for debugging purposes
        private const val TAG = "MainActivity"
    }
    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}