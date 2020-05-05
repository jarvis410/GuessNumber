package com.example.guessnumber

import android.annotation.SuppressLint
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setDefaultTheme()
        setListeners()
        setInstructions()
        setDifficultySpinner()
    }

    private fun setDefaultTheme() {
//        val mode = AppCompatDelegate.getDefaultNightMode()
        val settings = getSharedPreferences(THEME, 0)
        val theme = settings.getInt("theme", 0)

        AppCompatDelegate.setDefaultNightMode(theme)
        if (theme == MODE_NIGHT_NO) {
            themeToggle.isChecked = false
        } else if (theme == MODE_NIGHT_YES) {
            themeToggle.isChecked = true
        }
    }

    private fun setListeners() {
        startButton.setOnClickListener {
            val level: Level = difficultySpinner.selectedItem as Level
            val gameIntent = Intent(this, Game::class.java)
            gameIntent.putExtra(LEVEL, level.toString())
            startActivity(gameIntent)
        }
        
        themeToggle.setOnCheckedChangeListener { _, isChecked ->
            val settings = getSharedPreferences(THEME, 0)
            val editor = settings.edit()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                editor.putInt("theme", MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                editor.putInt("theme", MODE_NIGHT_NO)
            }
            editor.apply()
        }

        aboutBtn.setOnClickListener {
            showAlertDialog {
                setTitle(R.string.about_author)
                setMessage(R.string.about_author_txt)
                positiveButton("Close") {
                }
            }
        }
    }

    private fun showAlertDialog(dialogBuilder: AlertDialog.Builder.() -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.dialogBuilder()
        val dialog = builder.create()

        dialog.show()
    }

    private fun AlertDialog.Builder.positiveButton(text: String = "Okay", handleClick: (which: Int) -> Unit = {}) {
        this.setPositiveButton(text) { _, which->
            handleClick(which)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setInstructions() {
        txtInstruction.text = getString(R.string.instructions_home)
    }

    private fun setDifficultySpinner() {
        val adapter = ArrayAdapter<Level>(
            this,
            android.R.layout.simple_spinner_item,
            Level.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        difficultySpinner.adapter = adapter
    }

    override fun onBackPressed() {
        val backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            finishAffinity()
        } else {
            backToast.show()
        }

        backPressedTime = System.currentTimeMillis()
    }
}
