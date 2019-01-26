package com.aleixmp.enirve.ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.aleixmp.enirve.R
import info.hoang8f.android.segmented.SegmentedGroup

class SelectionVerbsActivity : AppCompatActivity(),
    RadioGroup.OnCheckedChangeListener,
    ChooseVerbsAllFragment.OnFragmentInteractionListener,
    ChooseVerbsRandomFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    enum class ModeChooseVerbs {
//        ALL,
//        RANDOM,
//        MANUAL,
//        BY_LEVEL
//    }

    private val mTAG = "SelectionVerbsActivity"

    private var mSegmentedModeChooseVerbs: SegmentedGroup? = null
    private var mTxtModeChooseVerbsescription: TextView? = null

    private var mDurationType: String = ""
    private var mDurationValue: Int = -1

    companion object {
        const val DURATION_TYPE = "DURATION_TYPE"
        const val DURATION_VALUE = "DURATION_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_verbs)

        mDurationType = intent.getStringExtra(SelectionVerbsActivity.DURATION_TYPE)
        mDurationValue = intent.getIntExtra(SelectionVerbsActivity.DURATION_VALUE, -1)

        mSegmentedModeChooseVerbs = findViewById(R.id.sg_mode_choose_verbs)
        mTxtModeChooseVerbsescription = findViewById(R.id.text_mode_choose_verbs_description)

        setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_all_description))
        setFragmentChooseMode(ChooseVerbsAllFragment.newInstance())

        mSegmentedModeChooseVerbs!!.setOnCheckedChangeListener(this)
    }

    private fun setDescriptionChooseMode(description: String) {
        mTxtModeChooseVerbsescription?.text = description
    }

    private fun setFragmentChooseMode(selectedFragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout_choose_verbs, selectedFragment)
        transaction.commit()
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val radioAll: RadioButton = findViewById(R.id.rb_mode_choose_verbs_all)
        val radioRandom: RadioButton = findViewById(R.id.rb_mode_choose_verbs_random)
        val radioManual: RadioButton = findViewById(R.id.rb_mode_choose_verbs_manual)
        val radioByLevel: RadioButton = findViewById(R.id.rb_mode_choose_verbs_by_level)

        when (checkedId) {
            radioAll.id -> {
                setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_all_description))
                setFragmentChooseMode(ChooseVerbsAllFragment.newInstance())
            }
            radioRandom.id -> {
                setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_random_description))
                setFragmentChooseMode(ChooseVerbsRandomFragment.newInstance())
            }
            radioManual.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_manual_description))
            radioByLevel.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_by_level_description))
        }
    }

}
