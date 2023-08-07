package com.smartath.flagquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    private var nameText: TextView? = null
    private var scoreText: TextView? = null
    private var againBtn: Button? = null
    private var finishBtn: Button? = null

    private var userName: String? = null
    private var correctAnswers: Int? = null
    private var totalQuestions: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        userName = intent.getStringExtra(Constants.USER_NAME)
        correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)

        nameText = findViewById(R.id.nameText)
        scoreText = findViewById(R.id.scoreText)
        againBtn = findViewById(R.id.againBtn)
        finishBtn = findViewById(R.id.finishBtn)

        nameText?.text = userName

        scoreText?.text = "Your score is $correctAnswers out of $totalQuestions"

        againBtn?.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(Constants.USER_NAME, userName)
            startActivity(intent)
            finish()
        }
        finishBtn?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}