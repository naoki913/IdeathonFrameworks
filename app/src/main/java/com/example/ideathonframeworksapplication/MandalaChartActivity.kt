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
import android.view.*
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_mandala_chart.*
import org.json.JSONArray
import kotlin.properties.Delegates


class MandalaChartActivity : AppCompatActivity() {

    var words=Array(9,{""})
    var words_9x9 =Array(9,{Array<String>(9,{""})})
    lateinit var vg:ViewGroup
    var isNew:Boolean by Delegates.notNull()
    var isExtended:Boolean by Delegates.notNull()
    lateinit var dataStore: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    var theme:String by Delegates.notNull()
    var isChanged:Boolean =false
    var isFirstSave:Boolean=true
    var scale:Int=0
    var base:Int =0
    val zoom:ArrayList<EditText> =arrayListOf()
    lateinit var mScaleGestureDetector: ScaleGestureDetector
    var mScaleFactor = 2.0f




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart)

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"

        dataStore =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor = dataStore.edit()
        println(editor)

        vg = findViewById<View>(R.id.TableLayout) as ViewGroup
        isNew=intent.getBooleanExtra("MC_IS_NEW",true)
        theme=intent.getStringExtra("MC_THEME_KEY")



        when(isNew){
            true->{
                initBoard()
            }
            false->{
                loadBoard()
            }
        }

        button_up.setOnClickListener {
            println(mScaleFactor)
            /*
            scale=(120+150)
            for (i in (0 .. zoom.size-1)){
                zoom[i].setHeight(scale)
                zoom[i].setWidth(scale)
            }
            */
        }
        button_down.setOnClickListener {

            for(i in(0..zoom.size-1)){
                //zoom[i].setEms(2)
                zoom[i].setHeight(100)
                zoom[i].setWidth(100)
            }
        }

        button_next.setOnClickListener {
            var isFinished=true
            when (isExtended){
                true->{
                    //すでに拡張されている旨の通知

                }
                false->{
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
                        dialog.setNegativeButton("いいえ", null)
                        dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                            // OKボタン押したときの処理
                            extendBoard()
                        })

                        dialog.show()
                    }
                }
            }
        }



        button_save.setOnClickListener {
            save()
        }




        zoomSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    zoomSeekBar:SeekBar,progress:Int,fromUser:Boolean){



                    scale=(base+progress)

                    for (i in (0 .. zoom.size-1)){
                        zoom[i].setHeight(scale)
                        zoom[i].setWidth(scale)
                    }
                    //mScaleFactor=(progress/5).toFloat()
                    println("zoomSeekBar:scale:"+scale)

                }
                override fun onStartTrackingTouch(zommSeekBar:SeekBar){

                }
                override fun onStopTrackingTouch(zoomSeekBar:SeekBar){

                }
            }
        )

    }

    fun save(){
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
        val keyWords="MC_"+theme+"_words"
        editor.putString(keyWords,jsonString)
        Log.d("input",jsonString)
        val keyIsExtended="MC_"+theme+"_isExtended"
        editor.putBoolean(keyIsExtended,isExtended)
        editor.apply()

        isChanged=false

        if(isFirstSave&&isNew){
            //theme追加
            val  themes : ArrayList<String> = arrayListOf()
            val jsonTempArray = JSONArray(dataStore.getString("MC_theme","[]"))
            for(t in 0 .. jsonTempArray.length()-1) {
                themes.add(jsonTempArray.get(t).toString())
            }
            themes.add(theme)
            val jsonArray = JSONArray(themes)
            editor.putString("MC_theme",jsonArray.toString())
            editor.apply()

            isFirstSave=false
        }
    }

    fun initBoard(){
        isExtended=false
        isChanged=true

        val scr : ScrollView = ScrollView(this)
        val hscr : HorizontalScrollView=HorizontalScrollView(this)
        vg.addView(scr)
        scr.addView(hscr)

        getLayoutInflater().inflate(R.layout.mandala_chart_table, hscr)
        zoom.clear()
        base=345
        zoomSeekBar.setProgress(0)
        zoomSeekBar.setMax(325)

        val tl=hscr.getChildAt(0)as TableLayout
        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView
                //val hscv=scv.getChildAt(0)as HorizontalScrollView
                val ed=scv.getChildAt(0)as EditText
                zoom.add(ed)

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
                        isChanged=true
                    }
                })
            }
        }

        scale=base
        for (i in (0 .. zoom.size-1)){
            zoom[i].setHeight(scale)
            zoom[i].setWidth(scale)

        }
    }

    fun loadBoard(){
        val keyIsExtended="MC_"+theme+"_isExtended"
        isExtended=dataStore.getBoolean(keyIsExtended,false)
        println(isExtended)
        zoom.clear()
        when(isExtended){
            true->{
                base=120
                zoomSeekBar.setProgress(0)
                zoomSeekBar.setMax(550)
                loadBoard_9x9()
            }
            false->{
                base=345
                zoomSeekBar.setProgress(0)
                zoomSeekBar.setMax(325)
                loadBoard_3x3()
            }
        }
    }
    fun loadBoard_3x3(){
        val keyWords="MC_"+theme+"_words"
        val jsonString=dataStore.getString(keyWords,"nothing")
        Log.d("log",jsonString)
        val gson= Gson()
        words = gson.fromJson(jsonString,Array<String>::class.java)

        val scr : ScrollView = ScrollView(this)
        val hscr : HorizontalScrollView=HorizontalScrollView(this)
        vg.addView(scr)
        scr.addView(hscr)

        getLayoutInflater().inflate(R.layout.mandala_chart_table, hscr)

        val tl=hscr.getChildAt(0)as TableLayout
        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView
                //val hscv=scv.getChildAt(0)as HorizontalScrollView
                val ed=scv.getChildAt(0)as EditText
                zoom.add(ed)

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
                        isChanged=true
                    }
                })
            }
        }
        scale=base
        for (i in (0 .. zoom.size-1)) {
            zoom[i].setHeight(scale)
            zoom[i].setWidth(scale)
        }



    }
    fun loadBoard_9x9(){
        val keyWords="MC_"+theme+"_words"
        val jsonString=dataStore.getString(keyWords,"nothing")
        val gson= Gson()
        words_9x9 = gson.fromJson(jsonString,Array<Array<String>>::class.java)

        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val ll1=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=ll1.getChildAt(i)as LinearLayout

            for(j in(0..2)) {
                val tl = llh.getChildAt(j) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll2 = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll2.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout
                        val scv = fl1.getChildAt(0) as ScrollView
                        //val hscv = scv.getChildAt(0) as HorizontalScrollView
                        val ed = scv.getChildAt(0) as EditText
                        zoom.add(ed)

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
                                isChanged=true
                            }
                        })
                    }
                }
            }
        }
        scale=base
        for (i in (0 .. zoom.size-1)) {
            zoom[i].setHeight(scale)
            zoom[i].setWidth(scale)
        }

    }

    fun extendBoard(){
        println("OK")
        vg.removeAllViews()
        zoom.clear()
        base=120
        zoomSeekBar.setProgress(0)
        zoomSeekBar.setMax(550)
        getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg)

        val scv=vg.getChildAt(0)as ScrollView
        val hscv=scv.getChildAt(0)as HorizontalScrollView
        val ll1=hscv.getChildAt(0)as LinearLayout

        for(i in(0..2)){
            val llh=ll1.getChildAt(i)as LinearLayout


            for(j in(0..2)) {
                val tl = llh.getChildAt(j) as TableLayout
                val fl = tl.getChildAt(0) as FrameLayout
                val ll2 = fl.getChildAt(1) as LinearLayout

                for (k in (0..2)) {
                    val tr = ll2.getChildAt(k) as TableRow

                    for (l in (0..2)) {
                        val fl1 = tr.getChildAt(l) as FrameLayout

                        val scv = fl1.getChildAt(0) as ScrollView
                        //val hscv = scv.getChildAt(0) as HorizontalScrollView
                        val ed = scv.getChildAt(0) as EditText
                        zoom.add(ed)

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
                                isChanged=true
                            }
                        })
                    }
                }
            }
        }
        scale=base
        for (i in (0 .. zoom.size-1)) {
            zoom[i].setHeight(scale)
            zoom[i].setWidth(scale)
        }
        isExtended=true
        isChanged=true
    }

    fun setScale(){
        for (i in (0 .. zoom.size-1)){
            zoom[i].setHeight(scale)
            zoom[i].setWidth(scale)
        }
        println("setScale:scale:"+scale)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(isChanged==true){
            val dialog = android.support.v7.app.AlertDialog.Builder(this)
            dialog.setTitle("データを保存しますか？")
            dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                // OKボタン押したときの処理
                save()
                finish()
            })
            dialog.setNegativeButton("いいえ", DialogInterface.OnClickListener { _, _->
                finish()
            })
            dialog.show()
        }
        else{
            finish()
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale =Math.max(base,Math.min(670,(scale*mScaleGestureDetector.scaleFactor).toInt()))
            zoomSeekBar.setProgress(scale-base)
            setScale()

            return true
        }
    }

    override fun onBackPressed(){

    }
}
