package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_brain.*
import kotlinx.android.synthetic.main.activity_brainstorming.*
import kotlinx.android.synthetic.main.brainstorming_new_board.*
import org.json.JSONArray
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
    lateinit var dataStore: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    val boards:ArrayList<LinearLayout> =arrayListOf()
    val inputText:ArrayList<EditText> = arrayListOf()

    var boardNum=0
    var cardNums:ArrayList<Int> =arrayListOf()
    val cardWords:ArrayList<ArrayList<String>> = arrayListOf()
    val genreList:ArrayList<String> = arrayListOf()
    val deleteView:ArrayList<TableRow> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        val hour = intent.getIntExtra("HOUR",0)
        val min=intent.getIntExtra("MIN",0)
        val sec=intent.getIntExtra("SEC",0)
        theme=intent.getStringExtra("BS_THEME_KEY")
        isNew=intent.getBooleanExtra("BS_IS_NEW",true)
        isSetTime=intent.getBooleanExtra("BS_IS_SET_TIME",false)


        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=theme
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        dataStore =getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor = dataStore.edit()

        vg = findViewById<View>(R.id.Linear) as LinearLayout

        inputText.add(genreText)




        timeValue=hour*60*60+min*60+sec

        if(isSetTime){
            timeText.text=time2Text(timeValue)
        }
        else{
            timeText.text="無制限"
        }

        if(isNew){
            //初期ボード設置
            getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
            val a=vg.getChildAt(boardNum)as ConstraintLayout
            val b=a.getChildAt(1)as LinearLayout

            val e=b.getChildAt(0)as TextView

            genreList.add(e.text.toString())

            val c=b.getChildAt(1)as ScrollView
            val vg1=c.getChildAt(0)as LinearLayout
            boards.add(vg1)
            cardWords.add(arrayListOf())

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
                    cardWords[Integer.parseInt(text.hint.toString())].add(card.text.toString())


                    println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
                }
            }

            boardNum++
            cardNums.add(0)

            //初期ボード設置ここまで
        }
        else{
            load()
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
                if(isSetTime){
                    time2Text(timeValue - passedTime).let {
                        if (it == "null") {
                        }
                        else if(it=="00:00:00"){
                            timeText.text = it
                            passedTime++
                            for(i in inputText){
                                i.setKeyListener(null)
                            }


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
                }

                handler.postDelayed(this, 1000)
            }
        }
        start()

        deleteButton.setOnClickListener {
            for(i in (0..boards.size-1)){
                for(j in(0..boards[i].childCount-1)){
                    val tr1=boards[i].getChildAt(j)as TableRow
                    val card1=tr1.getChildAt(0)as TextView
                    val check=tr1.getChildAt(1)as CheckBox

                    if(check.isChecked){
                        deleteView.add(tr1)
                    }
                }
                for(j in deleteView){
                    boards[i].removeView(j)
                    cardNums[i]--
                }
                deleteView.clear()
            }
        }

        cancelButton.setOnClickListener{
            println("cancel")

            for(i in boards){
                //println(i.childCount)
                for(j in (0..i.childCount-1)){
                    val tl=i.getChildAt(j)as TableRow
                    val cb =tl.getChildAt(1)as CheckBox
                    cb.visibility=View.INVISIBLE
                    cb.isChecked=false

                }
            }

            EditBar.visibility=View.INVISIBLE

        }

        moveButton.setOnClickListener {
            println("move")

            val strList = arrayOfNulls<String>(genreList.size)
            genreList.toArray(strList)
            var selectedItam=0

            AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("移動先のボードを選択してください")
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

                    for(i in boards){
                        for(j in (0..i.childCount-1)){
                            val tl=i.getChildAt(j)as TableRow
                            val cb =tl.getChildAt(1)as CheckBox
                            cb.visibility=View.VISIBLE
                        }
                    }
                })
                .setNegativeButton("キャンセル",{dialog,which ->

                })
                .show()
        }


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
                cardWords.add(arrayListOf())

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
                        cardWords[Integer.parseInt(text.hint.toString())].add(card.text.toString())

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
            //ロード処理
            load()

            //以下正常コード（編集）
            /*
            for(i in boards){
                for(j in (0..i.childCount-1)){
                    val tl=i.getChildAt(j)as TableRow
                    val cb =tl.getChildAt(1)as CheckBox
                    cb.visibility=View.VISIBLE
                }
            }

            println(genreList.size)

            EditBar.visibility=View.VISIBLE
            */
        }

        else if(id==R.id.action_settings2){
           save()


        }


        return super.onOptionsItemSelected(item)
    }

    fun save(){
        val gson= Gson()
        var jsonString:String

        val keyCardWords="BS_"+theme+"_cardWords"
        jsonString=gson.toJson(cardWords)
        editor.putString(keyCardWords,jsonString)

        val keyGenreList="BS_"+theme+"_genreList"
        jsonString=gson.toJson(genreList)
        editor.putString(keyGenreList,jsonString)

        val themes:ArrayList<String> = arrayListOf()
        val jsonArray = JSONArray(dataStore.getString("theme","[]"))
        println(jsonArray)
        for(i in 0..jsonArray.length()-1){
            themes.add(jsonArray[i].toString())
        }
        themes.add("BS_"+theme)
        val themesJsonString=gson.toJson(themes)
        editor.putString("theme",themesJsonString)

        editor.apply()
    }

    fun load(){
        //ロード処理
        //セーブデータの取得
        val gson= Gson()
        var jsonString:String=""

        val keyCardWords=theme+"_cardWords"
        jsonString=dataStore.getString(keyCardWords,"nothing")
        val tempCardWords = gson.fromJson(jsonString,Array<Array<String>>::class.java)

        cardWords.clear()
        for(i in 0..tempCardWords.size-1){
            cardWords.add(arrayListOf())
            for(j in 0..tempCardWords[i].size-1){
                cardWords[i].add(tempCardWords[i][j])
            }
        }

        val keyGenreList = theme+"_genreList"
        jsonString=dataStore.getString(keyGenreList,"nothing")
        val tempGenreList=gson.fromJson(jsonString,Array<String>::class.java)

        genreList.clear()
        for(i in tempGenreList){
            genreList.add(i)
        }


        for(i in 0..cardWords.size-1){

            getLayoutInflater().inflate(R.layout.brain_storming_board, vg)
            val a=vg.getChildAt(boardNum)as ConstraintLayout
            val b=a.getChildAt(1)as LinearLayout

            val e=b.getChildAt(0)as TextView
            e.text=genreList[i]

            val c=b.getChildAt(1)as ScrollView
            val vg1=c.getChildAt(0)as LinearLayout
            boards.add(vg1)
            cardWords.add(arrayListOf())

            val d=b.getChildAt(2)as LinearLayout
            val text=d.getChildAt(0)as EditText
            inputText.add(text)
            text.hint=boardNum.toString()
            val enter=d.getChildAt(1)as ImageButton

            boardNum++
            cardNums.add(0)

            for(j in 0..cardWords[i].size-1){
                getLayoutInflater().inflate(R.layout.brainstorming_card, boards[Integer.parseInt(text.hint.toString())])

                println(boards[Integer.parseInt(text.hint.toString())])
                println(cardNums[Integer.parseInt(text.hint.toString())])

                val tr=boards[Integer.parseInt(text.hint.toString())].getChildAt(cardNums[Integer.parseInt(text.hint.toString())]) as TableRow
                val card =tr.getChildAt(0)as TextView
                println(card.text)
                card.text=cardWords[i][j]

                println(card.text)

                cardNums[Integer.parseInt(text.hint.toString())]++
                cardWords[Integer.parseInt(text.hint.toString())].add(card.text.toString())

                println("boardNum:"+Integer.parseInt(text.hint.toString())+"num:"+cardNums[Integer.parseInt(text.hint.toString())])
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
