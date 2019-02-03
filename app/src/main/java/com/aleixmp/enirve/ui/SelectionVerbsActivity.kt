package com.aleixmp.enirve.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.google.gson.Gson
import info.hoang8f.android.segmented.SegmentedGroup


class SelectionVerbsActivity : AppCompatActivity(),
    RadioGroup.OnCheckedChangeListener,
    ChooseVerbsAllFragment.OnFragmentInteractionListener,
    ChooseVerbsRandomFragment.OnFragmentInteractionListener,
    ChooseVerbsManualFragment.OnFragmentInteractionListener,
    ChooseVerbsByLevelFragment.OnFragmentInteractionListener {

    private lateinit var mBtnNextChooseVerbs: Button
    private var mVerbsSelected: List<Verb>? = null

    override fun onVerbsSelected(verbs: List<Verb>) {
        mVerbsSelected = verbs
    }

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

        mDurationType = intent.getStringExtra(DURATION_TYPE)
        mDurationValue = intent.getIntExtra(DURATION_VALUE, -1)

        mSegmentedModeChooseVerbs = findViewById(R.id.sg_mode_choose_verbs)
        mTxtModeChooseVerbsescription = findViewById(R.id.text_mode_choose_verbs_description)
        mBtnNextChooseVerbs = findViewById(R.id.button_next_choose_verbs)

        mBtnNextChooseVerbs.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)

            updateVerbsSelectionByRepetitions()

            val gson = Gson()
            val json = gson.toJson(mVerbsSelected)
            intent.putExtra(PracticeActivity.PARAM_VERBS, json)
            intent.putExtra(PracticeActivity.PARAM_DURATION_TYPE, mDurationType)
            intent.putExtra(PracticeActivity.PARAM_DURATION_VALUE, mDurationValue)

            startActivity(intent)
        }

        setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_all_description))
        setFragmentChooseMode(ChooseVerbsAllFragment.newInstance())

        mSegmentedModeChooseVerbs!!.setOnCheckedChangeListener(this)
    }

    private fun updateVerbsSelectionByRepetitions() {
        val verbs: ArrayList<Verb> = ArrayList()
        if (mDurationType == "repetitions") {
            for (i in 0 until mVerbsSelected!!.size * mDurationValue) {
                verbs.add(mVerbsSelected!![i % mVerbsSelected!!.size])
            }
            mVerbsSelected = verbs
        }
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
            radioManual.id -> {
                setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_manual_description))
                setFragmentChooseMode(ChooseVerbsManualFragment.newInstance())
            }
            radioByLevel.id -> {
                setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_by_level_description))
                setFragmentChooseMode(ChooseVerbsByLevelFragment.newInstance())
            }
        }
    }

}
