package com.smartath.flagquiz

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private var nameEdit: EditText? = null
    private var startBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEdit = findViewById(R.id.nameEdit)
        startBtn = findViewById(R.id.startBtn)

        startBtn?.setOnClickListener {
            if (nameEdit?.text!!.isNotEmpty()){
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra(Constants.USER_NAME, nameEdit?.text.toString())
                startActivity(intent)
            }
        }
    }
}
