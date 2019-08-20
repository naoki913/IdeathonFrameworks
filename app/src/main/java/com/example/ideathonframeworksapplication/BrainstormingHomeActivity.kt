package com.example.ideathonframeworksapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_brainstorming_home.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.button_new
import kotlinx.android.synthetic.main.activity_mandala_chart_home.themeText

class BrainstormingHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brainstorming_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Brainstorming"
        val intent= Intent(this,BrainstormingActivity::class.java)

        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)
                intent.putExtra("MIN",LimitTimeMinText.text.toString().toInt())
                intent.putExtra("SEC",LimitTimeSecText.text.toString().toInt())

                startActivity(intent)
            }
            else{
                themeText.setError("テーマを入力してください")
            }
        }
    }
}
