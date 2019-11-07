package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_mandala_chart_home.*
import kotlinx.android.synthetic.main.fragment_set_mandala.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SetMandalaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SetMandalaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SetMandalaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val r=inflater.inflate(R.layout.fragment_set_mandala, container, false)

        r.themeText.setOnKeyListener { v, keyCode, event ->
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }


        r.mandalaStart.setOnClickListener {
            if(themeText.length()!=0){
                val intent= Intent(activity,MandalaChartActivity::class.java)
                println(themeText.text)
                intent.putExtra("MC_IS_NEW",true)
                intent.putExtra("MC_THEME_KEY",themeText.text.toString())

                startActivity(intent)
            }
            else{
                themeText.setError("テーマを入力してください")
            }



            /*
            val intent = Intent(activity,MandalaChartHomeActivity::class.java)
            startActivity(intent)
            */
        }


        return r
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }
    */

    override fun onDetach() {
        super.onDetach()
        listener = null
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
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SetMandalaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetMandalaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
