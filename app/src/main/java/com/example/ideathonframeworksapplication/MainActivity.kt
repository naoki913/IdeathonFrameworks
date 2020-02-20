package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    var themes : ArrayList<String> = arrayListOf()
    lateinit var dataStore: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var vg:ViewGroup
    lateinit var vg1:ViewGroup
    lateinit var vg2:ViewGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar?.hide()
        val toolbar =findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="アイデア一覧"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        dataStore=getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        editor=dataStore.edit()
        vg = findViewById<View>(R.id.TableLayout1) as ViewGroup
        vg1 = findViewById<View>(R.id.TableLayout1) as ViewGroup
        vg2 = findViewById<View>(R.id.TableLayout2) as ViewGroup

        val transaction = supportFragmentManager.beginTransaction()



    }
    override fun onResume(){
        super.onResume()

        /*
        editor.putString("theme","[]")
        editor.apply()
        */

        val jsonArray = JSONArray(dataStore.getString("theme","[]"))
        vg1.removeAllViews()
        vg2.removeAllViews()
        themes.clear()

        vg=vg1
        getLayoutInflater().inflate(R.layout.new_item, vg)

        val cl =vg.getChildAt(0)as ConstraintLayout
        val fl=cl.getChildAt(0)as FrameLayout
        val new_button=fl.getChildAt(2)as ImageButton

        new_button.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            val frag=SetModeFragment.newInstance()
            transaction.add(R.id.Constraint,frag)
            transaction.commit()
        }

        for(t in 0 .. jsonArray.length()-1) {
            themes.add(jsonArray.get(t).toString())

            if((t+1)%2==0){
                vg=vg1
            }
            else{
                vg=vg2
            }
            println("count:"+vg.childCount)

            if(themes[t].substring(0,2)=="BS"){
                val intent= Intent(this,BrainActivity::class.java)
                getLayoutInflater().inflate(R.layout.brain_storming_load_item, vg)



                val cl =vg.getChildAt((t+1)/2)as ConstraintLayout
                val fl=cl.getChildAt(0)as FrameLayout
                val cl2=fl.getChildAt(1)as ConstraintLayout
                val tv=cl2.getChildAt(1)as TextView

                tv.setText(themes[t].substring(3))

                val load_button=fl.getChildAt(2)as ImageButton
                load_button.setOnClickListener {
                    intent.putExtra("BS_IS_NEW",false)
                    intent.putExtra("BS_THEME_KEY",themes[t])
                    startActivity(intent)
                }

            }
            else if(themes[t].substring(0,2)=="MC"){
                val intent= Intent(this,MandalaActivity::class.java)
                getLayoutInflater().inflate(R.layout.mandala_chart_load_item, vg)

                val cl =vg.getChildAt((t+1)/2)as ConstraintLayout
                val fl=cl.getChildAt(0)as FrameLayout
                val cl2=fl.getChildAt(1)as ConstraintLayout
                val tv=cl2.getChildAt(1)as TextView

                tv.setText(themes[t].substring(3))

                val load_button=fl.getChildAt(2)as ImageButton
                load_button.setOnClickListener {
                    intent.putExtra("MC_IS_NEW",false)
                    intent.putExtra("MC_THEME_KEY",themes[t])
                    startActivity(intent)
                }
            }
        }

        println("themes:"+themes)

    }

    override fun onBackPressed(){
        super.onBackPressed()
    }
}


