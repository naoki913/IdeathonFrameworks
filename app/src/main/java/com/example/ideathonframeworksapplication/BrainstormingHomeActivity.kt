package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_brainstorming_home.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*
import kotlinx.android.synthetic.main.activity_mandala_chart_home.button_new
import kotlinx.android.synthetic.main.activity_mandala_chart_home.themeText
import org.json.JSONArray

class BrainstormingHomeActivity : AppCompatActivity() {
    var themes : ArrayList<String> = arrayListOf()
    lateinit var dataStore: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var vg:ViewGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brainstorming_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Brainstorming"
        val intent= Intent(this,BrainstormingActivity::class.java)


        dataStore=getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor=dataStore.edit()
        vg = findViewById<View>(R.id.TableLayout) as ViewGroup



        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)
                intent.putExtra("MIN",LimitTimeMinText.text.toString().toInt())
                intent.putExtra("SEC",LimitTimeSecText.text.toString().toInt())
                intent.putExtra("THEME",themeText.text.toString())

                startActivity(intent)
            }
            else{
                themeText.setError("テーマを入力してください")
            }
        }
    }


    override fun onResume(){
        super.onResume()

        val jsonArray = JSONArray(dataStore.getString("BS_theme","[]"))
        for(t in 0 .. jsonArray.length()-1) {
            themes.add(jsonArray.get(t).toString())
            getLayoutInflater().inflate(R.layout.mandala_chart_load_item, vg)

            val tr=vg.getChildAt(t) as TableRow
            val ll=tr.getChildAt(0) as LinearLayout

            (ll.getChildAt(2)as Button).setOnClickListener {
                val keyWords="BS_"+themes[t]+"_words"
                editor.remove(keyWords)
                themes.remove(themes[t])
                val jsonArray =JSONArray(themes)
                editor.putString("BS_theme",jsonArray.toString())
                editor.apply()

                onResume()
            }
        }
    }


}





