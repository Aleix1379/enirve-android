package com.aleixmp.enirve.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.aleixmp.enirve.R
import com.aleixmp.numberpicker.NumberPicker
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
    private var npOptionValue: NumberPicker? = null
    private var mBtnNextHome: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeDescription = resources.getString(R.string.time_description)
        repetitionsDescription = resources.getString(R.string.repetitions_description)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_irregular_verbs, container, false)

        mSegmentedGroup = view.findViewById(R.id.sg_duration_type)
        mTxtDurationTypeDescription = view.findViewById(R.id.text_duration_type_description) as TextView
        mTxtOptionDescription = view.findViewById(R.id.text_option_description) as TextView
        npOptionValue = view.findViewById(R.id.np_option_value_container) as NumberPicker
        mBtnNextHome = view.findViewById(R.id.button_next_home) as Button

        setDescriptionDurationType(timeDescription , timeOption)

        mSegmentedGroup!!.setOnCheckedChangeListener { _, checkedId: Int ->
            val radioTime: RadioButton = view.findViewById(R.id.rb_duration_type_time)

            if (radioTime.id == checkedId) {
                setDescriptionDurationType(timeDescription , timeOption)
            } else {
                setDescriptionDurationType(repetitionsDescription , repetitionOption)
            }

        }

        mBtnNextHome?.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        v?.let {
            if (it.tag == "next") {
                val buttonIdChecked = mSegmentedGroup!!.checkedRadioButtonId
                val button: Button = view?.findViewById(buttonIdChecked) as Button

                val intent = Intent(context, SelectionVerbsActivity::class.java)
                intent.putExtra(SelectionVerbsActivity.DURATION_TYPE, button.tag.toString())
                intent.putExtra(SelectionVerbsActivity.DURATION_VALUE, npOptionValue!!.getValue())
                startActivity(intent)
            }
        }
    }

    private fun setDescriptionDurationType(description: String, option: String) {
        mTxtDurationTypeDescription?.text = description
        mTxtOptionDescription?.text = option
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
