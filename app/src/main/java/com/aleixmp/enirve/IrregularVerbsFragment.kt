package com.aleixmp.enirve

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import info.hoang8f.android.segmented.SegmentedGroup

private const val timeDescription =
    "With this option you choose how long is the exercise in minutes, for example if you want to practise for 5 minutes this is your choose"

private const val repetitionsDescription =
    "With this option you choose how many times you want to practise each verb, for example if you want to do each verb 2 times this is your choose"

private const val timeOption = "Select the minutes"
private const val repetitionOption = "Select the number of repetitions"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IrregularVerbsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IrregularVerbsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IrregularVerbsFragment : Fragment(), View.OnClickListener {
    private val mTAG = "IrregularVerbsFragment"

    private var mListener: OnFragmentInteractionListener? = null

    private var mSegmentedGroup: SegmentedGroup? = null
    private var mTxtDurationTypeDescription: TextView? = null
    private var mTxtOptionDescription: TextView? = null
    private var mTxtOptionValue: TextView? = null
    private var mBtnOptionMinus: Button? = null
    private var mBtnOptionAdd: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.title = resources.getString(R.string.app_title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_irregular_verbs, container, false)


        mSegmentedGroup = view.findViewById(R.id.segmented_duration_type)
        mTxtDurationTypeDescription = view.findViewById(R.id.textViewDurationTypeDescription) as TextView
        mTxtOptionDescription = view.findViewById(R.id.txtOptionDescription) as TextView
        mTxtOptionValue= view.findViewById(R.id.txtOptionValue) as TextView
        mBtnOptionMinus = view.findViewById(R.id.btnOptionValueMinus) as Button
        mBtnOptionAdd = view.findViewById(R.id.btnOptionValueAdd) as Button

        setTextTimeOption()

        mSegmentedGroup!!.setOnCheckedChangeListener { _, checkedId: Int ->
            val radioTime: RadioButton = view.findViewById(R.id.segmented_duration_type_time)

            if (radioTime.id == checkedId) {
                Log.d(mTAG, "By time...")
                setTextTimeOption()
            } else {
                Log.d(mTAG, "By number of repetitions...")
                mTxtDurationTypeDescription?.text = repetitionsDescription
                mTxtOptionDescription?.text = repetitionOption
            }

        }

        mBtnOptionMinus?.setOnClickListener(this)
        mBtnOptionAdd?.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        v?.let {
            var value: Int = mTxtOptionValue!!.text.toString().toInt()
            if (it.tag == "minus" && value > 1) {
                value--
            } else if (it.tag == "add") {
                value++
            }
            mTxtOptionValue!!.text = value.toString()
        }
    }

    private fun setTextTimeOption() {
        mTxtDurationTypeDescription?.text = timeDescription
        mTxtOptionDescription?.text = timeOption
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        mListener?.onFragmentInteraction(uri)
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
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
