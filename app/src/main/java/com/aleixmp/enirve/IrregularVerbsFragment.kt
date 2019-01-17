package com.aleixmp.enirve

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import info.hoang8f.android.segmented.SegmentedGroup


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val timeDescription =
    "With this option you choose how long is the exercise in minutes, for example if you want to practise for 5 minutes this is your choose"

private const val repetitionsDescription =
    "With this option you choose how many times you want to practise each verb, for example if you want to do each verb 2 times this is your choose"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IrregularVerbsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IrregularVerbsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IrregularVerbsFragment : Fragment() {
    private val TAG = "IrregularVerbsFragment"


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var segmentedGroup: SegmentedGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_irregular_verbs, container, false)
        val txtDurationTypeDescription: TextView

        segmentedGroup = view.findViewById(R.id.segmented_duration_type)
        txtDurationTypeDescription = view.findViewById(R.id.textViewDurationTypeDescription) as TextView
        txtDurationTypeDescription.text = timeDescription

        segmentedGroup!!.setOnCheckedChangeListener { group, checkedId: Int ->
            val radioTime: RadioButton = view.findViewById(R.id.segmented_duration_type_time)

            if (radioTime.id == checkedId) {
                Log.d(TAG, "By time...")
                txtDurationTypeDescription.text = timeDescription
            } else {
                Log.d(TAG, "By number of repetitions...")
                txtDurationTypeDescription.text = repetitionsDescription
            }

        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

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
         * @return A new instance of fragment IrregularVerbsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            IrregularVerbsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
