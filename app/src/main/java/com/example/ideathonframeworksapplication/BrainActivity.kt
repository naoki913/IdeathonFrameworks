package com.example.ideathonframeworksapplication

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_brain.*
import kotlinx.android.synthetic.main.activity_brainstorming.*
import kotlinx.android.synthetic.main.brainstorming_new_board.*
import kotlin.properties.Delegates

class BrainActivity : AppCompatActivity() {
    lateinit var vg: LinearLayout
    lateinit var vg1: LinearLayout
    var theme:String by Delegates.notNull()
    var isNew:Boolean by Delegates.notNull()
    var isSetTime:Boolean by Delegates.notNull()
    var timeValue=0
    var passedTime=0
    var isFinished=false
    val handler= Handler()
    val boards:ArrayList<LinearLayout> =arrayListOf()
    val inputText:ArrayList<EditText> = arrayListOf()

    var boardNum=0
    var cardNums:ArrayList<Int> =arrayListOf()
    val genreList:ArrayList<String> = arrayListOf()
    val deleteView:ArrayList<TableRow> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="BrainStorming"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        vg = findViewById<View>(R.id.Linear) as LinearLayout

        inputText.add(genreText)

        getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
        val a=vg.getChildAt(boardNum)as ConstraintLayout
        val b=a.getChildAt(1)as LinearLayout

        val e=b.getChildAt(0)as TextView

        genreList.add(e.text.toString())

        val c=b.getChildAt(1)as ScrollView
        val vg1=c.getChildAt(0)as LinearLayout
        boards.add(vg1)

        val d=b.getChildAt(2)as LinearLayout
        val text=d.getChildAt(0)as EditText
        inputText.add(text)
        text.hint=boardNum.toString()
        val enter=d.getChildAt(1)as ImageButton



        enter.setOnClickListener {
            if(!isFinished){
                getLayoutInflater().inflate(R.layout.brainstorming_card, boards[Integer.parseInt(text.hint.toString())])
                val tr=boards[Integer.parseInt(text.hint.toString())].getChildAt(cardNums[Integer.parseInt(text.hint.toString())]) as TableRow
                val card =tr.getChildAt(0)as TextView
                println(card.text)
                card.text=text.text

                println(card.text)

                cardNums[Integer.parseInt(text.hint.toString())]++

                println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
            }
        }

        boardNum++
        cardNums.add(0)


        val hour = intent.getIntExtra("HOUR",0)
        val min=intent.getIntExtra("MIN",0)
        val sec=intent.getIntExtra("SEC",0)
        theme=intent.getStringExtra("BS_THEME_KEY")
        isNew=intent.getBooleanExtra("BS_IS_NEW",true)
        isSetTime=intent.getBooleanExtra("BS_IS_SET_TIME",false)

        timeValue=hour*60*60+min*60+sec

        if(isSetTime){
            timeText.text=time2Text(timeValue)
        }
        else{
            timeText.text="無制限"
        }



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
                time2Text(timeValue - passedTime).let {
                    if (it == "null") {
                    }
                    else if(it=="00:00:00"){
                        timeText.text = it
                        passedTime++
                        val dialog = android.support.v7.app.AlertDialog.Builder(this@BrainActivity)
                        dialog.setTitle("制限時間になりました")
                        println(inputText.size)
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                            // OKボタン押したときの処理
                        })
                        dialog.show()
                        isFinished=true
                        //addCardText.setKeyListener(null)
                    }
                    else {
                        timeText.text = it
                        passedTime++
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }
        start()


        newBoardButton.setOnClickListener {
            if(!isFinished){
                getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
                val a=vg.getChildAt(boardNum)as ConstraintLayout
                val b=a.getChildAt(1)as LinearLayout

                val e=b.getChildAt(0)as TextView
                e.text=genreText.text
                genreList.add(genreText.text.toString())
                genreText.setText("")

                val c=b.getChildAt(1)as ScrollView
                val vg1=c.getChildAt(0)as LinearLayout
                boards.add(vg1)

                val d=b.getChildAt(2)as LinearLayout
                val text=d.getChildAt(0)as EditText
                inputText.add(text)
                text.hint=boardNum.toString()
                val enter=d.getChildAt(1)as ImageButton

                enter.setOnClickListener {
                    if(!isFinished){
                        getLayoutInflater().inflate(R.layout.brainstorming_card, boards[Integer.parseInt(text.hint.toString())])
                        val tr=boards[Integer.parseInt(text.hint.toString())].getChildAt(cardNums[Integer.parseInt(text.hint.toString())]) as TableRow
                        val card =tr.getChildAt(0)as TextView
                        println(card.text)
                        card.text=text.text

                        println(card.text)

                        cardNums[Integer.parseInt(text.hint.toString())]++

                        println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
                    }
                }

                boardNum++
                cardNums.add(0)
            }

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

            println(genreList.size)
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

        else if(id==R.id.action_settings3){
            val strList = arrayOfNulls<String>(genreList.size)
            genreList.toArray(strList)
            var selectedItam=0

            AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("ラジオボタン選択ダイアログ")
                .setSingleChoiceItems(strList, 0, { dialog, which ->
                    //アイテム選択時の挙動
                    selectedItam=which
                })
                .setPositiveButton("OK", { dialog, which ->
                    println("size:"+boards.size)
                    for(i in (0..boards.size-1)){
                        for(j in(0..boards[i].childCount-1)){
                            //カードのチェックボックスを確認→ONなら選択されたボードにそのカードを追加し、元のボードからカードを削除する
                            val tr1=boards[i].getChildAt(j)as TableRow
                            val card1=tr1.getChildAt(0)as TextView
                            val check=tr1.getChildAt(1)as CheckBox

                            if(check.isChecked){

                                boards[selectedItam]

                                if(!(i==selectedItam)){
                                    getLayoutInflater().inflate(R.layout.brainstorming_card, boards[selectedItam])
                                    val tr2=boards[selectedItam].getChildAt(cardNums[selectedItam]) as TableRow
                                    val card2 =tr2.getChildAt(0)as TextView
                                    card2.text=card1.text
                                    cardNums[selectedItam]++
                                    deleteView.add(tr1)
                                }
                            }
                        }

                        for(j in deleteView){
                            boards[i].removeView(j)
                            cardNums[i]--
                        }
                        deleteView.clear()

                    }
                })
                .show()
        }
        return super.onOptionsItemSelected(item)
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
