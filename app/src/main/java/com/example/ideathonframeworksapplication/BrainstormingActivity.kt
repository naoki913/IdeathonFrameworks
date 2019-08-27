package com.example.ideathonframeworksapplication

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_brainstorming.*

class BrainstormingActivity : AppCompatActivity() {
    val handler= Handler()
    var timeValue=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brainstorming)

        lateinit var runnable: Runnable
        var isNotFinished=true

        val min=intent.getIntExtra("MIN",2)
        val sec=intent.getIntExtra("SEC",0)

        timeValue=min*60+sec
        textView_time.text=timeValue.toString()



        val vg = findViewById<View>(R.id.LinearLayout) as ViewGroup

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
        lateinit var vg2:ViewGroup
        var num=1

        button_add.setOnClickListener {
            if(isNotFinished) {
                println(vg.childCount)

                if (addCardText.text.toString() != "") {
                    if(isOnce){
                        getLayoutInflater().inflate(R.layout.vertical_layout, vg)
                        vg2=vg.getChildAt(num) as ViewGroup
                        isOnce=false
                    }

                    val index = vg2.childCount
                    getLayoutInflater().inflate(R.layout.brainstorming_card, vg2)
                    val tr = vg2.getChildAt(index) as TableRow
                    val text = tr.getChildAt(0) as TextView
                    text.text = addCardText.text
                    addCardText.setText("")

                    if(index==5){
                        isOnce=true
                        num++
                    }



                }

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
}
