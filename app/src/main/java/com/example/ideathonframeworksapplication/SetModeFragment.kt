package com.example.ideathonframeworksapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set_mode.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SetModeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SetModeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SetModeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var myContext: FragmentActivity

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
        val r=inflater.inflate(R.layout.fragment_set_mode, container, false)

        val ac=activity as FragmentActivity


        val pager : ViewPager = r.findViewById<ViewPager>(R.id.pager)
        val fragmentManager : FragmentManager = childFragmentManager//ac.supportFragmentManager
        val adapter = SetModePager(fragmentManager)
        pager.adapter=adapter

        val fragmentTranscation =fragmentManager.beginTransaction()


        r.cancelButton.setOnClickListener {
            //fragmentManager.beginTransaction().remove(this).commit()
            fragmentTranscation.remove(this).commit()
        }

        r.createButton.setOnClickListener {
            val mode =r.pager.currentItem

            when(mode) {
                0->{
                    if(r.themeText.length()!=0){
                        val intent= Intent(activity,MandalaChartActivity::class.java)
                        intent.putExtra("MC_IS_NEW",true)
                        intent.putExtra("MC_THEME_KEY",r.themeText.text.toString())

                        startActivity(intent)
                    }
                    else{
                        r.themeText.setError("テーマを入力してください")
                    }

                }
                1->{
                    if(r.themeText.length()!=0){
                        val transaction = childFragmentManager.beginTransaction()
                        val frag=SetTimerFragment.newInstance(r.themeText.text.toString())
                        transaction.add(R.id.root,frag)
                        transaction.commit()


                        /*
                        val intent= Intent(activity,BrainstormingActivity::class.java)
                        intent.putExtra("BS_THEME_KEY",r.themeText.text.toString())
                        intent.putExtra("BS_IS_NEW",true)

                        startActivity(intent)
                        */
                    }
                    else{
                        r.themeText.setError("テーマを入力してください")
                    }
                }


            }
        }




        // Inflate the layout for this fragment
        return r
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
/*
    override fun onAttach(activity:Activity){
        myContext=activity as FragmentActivity
        super.onAttach(activity)

    }
*/
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
         * @return A new instance of fragment SetModeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetModeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        fun newInstance()=SetModeFragment().apply{

        }
    }
}
