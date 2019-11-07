package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    var themes : ArrayList<String> = arrayListOf()
    lateinit var dataStore: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var vg:ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        dataStore=getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor=dataStore.edit()
        vg = findViewById<View>(R.id.TableLayout) as ViewGroup

        val transaction = supportFragmentManager.beginTransaction()
        lateinit var frag : Fragment

        button_mandala.setOnClickListener {
            val pager : ViewPager = findViewById<ViewPager>(R.id.pager)
            val fragmentManager : FragmentManager = supportFragmentManager
            val adapter = SetModePager(fragmentManager)
            pager.adapter=adapter




            /*
            if(savedInstanceState==null){
                println("A")
                //val transaction = supportFragmentManager.beginTransaction()
                frag=SetModeFragment.createInstance(this)
                transaction.add(R.id.Constraint,frag)
                transaction.commit()
            }
            */



            /*
            val intent = Intent(this,MandalaChartHomeActivity::class.java)
            startActivity(intent)
            */
        }

        button_bs.setOnClickListener {
            println("B")
            /*
            transaction.remove(frag)
            transaction.commit()
            */




            val intent = Intent(this,BrainstormingHomeActivity::class.java)
            startActivity(intent)

        }
    }
    override fun onResume(){
        super.onResume()

        val jsonArray = JSONArray(dataStore.getString("theme","[]"))
        vg.removeAllViews()
        themes.clear()

        for(t in 0 .. jsonArray.length()-1) {
            themes.add(jsonArray.get(t).toString())

            if(themes[t].substring(0,2)=="BS"){
                val intent= Intent(this,BrainstormingActivity::class.java)
                getLayoutInflater().inflate(R.layout.brain_storming_load_item, vg)

                val tr=vg.getChildAt(t) as TableRow
                val ll=tr.getChildAt(0) as LinearLayout

                (ll.getChildAt(0)as Button).setText(themes[t].substring(3))
                (ll.getChildAt(0)as Button).setOnClickListener {
                    intent.putExtra("BS_IS_NEW",false)
                    intent.putExtra("BS_THEME_KEY",themes[t])
                    startActivity(intent)
                    //println(themes[t])
                }

                (ll.getChildAt(1)as TextView).setText(themes[t]+"に関する説明")

                (ll.getChildAt(2)as Button).setOnClickListener {
                    val keyWords=themes[t]+"_words"
                    editor.remove(keyWords)
                    themes.remove(themes[t])
                    val jsonArray =JSONArray(themes)
                    editor.putString("theme",jsonArray.toString())
                    editor.apply()

                    onResume()
                }
            }
            else if(themes[t].substring(0,2)=="MC"){
                val intent= Intent(this,MandalaChartActivity::class.java)
                getLayoutInflater().inflate(R.layout.mandala_chart_load_item, vg)

                val tr=vg.getChildAt(t) as TableRow
                val ll=tr.getChildAt(0) as LinearLayout

                (ll.getChildAt(0)as Button).setText(themes[t].substring(3))
                (ll.getChildAt(0)as Button).setOnClickListener {
                    intent.putExtra("MC_IS_NEW",false)
                    intent.putExtra("MC_THEME_KEY",themes[t])
                    startActivity(intent)
                }

                (ll.getChildAt(1)as TextView).setText(themes[t]+"に関する説明")

                (ll.getChildAt(2)as Button).setOnClickListener {
                    val keyWords=themes[t]+"_words"
                    editor.remove(keyWords)
                    val keyIsExtended=themes[t]+"_isExtended"
                    editor.remove(keyIsExtended)
                    themes.remove(themes[t])
                    val jsonArray =JSONArray(themes)
                    editor.putString("theme",jsonArray.toString())
                    editor.apply()

                    onResume()
                }
            }


        }
        println("themes:"+themes)

    }


    override fun onBackPressed(){
        super.onBackPressed()
    }
}
