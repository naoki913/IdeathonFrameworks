package com.example.ideathonframeworksapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*

class MandalaChartHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="MandalaChart"

        button_new.setOnClickListener{
            if(themeText.length()!=0){
                println(themeText.text)

                val intent= Intent(this,MandalaChartActivity::class.java)

                intent.putExtra("THEME_KEY",themeText.text.toString())

                startActivity(intent)

            }
            else{
                themeText.setError("テーマを入力してください")
                println("0000000")
            }

        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->finish()
            else ->return super.onOptionsItemSelected(item)
        }
        return true
    }
}
