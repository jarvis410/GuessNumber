package com.example.guessnumber

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class Result : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setListeners()
        setResult()

    }

    private fun setListeners() {
        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        playAgainBtn.setOnClickListener {
            val level: String? = intent?.getStringExtra(LEVEL)
            val gameIntent = Intent(this, Game::class.java)
            gameIntent.putExtra(LEVEL, level)
            startActivity(gameIntent)
        }
    }

    private fun setResult() {
        val result = intent.getStringExtra(RESULT)
        txtResult.text = result
    }

    override fun onBackPressed() {

    }
}
