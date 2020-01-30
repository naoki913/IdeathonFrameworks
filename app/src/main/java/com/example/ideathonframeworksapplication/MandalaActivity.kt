package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.gson.Gson
import org.json.JSONArray
import kotlin.properties.Delegates

const val DELTA_3x3=0.31944444444
const val DELTA_9x9=0.11111111111

class MandalaActivity : AppCompatActivity() {

    lateinit var vg:ViewGroup
    lateinit var dataStore: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var isNew:Boolean by Delegates.notNull()
    var theme:String by Delegates.notNull()
    var isChanged:Boolean =false
    var width:Int=0
    var words =Array(9,{Array<String>(9,{""})})
    val eds:ArrayList<EditText> =arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala)

        vg = findViewById<View>(R.id.TableLayout1) as ViewGroup
        isNew=intent.getBooleanExtra("MC_IS_NEW",true)
        theme=intent.getStringExtra("MC_THEME_KEY")

        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=theme
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        dataStore =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor = dataStore.edit()

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        width = dm.widthPixels


        when(isNew){
            true->{
                initBoard()
            }
            false->{
                loadBoard()
            }
        }
    }

    override fun onResume(){
        super.onResume()
        callOnResume()
    }

    fun destroyFragment(_index:Int,_isChanged:Boolean,_words:Array<String>){
        if(_isChanged==true){
            isChanged=true
        }
        words[_index]=_words
        updateBoard(_index)

        println("destroyFragment")
    }


    fun callOnResume(){
        println("onResume")
    }

    fun updateBoard(index : Int){
        for(i in(0..2)){
            for(j in(0..2)){
                eds[9*index+3*i+j].setText(words[index][i*3+j])
            }
        }
    }


    fun save(){
        val saveDate:String
        //saveDate=getToday()
        //println(saveDate)


        val gson= Gson()
        var jsonString:String
        jsonString=gson.toJson(words)


        val keyWords="MC_"+theme+"_words"
        //val keyIsExtended="MC_"+theme+"_isExtended"
        editor.putString(keyWords,jsonString)

        val themes:ArrayList<String> = arrayListOf()
        val jsonArray = JSONArray(dataStore.getString("theme","[]"))
        println(jsonArray)
        for(i in 0..jsonArray.length()-1){
            themes.add(jsonArray[i].toString())
        }
        themes.add("MC_"+theme)
        val themesJsonString=gson.toJson(themes)
        editor.putString("theme",themesJsonString)

        editor.apply()
        isChanged=false
    }

    fun initBoard(){
        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val ll1=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=ll1.getChildAt(i)as LinearLayout


            for(j in(0..2)) {
                val fl1=llh.getChildAt(j) as FrameLayout
                val tl = fl1.getChildAt(0) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll2 = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll2.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout
                        val scv = fl1.getChildAt(0) as ScrollView
                        val ed = scv.getChildAt(0) as EditText

                        eds.add(ed)

                        if(i==1&&j==1&&k==1&&l==1){
                            ed.setText(theme)
                        }
                        words[4][4]=theme

                        ed.setOnKeyListener { v, keyCode, event ->
                            println("keyCode:"+keyCode)
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                                true
                            } else {
                                false
                            }
                        }

                        ed.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                words[i*3+j][k * 3 + l] = ed.text.toString()
                                println(ed.text)
                                isChanged=true
                            }
                        })
                    }
                }
            }
        }
    }



    fun loadBoard(){
        val keyWords=theme+"_words"
        val jsonString=dataStore.getString(keyWords,"nothing")
        val gson= Gson()
        words = gson.fromJson(jsonString,Array<Array<String>>::class.java)

        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val ll1=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=ll1.getChildAt(i)as LinearLayout

            for(j in(0..2)) {
                val fl1=llh.getChildAt(j) as FrameLayout
                val tl = fl1.getChildAt(0) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll2 = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll2.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout
                        val scv = fl1.getChildAt(0) as ScrollView
                        //val hscv = scv.getChildAt(0) as HorizontalScrollView
                        val ed = scv.getChildAt(0) as EditText

                        eds.add(ed)

                        ed.setText(words[i*3+j][k*3+l])

                        ed.setOnKeyListener { v, keyCode, event ->
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                                true
                            } else {
                                false
                            }
                        }

                        ed.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                words[i*3+j][k * 3 + l] = ed.text.toString()
                                println(ed.text)
                                isChanged=true
                            }
                        })
                    }
                }
            }
        }
    }

    fun onClick(v:View){
        //println(v.id)
        val text=findViewById<TextView>(v.id)

        val temp:Int=text.hint.toString().toInt()

        val tempIndex:Int=text.hint.toString().toInt()
        val transaction = supportFragmentManager.beginTransaction()
        val frag=Mandala3x3Fragment.newInstance(width,tempIndex,words)
        transaction.add(R.id.Frame,frag)
        transaction.commit()

       }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_brain, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id= item!!.itemId
        if(id==R.id.action_settings){
            println(1)
        }
        else if(id==R.id.action_settings2){
            println(2)
            save()
        }

        return super.onOptionsItemSelected(item)
    }
}

