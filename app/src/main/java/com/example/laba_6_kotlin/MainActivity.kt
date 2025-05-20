package com.example.laba_6_kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

const val API_KEY = "ff49fcd4d4a08aa6aafb6ea3de826464"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        photoParsing()
    }

    private fun photoParsing() {
        lifecycleScope.launch {
            try {

                val photos = fetchPhotosFromApi()

                val photoUrls = mutableListOf<String>()
                for (photo in photos) {
                    val url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
                    photoUrls.add(url)
                }

                setupRecyclerView(photoUrls)

                for (indexOfPhoto in photos.indices step 5) {
                    val photo = photos[indexOfPhoto]
                    Timber.d(photo.toString())
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching photos")
            }
        }
    }

    private fun setupRecyclerView(photoUrls: List<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = Adapter(photoUrls) { imageUrl ->
            openActivity(imageUrl)
        }

        Timber.d("Adapter attached with ${photoUrls.size} items")
    }

    private fun openActivity(imageUrl: String) {
        val intent = Intent(this, PicViewerActivity::class.java)
        intent.putExtra("IMAGE_URL", imageUrl)
        startActivityForResult(intent, REQUEST_CODE)
    }

    companion object {
        private const val REQUEST_CODE = 101
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val imageUrl = data?.getStringExtra("FAVORITE_IMAGE_URL")
            val isFavorite = data?.getBooleanExtra("IS_FAVORITE", false) ?: false

            if (isFavorite && imageUrl != null) {

                val rootView = findViewById<View>(android.R.id.content)
                Snackbar.make(rootView, "Картинка добавлена в избранное", Snackbar.LENGTH_LONG)
                    .setAction("ОТКРЫТЬ") {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                        startActivity(intent)
                    }
                    .show()

            }
        }
    }

    private suspend fun fetchPhotosFromApi(): List<Photo> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" +
                    API_KEY +
                    "&tags=" +
                    "cat" +
                    "&format=" +
                    "json" +
                    "&nojsoncallback=" +
                    "1")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()

        if (body != null) {
            val wrapper = Gson().fromJson(body, Wrapper::class.java)
            wrapper.photos.photo
        } else {
            emptyList()
        }
    }
}

