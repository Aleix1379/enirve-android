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

private const val timeOption = "Select the minutes"
private const val repetitionOption = "Select the number of repetitions"

class IrregularVerbsFragment : Fragment(), View.OnClickListener {
    private val mTAG = "IrregularVerbsFragment"
    private var timeDescription: String = ""
    private var repetitionsDescription: String = ""

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
        timeDescription = resources.getString(R.string.time_description)
        repetitionsDescription = resources.getString(R.string.repetitions_description)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_irregular_verbs, container, false)

        mSegmentedGroup = view.findViewById(R.id.sg_duration_type)
        mTxtDurationTypeDescription = view.findViewById(R.id.text_duration_type_description) as TextView
        mTxtOptionDescription = view.findViewById(R.id.text_option_description) as TextView
        mTxtOptionValue= view.findViewById(R.id.text_option_value) as TextView
        mBtnOptionMinus = view.findViewById(R.id.button_option_value_minus) as Button
        mBtnOptionAdd = view.findViewById(R.id.button_option_value_add) as Button

        setTextTimeOption()

        mSegmentedGroup!!.setOnCheckedChangeListener { _, checkedId: Int ->
            val radioTime: RadioButton = view.findViewById(R.id.rb_duration_type_time)

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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = IrregularVerbsFragment()
    }
}
