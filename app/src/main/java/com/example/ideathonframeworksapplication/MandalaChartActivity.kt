package com.example.ideathonframeworksapplication

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_mandala_chart.*
import kotlin.properties.Delegates


class MandalaChartActivity : AppCompatActivity() {

    var words=Array(9,{""})
    var words_9x9 =Array(9,{Array<String>(9,{""})})
    lateinit var vg:ViewGroup
    var isNew:Boolean by Delegates.notNull()
    var isExtended:Boolean by Delegates.notNull()
    lateinit var dataStore: SharedPreferences
    var theme:String by Delegates.notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"

        dataStore =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val editor = dataStore.edit()
        println(editor)


        vg = findViewById<View>(R.id.TableLayout) as ViewGroup
        isNew=intent.getBooleanExtra("IS_NEW",true)
        theme=intent.getStringExtra("THEME_KEY")
        editor.putString("THEME",theme)
        editor.apply()

        when(isNew){
            true->{

                initBoard()
            }
            false->{

                loadBoard()
            }

        }


        button_check.setOnClickListener {
            for (k in (0..8)) {
                for (i in (0..2)) {
                    for (j in (0..2)) {
                        println("k="+k+",i="+i+",j="+j+","+words_9x9[k][i * 3 + j])
                    }
                }
            }
        }



        button_next.setOnClickListener {
            var isFinished=true

            for(i in(0..2)){
                for(j in(0..2)){
                    if(words[i*3+j].length==0){
                        isFinished=false
                    }
                }
            }

            if(isFinished){
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("チャートを拡大しますか")
                dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    // OKボタン押したときの処理
                    extendBoard()
                })
                dialog.setNegativeButton("いいえ", null)
                dialog.show()
            }
        }

        button_save.setOnClickListener {
            val gson= Gson()
            var jsonString:String
            when(isExtended){
                true->{
                    jsonString=gson.toJson(words_9x9)
                }
                false->{
                    jsonString=gson.toJson(words)
                }
            }
            editor.putString("words",jsonString)
            Log.d("input",jsonString)
            editor.putBoolean("isExtended",isExtended)
            editor.apply()
        }

    }

    fun initBoard(){
        isExtended=false

        getLayoutInflater().inflate(R.layout.mandala_chart_table, vg)

        val tl=vg.getChildAt(0)as TableLayout
        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView
                val hscv=scv.getChildAt(0)as HorizontalScrollView
                val ed=hscv.getChildAt(0)as EditText

                if(i==1&&j==1){
                    ed.setText(theme)
                    words[4]= ed.text.toString()
                    ed.setKeyListener(null)
                }

                ed.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        words[i*3+j]= ed.text.toString()
                        println(ed.text)

                    }
                })
            }
        }
    }

    fun loadBoard(){

        isExtended=dataStore.getBoolean("isExtended",false)
        println(isExtended)
        when(isExtended){
            true->{
                loadBoard_9x9()
            }
            false->{
                loadBoard_3x3()
            }
        }
    }
    fun loadBoard_3x3(){
        val jsonString=dataStore.getString("words","nothing")
        Log.d("log",jsonString)
        val gson= Gson()
        words = gson.fromJson(jsonString,Array<String>::class.java)


        getLayoutInflater().inflate(R.layout.mandala_chart_table, vg)

        val tl=vg.getChildAt(0)as TableLayout
        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView
                val hscv=scv.getChildAt(0)as HorizontalScrollView
                val ed=hscv.getChildAt(0)as EditText

                ed.setText(words[i*3+j])
                if(i==1&&j==1){
                    ed.setKeyListener(null)
                }

                ed.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        words[i*3+j]= ed.text.toString()
                        println(ed.text)
                        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    }
                })
            }
        }
    }
    fun loadBoard_9x9(){
        val jsonString=dataStore.getString("words","nothing")
        Log.d("log",jsonString)
        val gson= Gson()
        words_9x9 = gson.fromJson(jsonString,Array<Array<String>>::class.java)

        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val llv=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=llv.getChildAt(i)as LinearLayout

            for(j in(0..2)) {
                val tl = llh.getChildAt(j) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout
                        val scv = fl1.getChildAt(0) as ScrollView
                        val hscv = scv.getChildAt(0) as HorizontalScrollView
                        val ed = hscv.getChildAt(0) as EditText

                        ed.setText(words_9x9[i*3+j][k*3+l])
                        if (k == 1 && l == 1) {
                            ed.setKeyListener(null)
                        }

                        ed.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                words_9x9[i*3+j][k * 3 + l] = ed.text.toString()
                                println(ed.text)
                            }
                        })
                    }
                }
            }
        }

    }

    fun extendBoard(){
        println("OK")
        vg.removeAllViews()
        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val llv=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=llv.getChildAt(i)as LinearLayout

            for(j in(0..2)) {
                val tl = llh.getChildAt(j) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout
                        val scv = fl1.getChildAt(0) as ScrollView
                        val hscv = scv.getChildAt(0) as HorizontalScrollView
                        val ed = hscv.getChildAt(0) as EditText

                        if (k == 1 && l == 1) {
                            ed.setText(words[i*3+j])
                            words_9x9[i*3+j][k*3+l] = ed.text.toString()
                            ed.setKeyListener(null)
                        }

                        ed.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                words_9x9[i*3+j][k * 3 + l] = ed.text.toString()
                                println(ed.text)
                            }
                        })
                    }
                }
            }
        }
        isExtended=true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->finish()
            else ->return super.onOptionsItemSelected(item)
        }
        return true
    }
}
