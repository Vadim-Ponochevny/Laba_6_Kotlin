package com.example.laba_6_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.laba_6_kotlin.databinding.ActivityPicViewerBinding

class PicViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPicViewerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )

            val layoutParams = view.layoutParams
            layoutParams.height = statusBarHeight + resources.getDimensionPixelSize(R.dimen.toolbar_height)
            view.layoutParams = layoutParams

            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""
        Glide.with(this)
            .load(imageUrl)
            .into(binding.fullscreenImageView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""

                val resultIntent = Intent().apply {
                    putExtra("FAVORITE_IMAGE_URL", imageUrl)
                    putExtra("IS_FAVORITE", true)
                }

                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}