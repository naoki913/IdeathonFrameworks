package com.example.ideathonframeworksapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set_mode.view.*
import kotlinx.android.synthetic.main.fragment_set_timer.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SetTimerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SetTimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SetTimerFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val r=inflater.inflate(R.layout.fragment_set_timer, container, false)




        r.HourPicker.maxValue=59
        r.HourPicker.minValue=0
        r.MinutePicker.maxValue=59
        r.MinutePicker.minValue=0
        r.SecondPicker.maxValue=59
        r.SecondPicker.minValue=0

        r.noSet.setOnClickListener {
            val intent= Intent(activity,BrainActivity::class.java)
            intent.putExtra("BS_THEME_KEY",param1)
            intent.putExtra("BS_IS_NEW",true)
            intent.putExtra("BS_IS_SET_TIME",false)

            startActivity(intent)
        }

        r.timeSet.setOnClickListener {
            val intent= Intent(activity,BrainActivity::class.java)
            intent.putExtra("HOUR",r.HourPicker.value)
            intent.putExtra("MIN",r.MinutePicker.value)
            intent.putExtra("SEC",r.SecondPicker.value)
            intent.putExtra("BS_THEME_KEY",param1)
            intent.putExtra("BS_IS_NEW",true)
            intent.putExtra("BS_IS_SET_TIME",true)

            startActivity(intent)
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
         * @return A new instance of fragment SetTimerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetTimerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun newInstance(param1: String) =
            SetTimerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }

        fun newInstance()=SetTimerFragment().apply{

        }
    }
}
