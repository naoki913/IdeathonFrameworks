package com.example.ideathonframeworksapplication

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_mandala_chart.*
import kotlinx.android.synthetic.main.fragment_mandala3x3.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Mandala3x3Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Mandala3x3Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Mandala3x3Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var vg:ViewGroup
    var isChanged:Boolean=false
    var width:Int=0
    var index:Int=0
    var words=Array(9,{""})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val r=inflater.inflate(R.layout.fragment_mandala3x3, container, false)







        r.button5.setOnClickListener {



            getFragmentManager()?.beginTransaction()?.remove(this)?.commit()


        }

        vg = r.findViewById<View>(R.id.Constraint) as ViewGroup

        initBoard()



        // Inflate the layout for this fragment
        return r
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun initBoard(){
        val ac=activity as FragmentActivity

        val scr : ScrollView = ScrollView(ac)
        val hscr : HorizontalScrollView = HorizontalScrollView(ac)
        vg.addView(scr)
        scr.addView(hscr)

        getLayoutInflater().inflate(R.layout.mandala_chart_table, hscr)

        val tl=hscr.getChildAt(0)as TableLayout
        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView
                //val hscv=scv.getChildAt(0)as HorizontalScrollView
                val ed=scv.getChildAt(0)as EditText

                ed.setHeight((DELTA_3x3*width).toInt())
                ed.setWidth((DELTA_3x3*width).toInt())
/*
                if(i==1&&j==1){
                    ed.setText(theme)
                    words[4]= ed.text.toString()
                    ed.setKeyListener(null)
                }
*/

                /*
                ed.setOnKeyListener { v, keyCode, event ->
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        val inputMethodManager = .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                        true
                    } else {
                        false
                    }
                }
                */


                ed.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        words[i*3+j]= ed.text.toString()
                        //println(ed.text)
                        isChanged=true
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")

        val ac=activity as MandalaActivity
        ac.destroyFragment(index,isChanged,words)

    }
    /*
    override fun onDetach() {
        super.onDetach()
        println("onDetach()")
        listener = null
    }
    */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Mandala3x3Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Mandala3x3Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun newInstance(_width:Int,_index:Int,_words:Array<Array<String>>)=
                Mandala3x3Fragment().apply{
                    arguments=Bundle().apply{
                        width=_width
                        index=_index-1
                        words=_words[index]
                    }
                }


        fun newInstance()=
            Mandala3x3Fragment().apply{


        }
    }
}
