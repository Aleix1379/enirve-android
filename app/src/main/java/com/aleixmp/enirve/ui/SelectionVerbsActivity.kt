package com.aleixmp.enirve.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import com.aleixmp.enirve.R
import info.hoang8f.android.segmented.SegmentedGroup

class SelectionVerbsActivity : AppCompatActivity() {
    private val mTAG = "SelectionVerbsActivity"

    private var mSegmentedModeChooseVerbs: SegmentedGroup? = null
    private var mTxtModeChooseVerbsescription: TextView? = null

    companion object {
        const val DURATION_TYPE = "DURATION_TYPE"
        const val DURATION_VALUE = "DURATION_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_verbs)

        val durationType = intent.getStringExtra(SelectionVerbsActivity.DURATION_TYPE)
        val durationValue = intent.getStringExtra(SelectionVerbsActivity.DURATION_VALUE)

        mSegmentedModeChooseVerbs = findViewById(R.id.sg_mode_choose_verbs)
        mTxtModeChooseVerbsescription = findViewById(R.id.text_mode_choose_verbs_description)

        setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_all_description))

        mSegmentedModeChooseVerbs!!.setOnCheckedChangeListener { _, checkedId: Int ->
            val radioAll: RadioButton = findViewById(R.id.rb_mode_choose_verbs_all)
            val radioRandom: RadioButton = findViewById(R.id.rb_mode_choose_verbs_random)
            val radioManual: RadioButton = findViewById(R.id.rb_mode_choose_verbs_manual)
            val radioByLevel: RadioButton = findViewById(R.id.rb_mode_choose_verbs_by_level)

            when (checkedId) {
                radioAll.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_all_description))
                radioRandom.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_random_description))
                radioManual.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_manual_description))
                radioByLevel.id -> setDescriptionChooseMode(resources.getString(R.string.mode_choose_verbs_by_level_description))
            }

        }

        Log.d(mTAG, "durationType is: $durationType ")
        Log.d(mTAG, "durationValue is: $durationValue ")
    }

    private fun setDescriptionChooseMode(description: String) {
        mTxtModeChooseVerbsescription?.text = description
    }

}
