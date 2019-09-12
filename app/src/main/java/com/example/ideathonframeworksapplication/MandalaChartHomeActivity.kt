package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
//import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*

import org.json.JSONArray
import kotlin.properties.Delegates

class MandalaChartHomeActivity : AppCompatActivity() {
    var  themes : ArrayList<String> = arrayListOf()
    lateinit var vg:ViewGroup
    lateinit var dataStore: SharedPreferences
    lateinit var editor:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart_home)

        dataStore =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor = dataStore.edit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"
        val intent= Intent(this,MandalaChartActivity::class.java)
        vg = findViewById<View>(R.id.TableLayout) as ViewGroup


        println(themes)

        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)
                intent.putExtra("MC_IS_NEW",true)
                intent.putExtra("MC_THEME_KEY",themeText.text.toString())

                startActivity(intent)
            }
            else{
                themeText.setError("テーマを入力してください")
            }
        }



    }

    override fun onResume() {
        super.onResume()
        println("onResume")

        val intent= Intent(this,MandalaChartActivity::class.java)
        //editor.remove("theme")
        editor.apply()

        val jsonArray = JSONArray(dataStore.getString("MC_theme","[]"))
        println(jsonArray)


        vg.removeAllViews()

        themes.clear()
        println(themes)

        for(t in 0 .. jsonArray.length()-1){
            themes.add(jsonArray.get(t).toString())
            getLayoutInflater().inflate(R.layout.mandala_chart_load_item, vg)

            val tr=vg.getChildAt(t) as TableRow
            val ll=tr.getChildAt(0) as LinearLayout

            (ll.getChildAt(0)as Button).setText(themes[t])
            (ll.getChildAt(0)as Button).setOnClickListener {
                intent.putExtra("MC_IS_NEW",false)
                intent.putExtra("MC_THEME_KEY",themes[t])
                startActivity(intent)
            }

            (ll.getChildAt(1)as TextView).setText(themes[t]+"に関する説明")

            (ll.getChildAt(2)as Button).setOnClickListener {
                val keyWords="MC_"+themes[t]+"_words"
                editor.remove(keyWords)
                val keyIsExtended="MC_"+themes[t]+"_isExtended"
                editor.remove(keyIsExtended)
                themes.remove(themes[t])
                val jsonArray =JSONArray(themes)
                editor.putString("MC_theme",jsonArray.toString())
                editor.apply()

                onResume()
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->finish()
            else ->return super.onOptionsItemSelected(item)
        }
        return true
    }


    override fun onBackPressed(){
        /*　ひとつ前の画面に戻る(default)　or　指定のアクティビティを開始
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        */
    }
}
