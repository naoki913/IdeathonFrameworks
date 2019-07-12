package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.button_load

class MandalaChartHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart_home)

        val dataStore: SharedPreferences =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val editor = dataStore.edit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"

        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)

                val intent= Intent(this,MandalaChartActivity::class.java)

                intent.putExtra("THEME_KEY",themeText.text.toString())

                startActivity(intent)

            }
            else{
                themeText.setError("テーマを入力してください")
                println("0000000")
            }

        }

        button_load.setOnClickListener {
            val jsonString=dataStore.getString("words","nothing")
            Log.d("log",jsonString)
            val gson= Gson()

            val str = gson.fromJson(jsonString,Array<Array<String>>::class.java)

            Log.d("log",str[0][0])

        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->finish()
            else ->return super.onOptionsItemSelected(item)
        }
        return true
    }
}
