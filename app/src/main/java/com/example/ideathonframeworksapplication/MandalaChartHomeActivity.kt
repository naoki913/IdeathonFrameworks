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
import org.json.JSONArray
import kotlin.properties.Delegates

class MandalaChartHomeActivity : AppCompatActivity() {
    var  themes : ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart_home)

        val dataStore: SharedPreferences =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val editor = dataStore.edit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"

        //val jsonString=dataStore.getString("themes","nothing")
        //Log.d("log",jsonString)
        val gson= Gson()
        val jsonArray = JSONArray(dataStore.getString("theme","[]"))
        println(jsonArray)
        for(t in 0 .. jsonArray.length()-1){
            themes.add(t.toString())
        }

        val intent= Intent(this,MandalaChartActivity::class.java)
        println(themes)

        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)
                themes.add(themeText.text.toString())
                /*
                val gson= Gson()
                val jsonString:String =gson.toJson(themes)
                editor.putString("theme",jsonString)
                */

                val jsonArray =JSONArray(themes)
                editor.putString("theme",jsonArray.toString())
                editor.apply()

                intent.putExtra("IS_NEW",true)
                intent.putExtra("THEME_KEY",themeText.text.toString())

                startActivity(intent)

            }
            else{
                themeText.setError("テーマを入力してください")
            }

        }

        button_load.setOnClickListener {

            intent.putExtra("IS_NEW",false)
            startActivity(intent)

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
