package com.smartath.flagquiz

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private var optionOne: TextView? = null
    private var optionTwo: TextView? = null
    private var optionThree: TextView? = null
    private var optionFour: TextView? = null

    private var flagImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var barText: TextView? = null
    private var submitBtn: Button? = null

    private var questionsList: ArrayList<Question>? = null
    private var currentPosition: Int = 1
    private var selectedPosition: Int = 0
    private var correctAnswers: Int = 0

    private var userName: String? = null

    private var countryList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        webScraping()

        optionOne = findViewById(R.id.optionOne)
        optionTwo = findViewById(R.id.optionTwo)
        optionThree = findViewById(R.id.optionThree)
        optionFour = findViewById(R.id.optionFour)

        flagImage = findViewById(R.id.flagImage)
        progressBar = findViewById(R.id.progressBar)
        barText = findViewById(R.id.barText)
        submitBtn = findViewById(R.id.submitBtn)

        userName = intent.getStringExtra(Constants.USER_NAME)

        submitBtn?.setOnClickListener(this)
        optionOne?.setOnClickListener(this)
        optionTwo?.setOnClickListener(this)
        optionThree?.setOnClickListener(this)
        optionFour?.setOnClickListener(this)
    }

    private fun webScraping()  {
        lifecycleScope.launch {
            withContext(Dispatchers.Default){
                val document: Document? = Jsoup.connect("https://www.worldometers.info/geography/alphabetical-list-of-countries/").get()

                val table: Element? = document?.select("table")?.get(0)
                val rows: Elements? = table?.select("tr")

                countryList = ArrayList<String>()

                for (i in 1 until rows!!.size) {
                    val row: Element? = rows[i]
                    val td: Elements? = row?.allElements
                    countryList?.add(td?.get(2)!!.text())
                }
                questionsList = countryList?.let { Constants.getQuestions(it) }
            }
            withContext(Dispatchers.Main){
                setQuestion()
            }
        }
    }

    private fun setQuestion(){
        defaultOptionsView()
        val question: Question? = questionsList!![currentPosition-1]
        flagImage?.setImageResource(question!!.imageFlag)
        progressBar?.progress = currentPosition
        barText?.text = "$currentPosition / ${progressBar?.max}"
        optionOne?.text = question?.option1
        optionTwo?.text = question?.option2
        optionThree?.text = question?.option3
        optionFour?.text = question?.option4

        if (currentPosition == questionsList!!.size){
            submitBtn?.text = "FINISH"
        }
        else{
            submitBtn?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        optionOne?.let {
            options.add(0,it)
        }
        optionTwo?.let {
            options.add(1,it)
        }
        optionThree?.let {
            options.add(2,it)
        }
        optionFour?.let {
            options.add(3,it)
        }

        for (option in options){
            option.setTextColor(Color.parseColor("#FFFFFF"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.option_border_bg)
        }
    }

    private fun selectedOptionsView(text: TextView, selectedOptionsNum: Int){
        defaultOptionsView()

        selectedPosition = selectedOptionsNum
        text.setTextColor(Color.parseColor("#FFFFFF"))
        text.setTypeface(text.typeface, Typeface.BOLD)
        text.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.optionOne -> {
                optionOne?.let {
                    selectedOptionsView(it, 1)
                }
            }
            R.id.optionTwo -> {
                optionTwo?.let {
                    selectedOptionsView(it, 2)
                }
            }
            R.id.optionThree -> {
                optionThree?.let {
                    selectedOptionsView(it, 3)
                }
            }
            R.id.optionFour -> {
                optionFour?.let {
                    selectedOptionsView(it, 4)
                }
            }
            R.id.submitBtn -> {
                if (selectedPosition == 0){
                    currentPosition++

                    when{
                        currentPosition <= questionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, userName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else{
                    val question = questionsList?.get(currentPosition-1)
                    if (question!!.correctAnswer != selectedPosition){
                        answerView(selectedPosition, R.drawable.wrong_answer_border_bg)
                    }
                    else{
                        correctAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_answer_border_bg)

                    if (currentPosition == questionsList!!.size){
                        submitBtn?.text = "FINISH"
                    }
                    else{
                        submitBtn?.text = "NEXT QUESTION"
                    }

                    selectedPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                optionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                optionTwo?.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                optionThree?.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                optionFour?.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

}