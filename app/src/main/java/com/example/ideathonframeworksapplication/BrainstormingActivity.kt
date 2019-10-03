package com.example.ideathonframeworksapplication

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_brainstorming.*
import org.json.JSONArray
import kotlin.properties.Delegates



class BrainstormingActivity : AppCompatActivity() {
    val handler= Handler()
    var theme:String by Delegates.notNull()
    var timeValue=0
    var passedTime=0
    var words:ArrayList<String> =arrayListOf()
    var wordss:ArrayList<Array<String>> = arrayListOf()
    var genres:ArrayList<String> =arrayListOf()
    var bls:ArrayList<LinearLayout> =arrayListOf()
    var chooseIndex=0
    var isNew:Boolean by Delegates.notNull()
    var isChanged:Boolean =false
    var isAlreadyTheme:Boolean = false
    lateinit var dataStore: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var isFirstSave:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brainstorming)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="BrainStorming"

        dataStore=getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor=dataStore.edit()

        lateinit var runnable: Runnable
        var isNotFinished=true

        val min=intent.getIntExtra("MIN",0)
        val sec=intent.getIntExtra("SEC",0)
        theme=intent.getStringExtra("BS_THEME_KEY")
        isNew=intent.getBooleanExtra("MC_IS_NEW",true)

        timeValue=min*60+sec
        textView_time.text=timeValue.toString()


        val vg = findViewById<View>(R.id.BoardParentLayout) as LinearLayout

        /* Multidimensional Array example
        val tempArray:Array<String> = Array(2,{index -> index.toString()})
        wordss.add(tempArray)

        val tempArray2:Array<String> = Array(5,{index -> index.toString()})
        wordss.add(tempArray2)

        println("words:"+wordss[1].size)
        */






        fun start(){
            handler.post(runnable)
        }


        fun stop(){
            println("stop")
            handler.removeCallbacks(runnable)
        }
        runnable= object :Runnable{
            override fun run(){
                time2Text(timeValue-passedTime).let{
                    if(it=="null") {
                    }
                    else if(it=="00:00:00"){
                        textView_time.text = it
                        passedTime++
                        val dialog = android.support.v7.app.AlertDialog.Builder(this@BrainstormingActivity)
                        dialog.setTitle("制限時間になりました")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                            // OKボタン押したときの処理
                        })
                        dialog.show()
                        isNotFinished=false
                        addCardText.setKeyListener(null)
                    }
                    else{
                        textView_time.text = it
                        passedTime++
                    }
                }
                handler.postDelayed(this,1000)
            }
        }
        start()

        addCardText.setOnKeyListener { v, keyCode, event ->
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }

        var isOnce=true
        lateinit var vg2:LinearLayout
        var num=1



        button_add.setOnClickListener {
            if(isNotFinished) {
                //println(vg.childCount)
                if (addCardText.text.toString() != "") {
                    if(isOnce){
                        vg2 = LinearLayout (this)
                        vg2.setOrientation ( LinearLayout.VERTICAL)
                        vg.addView(vg2)
                        isOnce=false
                    }

                    val index = vg2.childCount

                    getLayoutInflater().inflate(R.layout.brainstorming_card, vg2)
                    val tr = vg2.getChildAt(index) as TableRow
                    val text = tr.getChildAt(0) as TextView
                    text.text = addCardText.text
                    words.add(addCardText.text.toString())

                    val tempArray:Array<String> = arrayOf(chooseIndex.toString(),addCardText.text.toString())
                    wordss.add(tempArray)

                    addCardText.setText("")

                    if(index==5){
                        isOnce=true
                        num++
                    }
                }
                isChanged=true
            }

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(vg.windowToken, 0)

        }




        fun loadCard(){//絶賛不具合中

            /*

            val keyTimeValue="BS_"+theme+"_timeValue"
            val keyPassedTime="BS_"+theme+"_passedTime"
            timeValue=dataStore.getInt(keyTimeValue,0)
            passedTime=dataStore.getInt(keyPassedTime,0)


            vg2 = LinearLayout (this)
            vg2.setOrientation ( LinearLayout.VERTICAL)
            vg.addView(vg2)

            val keyWords = "BS_"+theme+"_words"
            val jsonArray = JSONArray(dataStore.getString(keyWords,"[]"))
            println(jsonArray)

            for (t in 0 .. jsonArray.length()-1){
                getLayoutInflater().inflate(R.layout.brainstorming_card, vg2)
                val tr = vg2.getChildAt(t) as TableRow
                val text = tr.getChildAt(0) as TextView
                text.text = jsonArray.get(t).toString()
                words.add(text.text.toString())

            }
            */
        }

        if(isNew){
            isChanged=true
        }
        else{
            loadCard()
            println(words)
        }






        button_save.setOnClickListener {
            /*
            println(words)
            saveManage(false)
            */

            //getLayoutInflater().inflate(R.layout.brainstorming_card,(bls[chooseIndex-1].getChildAt(1)as ScrollView).getChildAt(0)as LinearLayout)

            println("wordss.size:"+wordss.size)
            for(i in(0..wordss.size-1)){
                println(wordss[i][0])
                println(wordss[i][1])
            }

        }


        button_load.setOnClickListener {
           //add board
            getLayoutInflater().inflate(R.layout.brain_storming_board,vg)
            genres.add(genres.size.toString())
            println(genres)

            bls.add(vg.getChildAt(genres.size-1) as LinearLayout)
            val bt=bls[genres.size-1].getChildAt(0)as Button
            val index=genres.size

            bt.text=genres.size.toString()
            bt.setOnClickListener {
                chooseIndex=index
            }
        }
    }

    fun save(){
        val gson= Gson()
        val jsonString=gson.toJson(words)
        val keyWords="BS_"+theme+"_words"
        val keyTimeValue="BS_"+theme+"_timeValue"
        val keyPassedTime="BS_"+theme+"_passedTime"
        editor.putString(keyWords,jsonString)
        editor.putInt(keyTimeValue,timeValue)
        editor.putInt(keyPassedTime,passedTime)
        editor.apply()
        isChanged=false
    }
    fun saveManage(_isFinish:Boolean){
        if(isFirstSave&&isNew){
            //add theme
            val  themes : ArrayList<String> = arrayListOf()
            val jsonTempArray = JSONArray(dataStore.getString("BS_theme","[]"))
            for(t in 0 .. jsonTempArray.length()-1) {
                themes.add(jsonTempArray.get(t).toString())
                if(jsonTempArray.get(t).toString()==theme){
                    isAlreadyTheme=true
                }
            }
            if(isAlreadyTheme){
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("既に同じテーマがあります．上書きしますか？")
                dialog.setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, which ->
                    if(_isFinish){
                        finish()
                    }
                })
                dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    // OKボタン押したときの処理

                    themes.remove(theme)

                    themes.add(theme)
                    val jsonArray = JSONArray(themes)
                    editor.putString("BS_theme",jsonArray.toString())
                    editor.apply()

                    isFirstSave=false

                    save()

                    //どこからsave関数にアクセスしたかで変化
                    if(_isFinish){
                        finish()
                    }
                })
                dialog.show()
            }
            else{
                themes.add(theme)
                val jsonArray = JSONArray(themes)
                editor.putString("BS_theme",jsonArray.toString())
                editor.apply()

                save()

                isFirstSave=false
                if(_isFinish){
                    finish()
                }
            }
        }
        else{
            save()
            if(_isFinish){
                finish()
            }

        }

    }


    private fun time2Text(time:Int =0):String{
        return if(time<0){
            "null"
        }else if(time==0){
            "00:00:00"
        }else {
            val h = time / 3600
            val m= time%3600 /60
            val s = time %60
            "%1$02d:%2$02d:%3$02d".format(h,m,s)
        }
    }

    override fun onBackPressed(){

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(isChanged==true){
            val dialog = android.support.v7.app.AlertDialog.Builder(this)
            dialog.setTitle("データを保存しますか？")
            dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                // OKボタン押したときの処理
                saveManage(true)
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
}