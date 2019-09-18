package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_brainstorming.*
import org.json.JSONArray
import kotlin.properties.Delegates



class BrainstormingActivity : AppCompatActivity() {
    val handler= Handler()
    var timeValue=0
    var words:ArrayList<String> =arrayListOf()
    //var theme:String by Delegates.notNull()
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
        val theme=intent.getStringExtra("THEME")

        timeValue=min*60+sec
        textView_time.text=timeValue.toString()


        val vg = findViewById<View>(R.id.LinearLayout1) as ViewGroup

        fun start(){
            handler.post(runnable)
        }


        fun stop(){
            println("stop")
            handler.removeCallbacks(runnable)
        }
        runnable= object :Runnable{
            override fun run(){
                time2Text(timeValue).let{
                    if(it=="null") {
                    }
                    else if(it=="00:00:00"){
                        textView_time.text = it
                        timeValue--
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
                        timeValue--
                    }
                }
                handler.postDelayed(this,1000)
            }
        }
        start()

        var isOnce=true
        lateinit var vg2:LinearLayout
        var num=1



        button_add.setOnClickListener {
            if(isNotFinished) {
                println(vg.childCount)
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
                    addCardText.setText("")

                    if(index==5){
                        isOnce=true
                        num++
                    }
                }
            }
        }
        fun save(){
            val gson= Gson()
            val jsonString=gson.toJson(words)
            val keyWords="BS_"+theme+"_words"
            editor.putString(keyWords,jsonString)
            editor.apply()

            if(isFirstSave){
                val themes : ArrayList<String> =arrayListOf()
                val jsonTempArray=JSONArray(dataStore.getString("BS_theme","[]"))
                for(t in 0 .. jsonTempArray.length()-1){
                    themes.add(jsonTempArray.get(t).toString())
                }
                themes.add(theme)
                val jsonArray = JSONArray(themes)
                editor.putString("MC_theme",jsonArray.toString())
                editor.apply()
                isFirstSave=false
            }
        }

        button_save.setOnClickListener {
            println(words)
            save()

        }

        button_load.setOnClickListener {
            val keyWords="BS_"+theme+"_words"
            val jsonArray= JSONArray(dataStore.getString(keyWords,"[]"))
            words.clear()
            for(t in 0 .. jsonArray.length()-1){
                words.add(jsonArray.get(t).toString())
            }
            println(words)
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
        when(item?.itemId){
            android.R.id.home->finish()
            else ->return super.onOptionsItemSelected(item)
        }
        return true
    }
}