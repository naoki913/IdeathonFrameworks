package com.example.ideathonframeworksapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_mandala_chart.*


class MandalaChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart)
        val words=Array(9,{""})
        val words_9x9 =Array(9,{Array<String>(9,{""})})
        val theme=intent.getStringExtra("THEME_KEY")
        println(theme)

        val vg = findViewById<View>(R.id.TableLayout) as ViewGroup
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
                        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    }
                })
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
            println("a")
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














                })
                dialog.setNegativeButton("いいえ", null)
                dialog.show()
            }
        }

    }
}
