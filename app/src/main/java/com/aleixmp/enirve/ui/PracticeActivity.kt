package com.aleixmp.enirve.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.google.gson.GsonBuilder

class PracticeActivity : AppCompatActivity() {
    private var mVerbsSelected: List<Verb>? = null
    private var mDurationType: String = ""
    private var mDurationValue: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        mDurationValue = intent.getIntExtra(PracticeActivity.PARAM_DURATION_VALUE, -1)
        mDurationType = intent.extras?.getString(PracticeActivity.PARAM_DURATION_TYPE)!!

        val json = intent.extras?.getString(PracticeActivity.PARAM_VERBS)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        mVerbsSelected = ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())
    }

    companion object {
        const val PARAM_VERBS = "PARAM_VERBS"
        const val PARAM_DURATION_TYPE = "PARAM_DURATION_TYPE"
        const val PARAM_DURATION_VALUE = "PARAM_DURATION_VALUE"
    }

}
