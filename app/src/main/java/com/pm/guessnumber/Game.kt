package com.pm.guessnumber

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

class Game : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var totalGuesses: Int = 0
    private var guess: Int = 0
    private var secretNumber: Int = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setListeners()

        val level: String? = intent?.getStringExtra(LEVEL)
        if (level != null) {
            setDifficultyAndGuesses(level)
        }

        setInstruction()
        setGuessLeft()
        generateSecretNumber()
    }

    private fun setGuessLeft() {
        guessLeft.text = getString(R.string.display_guesses_left,totalGuesses - guess)
    }

    private fun setInstruction() {
        txtInstructions.text = getString(R.string.instructions_game)
    }

    private fun generateSecretNumber() {
        secretNumber = (1..20).shuffled().first()
        Log.i(tag, secretNumber.toString())
    }

    private fun setListeners() {
        quit.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        submit.setOnClickListener {
            submitAnswer()
        }
    }

    private fun submitAnswer() {
        evaluateAnswer()
    }

    private fun evaluateAnswer() {
        when (isValidAnswer()) {
            false -> if (++guess == totalGuesses) {
                lose()
            } else {
                setGuessLeft()
            }
            true -> when (checkAnswer()) {
                true -> win()
                false -> if (++guess == totalGuesses) {
                    lose()
                } else {
                    showHint()
                    setGuessLeft()
                }
            }
        }
    }

    private fun isValidAnswer(): Boolean {
        val answerStr = txtAnswer.text.toString()

        if (answerStr == "") {
            showToast(getString(R.string.empty_input))
            return false;
        } else if (answerStr.toInt() !in 1..20) {
            showToast(getString(R.string.invalid_input))
            txtAnswer.text.clear()
            return false
        }
        return true
    }

    private fun checkAnswer(): Boolean {
        val answer = txtAnswer.text.toString().toInt()

        return answer == secretNumber
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showHint() {
        val answer = txtAnswer.text.toString().toInt()

        val message: String = if (answer < secretNumber) {
            getString(R.string.answer_hint, "less")
        } else {
            getString(R.string.answer_hint, "more")
        }

        showToast(message)
    }

    private fun lose() {
        val message = getString(R.string.result_lose, secretNumber)
        showResultScreen(message)
    }

    private fun win() {
        val message = getString(R.string.result_win)
        showResultScreen(message)
    }

    private fun showResultScreen(message: String) {
        val level: String? = intent?.getStringExtra(LEVEL)
        val resultIntent = Intent(this, Result::class.java)
        resultIntent.putExtra(LEVEL, level)
        resultIntent.putExtra(RESULT, message)
        startActivity(resultIntent)
    }

    private fun setDifficultyAndGuesses(level: String) {
        totalGuesses = Level.valueOf(level).guesses

        difficulty.text = getString(R.string.display_difficulty, level)
        guessCount.text = getString(R.string.display_total_guesses, totalGuesses)
    }

    override fun onBackPressed() { }
}
