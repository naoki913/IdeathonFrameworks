package com.example.ideathonframeworksapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_brain.*
import kotlin.properties.Delegates

class BrainActivity : AppCompatActivity() {
    lateinit var vg: LinearLayout
    lateinit var vg1: LinearLayout
    var theme:String by Delegates.notNull()
    var isNew:Boolean by Delegates.notNull()
    var timeValue=0
    val handler= Handler()
    val boards:ArrayList<LinearLayout> =arrayListOf()


    var num=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="BrainStorming"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        vg = findViewById<View>(R.id.Linear) as LinearLayout
        val a=vg.getChildAt(0)as LinearLayout
        val b = a.getChildAt(1)as ScrollView
        val c = b.getChildAt(0)as LinearLayout
        boards.add(c)

        val hour = intent.getIntExtra("HOUR",0)
        val min=intent.getIntExtra("MIN",0)
        val sec=intent.getIntExtra("SEC",0)
        theme=intent.getStringExtra("BS_THEME_KEY")
        isNew=intent.getBooleanExtra("BS_IS_NEW",true)

        timeValue=hour*60*60+min*60+sec

        lateinit var runnable: Runnable
        fun start(){
            handler.post(runnable)
        }

        fun stop(){
            println("stop")
            handler.removeCallbacks(runnable)
        }
        runnable= object :Runnable{
            override fun run() {

                handler.postDelayed(this, 1000)
            }
        }
        start()



        board.setOnClickListener {
            getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
            val a=vg.getChildAt(num)as LinearLayout
            val b=a.getChildAt(1)as ScrollView
            val vg1=b.getChildAt(0)as LinearLayout
            boards.add(vg1)

            num++
        }

        card.setOnClickListener {
            getLayoutInflater().inflate(R.layout.brainstorming_card, boards[0])
        }




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_brain, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id= item!!.itemId
        if(id==R.id.action_settings){
            //println(1)
            for(i in boards){
                //println(i.childCount)
                for(j in (0..i.childCount-1)){
                    val tl=i.getChildAt(j)as TableRow
                    val cb =tl.getChildAt(1)as CheckBox
                    cb.visibility=View.VISIBLE

                }
            }
        }
        else if(id==R.id.action_settings2){
            //println(2)
            for(i in boards){
                //println(i.childCount)
                for(j in (0..i.childCount-1)){
                    val tl=i.getChildAt(j)as TableRow
                    val cb =tl.getChildAt(1)as CheckBox
                    cb.visibility=View.INVISIBLE
                    cb.isChecked=false
                   
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
