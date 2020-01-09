package com.example.ideathonframeworksapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_brain.*
import kotlinx.android.synthetic.main.brainstorming_new_board.*
import kotlin.properties.Delegates

class BrainActivity : AppCompatActivity() {
    lateinit var vg: LinearLayout
    lateinit var vg1: LinearLayout
    var theme:String by Delegates.notNull()
    var isNew:Boolean by Delegates.notNull()
    var timeValue=0
    val handler= Handler()
    val boards:ArrayList<LinearLayout> =arrayListOf()


    var boardNum=0
    var cardNums:ArrayList<Int> =arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="BrainStorming"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        vg = findViewById<View>(R.id.Linear) as LinearLayout

        getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
        val a=vg.getChildAt(boardNum)as ConstraintLayout
        val b=a.getChildAt(1)as LinearLayout
        val c=b.getChildAt(1)as ScrollView
        val vg1=c.getChildAt(0)as LinearLayout
        boards.add(vg1)

        val d=b.getChildAt(2)as LinearLayout
        val text=d.getChildAt(0)as EditText
        text.hint=boardNum.toString()
        val enter=d.getChildAt(1)as ImageButton




        enter.setOnClickListener {
            getLayoutInflater().inflate(R.layout.brainstorming_card, boards[Integer.parseInt(text.hint.toString())])

            cardNums[Integer.parseInt(text.hint.toString())]++

            println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
        }

        boardNum++
        cardNums.add(0)

        /*
        val a=vg.getChildAt(0)as ConstraintLayout
        val d=a.getChildAt(1)as LinearLayout
        val b = d.getChildAt(1)as ScrollView
        val c = b.getChildAt(0)as LinearLayout
        boards.add(c)

        val e=d.getChildAt(2)as LinearLayout
        val text=d.getChildAt(0)as EditText
        val enter=d.getChildAt(1)as ImageButton

        enter.setOnClickListener {
            println("aaaaaaaaaa")
            getLayoutInflater().inflate(R.layout.brainstorming_card, boards[num])
        }

        num++
        */



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


        newBoardButton.setOnClickListener {
            getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
            val a=vg.getChildAt(boardNum)as ConstraintLayout
            val b=a.getChildAt(1)as LinearLayout

            val e=b.getChildAt(0)as TextView
            e.text=genreText.text

            val c=b.getChildAt(1)as ScrollView
            val vg1=c.getChildAt(0)as LinearLayout
            boards.add(vg1)

            val d=b.getChildAt(2)as LinearLayout
            val text=d.getChildAt(0)as EditText
            text.hint=boardNum.toString()
            val enter=d.getChildAt(1)as ImageButton

            enter.setOnClickListener {
                getLayoutInflater().inflate(R.layout.brainstorming_card, boards[Integer.parseInt(text.hint.toString())])
                val tr=boards[Integer.parseInt(text.hint.toString())].getChildAt(cardNums[Integer.parseInt(text.hint.toString())]) as TableRow
                val card =tr.getChildAt(0)as TextView
                println(card.text)
                card.text=text.text
                println(card.text)

                cardNums[Integer.parseInt(text.hint.toString())]++

                println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
            }

            boardNum++
            cardNums.add(0)
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
