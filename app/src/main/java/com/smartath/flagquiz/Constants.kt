package com.smartath.flagquiz

import android.content.ContentValues.TAG
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.Collections


object Constants {

    const val USER_NAME: String = "user_name"
    const val CORRECT_ANSWERS = "correct_answers"
    const val TOTAL_QUESTIONS = "total_questions"

    fun getQuestions(countries: ArrayList<String>): ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        countries.remove("Argentina")
        countries.remove("Australia")
        countries.remove("Belgium")
        countries.remove("Brazil")
        countries.remove("Denmark")
        countries.remove("Fiji")
        countries.remove("Germany")
        countries.remove("India")
        countries.remove("Kuwait")
        countries.remove("New Zealand")

        var correctCountries = ArrayList<String>()
        correctCountries.add("Argentina")
        correctCountries.add("Australia")
        correctCountries.add("Belgium")
        correctCountries.add("Brazil")
        correctCountries.add("Denmark")
        correctCountries.add("Fiji")
        correctCountries.add("Germany")
        correctCountries.add("India")
        correctCountries.add("Kuwait")
        correctCountries.add("New Zealand")

        var countriesSelected : List<String> = emptyList()
        countriesSelected = countries.shuffled().take(30)

        for (i in 0..9){
            var flagCollection: List<Int> = emptyList()
            flagCollection = listOf(R.drawable.ic_flag_of_argentina, R.drawable.ic_flag_of_australia, R.drawable.ic_flag_of_belgium,
                R.drawable.ic_flag_of_brazil, R.drawable.ic_flag_of_denmark, R.drawable.ic_flag_of_fiji, R.drawable.ic_flag_of_germany,
                R.drawable.ic_flag_of_india, R.drawable.ic_flag_of_kuwait, R.drawable.ic_flag_of_new_zealand)

            var collection: List<String> = emptyList()
            collection = listOf(countriesSelected[3*i], countriesSelected[3*i+1], countriesSelected[3*i+2], correctCountries[i])
            Collections.shuffle(collection)

            val question = Question(i+1, "What country does this flag belong to?", flagCollection[i],
                collection[0], collection[1], collection[2], collection[3], collection.indexOf(correctCountries[i])+1)
            questionsList.add(question)

        }
        return questionsList
    }


}