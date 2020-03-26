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
import android.opengl.ETC1.getWidth
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap






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
    var height:Int=0
    var index:Int=0
    var words=Array(9,{""})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val fl1=vg.getChildAt(0)as FrameLayout

        getLayoutInflater().inflate(R.layout.mandala_chart_table_3x3, fl1)

        val tl=fl1.getChildAt(0)as TableLayout

        val fl=tl.getChildAt(0)as FrameLayout
        val ll=fl.getChildAt(1)as LinearLayout

        val iv=fl.getChildAt(0)as ImageView

        for(i in(0..2)){
            val tr=ll.getChildAt(i)as TableRow

            for(j in(0..2)){
                val fl1=tr.getChildAt(j)as FrameLayout
                val scv=fl1.getChildAt(0)as ScrollView

                val ed=scv.getChildAt(0)as EditText

                ed.setHeight((DELTA_3x3*width).toInt())
                ed.setWidth((DELTA_3x3*width).toInt())
                ed.setText(words[i*3+j])

                if(i==1&&j==1){

                    ed.setKeyListener(null)
                }

                ed.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        words[i*3+j]= ed.text.toString()
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

        fun newInstance(_width:Int/*,_height:Int*/,_index:Int,_words:Array<Array<String>>)=
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
