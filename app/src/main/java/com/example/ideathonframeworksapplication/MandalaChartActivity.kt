package com.example.ideathonframeworksapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_mandala_chart.*

class MandalaChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_chart)



        var vg1 = findViewById<View>(R.id.TableLayout1) as ViewGroup
       // var vg2 = findViewById<View>(R.id.TableLayout2) as ViewGroup
       // var vg3 = findViewById<View>(R.id.TableLayout3) as ViewGroup
       // button_add.setOnClickListener{

            getLayoutInflater().inflate(R.layout.mandala_chart_table_9x9, vg1)
       //     getLayoutInflater().inflate(R.layout.mandala_chart_table, vg2)
         //   getLayoutInflater().inflate(R.layout.mandala_chart_table, vg3)
          //  println(vg1.childCount)
        //}

       // getLayoutInflater().inflate(R.layout.mandala_chart_table, vg)
    }
}
