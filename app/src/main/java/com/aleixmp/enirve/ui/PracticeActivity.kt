package com.aleixmp.enirve.ui

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.google.gson.GsonBuilder

class PracticeActivity : AppCompatActivity(), View.OnClickListener {
    private var mVerbsSelected: ArrayList<Verb>? = null
    private var mDurationType: String = ""
    private var mDurationValue: Int = -1
    private val mTag: String = "PracticeActivity"
    private var mLimitRepetitions: Int = -1
    private var mCurrentIndex: Int = 0
    private var mCurrentVerb: Verb? = null

    private var mSuccess: Int = 0
    private var mErrors: Int = 0

    private lateinit var mRdLimitPractice: RoundCornerProgressBar
    private lateinit var mTxtTitleRepetitions: TextView
    private lateinit var mClProgressbarRepetitions: ConstraintLayout
    private lateinit var mTextViewPracticeSuccess: TextView
    private lateinit var mTextViewPracticeError: TextView
    private lateinit var mTextViewPracticeVerb: TextView
    private lateinit var mBtnPracticeCheck: Button
    private lateinit var mBtnPracticeNext: Button
    private lateinit var mBtnPracticeShowTheResults: Button
    private lateinit var mEdtPastSimple: EditText
    private lateinit var mEdtPastParticiple: EditText
    private lateinit var mClPastSimpleError: ConstraintLayout
    private lateinit var mClPastParticipleError: ConstraintLayout
    private lateinit var mTxtPracticeCorrectionSimple: TextView
    private lateinit var mTxtPracticeCorrectionParticiple: TextView
    private lateinit var mImvPracticeSimpleOk: ImageView
    private lateinit var mImvPracticeParticipleOk: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        loadParams()
        loadViews()
        initData()

        if (mDurationType == "repetitions") {
            initRepetitionsPractice()
        } else if (mDurationType == "time") {
            initTimePractice()
        }
    }

    private fun initData() {
        updateSuccesValue(0)
        updateErrorValue(0)
        mCurrentVerb = getCurrentVerb()
        updateCurrentVerb()
    }

    private fun getCurrentVerb(): Verb {
        return mVerbsSelected!![mCurrentIndex]
    }

    private fun getNextVerb(): Verb {
        mCurrentIndex++
        return getCurrentVerb()
    }

    private fun updateCurrentVerb() {
        mTextViewPracticeVerb.text = getString(R.string.practice_verb, mCurrentVerb!!.present)
    }

    private fun initTimePractice() {
        mClProgressbarRepetitions.visibility = View.GONE
    }

    private fun initRepetitionsPractice() {
        mLimitRepetitions = mVerbsSelected!!.size * mDurationValue

        mRdLimitPractice = findViewById(R.id.rc_limit_practice)
        updateProgressbar()

        mTxtTitleRepetitions = findViewById(R.id.text_view_practice_title_repetitions)

        updateRepetitionsTitle()

        mClProgressbarRepetitions.visibility = View.VISIBLE
    }

    private fun loadViews() {
        mClProgressbarRepetitions = findViewById(R.id.cl_progressbar_repetitions_container)
        mTextViewPracticeSuccess = findViewById(R.id.text_view_practice_success)
        mTextViewPracticeError = findViewById(R.id.text_view_practice_error)
        mTextViewPracticeVerb = findViewById(R.id.text_view_practice_verb)
        mEdtPastSimple = findViewById(R.id.edit_text_past_simple)
        mEdtPastParticiple = findViewById(R.id.edit_text_past_participle)
        mBtnPracticeCheck = findViewById(R.id.button_practice_check)
        mBtnPracticeNext = findViewById(R.id.button_practice_next)
        mBtnPracticeShowTheResults = findViewById(R.id.button_practice_show_result)
        mBtnPracticeCheck.setOnClickListener(this)
        mBtnPracticeNext.setOnClickListener(this)
        mBtnPracticeShowTheResults.setOnClickListener(this)
        mClPastSimpleError = findViewById(R.id.cl_past_simple_error)
        mClPastParticipleError = findViewById(R.id.cl_past_participle_error)
        mTxtPracticeCorrectionSimple = findViewById(R.id.text_view_practice_correction_simple)
        mTxtPracticeCorrectionParticiple = findViewById(R.id.text_view_practice_correction_participle)
        mImvPracticeSimpleOk = findViewById(R.id.practice_simple_ok)
        mImvPracticeParticipleOk = findViewById(R.id.practice_participle_ok)
    }

    private fun updateProgressbar() {
        mRdLimitPractice.progress = getPercentage()
    }

    private fun updateErrorValue(value: Int) {
        mTextViewPracticeError.text = getString(R.string.practice_errors, value)
    }

    private fun updateSuccesValue(value: Int) {
        mTextViewPracticeSuccess.text = getString(R.string.practice_success, value)
    }

    private fun loadParams() {
        mDurationValue = intent.getIntExtra(PARAM_DURATION_VALUE, -1)
        mDurationType = intent.extras?.getString(PARAM_DURATION_TYPE)!!

        val json = intent.extras?.getString(PARAM_VERBS)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        mVerbsSelected = ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())
    }

    private fun updateRepetitionsTitle() {
        mTxtTitleRepetitions.text = getString(
            R.string.practice_title_repetitions,
            (mSuccess + mErrors), mLimitRepetitions
        )
    }

    private fun getPercentage(): Float {
        val value = mSuccess + mErrors
        return (value * 100 / mVerbsSelected!!.size).toFloat()
    }

    override fun onClick(button: View?) {
        if (button != null) {
            if (button.tag == "check") {
                checkVerb()
                updateRepetitionsTitle()
                updateProgressbar()
                toggleVisivilityButtons(false)
            } else if (button.tag == "next") {
                nextVerb()
                cleanEditText()
                toggleVisivilityButtons(true)
                mEdtPastSimple.requestFocus()
            } else if (button.tag == "results") {
                Toast.makeText(baseContext, "Success: $mSuccess Errors: $mErrors", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun nextVerb() {
        mCurrentVerb = getNextVerb()
        updateCurrentVerb()
        setInputCorrection(mClPastSimpleError, mTxtPracticeCorrectionSimple, "", mImvPracticeSimpleOk, true)
        setInputCorrection(
            mClPastParticipleError,
            mTxtPracticeCorrectionParticiple,
            "",
            mImvPracticeParticipleOk,
            true
        )
    }

    private fun checkVerb() {
        val resultPastSimple = mCurrentVerb!!.simple == mEdtPastSimple.text.toString().toLowerCase()
        val resultPastParticiple = mCurrentVerb!!.participle == mEdtPastParticiple.text.toString().toLowerCase()

        setInputCorrection(
            mClPastSimpleError,
            mTxtPracticeCorrectionSimple,
            mCurrentVerb!!.simple,
            mImvPracticeSimpleOk,
            resultPastSimple
        )
        setInputCorrection(
            mClPastParticipleError,
            mTxtPracticeCorrectionParticiple,
            mCurrentVerb!!.participle,
            mImvPracticeParticipleOk,
            resultPastParticiple
        )

        val result = resultPastSimple && resultPastParticiple

        if (result) {
            updateSuccesValue(++mSuccess)
        } else {
            updateErrorValue(++mErrors)
        }

    }

    private fun setInputCorrection(
        layout: ConstraintLayout,
        editText: TextView,
        correction: String,
        imgOk: ImageView,
        isCorrect: Boolean
    ) {
        when {
            isCorrect -> {
                layout.visibility = View.GONE
                imgOk.visibility = View.VISIBLE
            }
            else -> {
                layout.visibility = View.VISIBLE
                imgOk.visibility = View.GONE
            }
        }
        editText.text = correction
    }

    private fun cleanEditText() {
        mEdtPastSimple.text.clear()
        mEdtPastParticiple.text.clear()
        mImvPracticeSimpleOk.visibility = View.GONE
        mImvPracticeParticipleOk.visibility = View.GONE
    }

    private fun toggleVisivilityButtons(next: Boolean) {
        if (mBtnPracticeCheck.visibility == View.VISIBLE) {
            mBtnPracticeCheck.visibility = View.GONE
            mBtnPracticeNext.visibility = View.VISIBLE
        } else {
            mBtnPracticeCheck.visibility = View.VISIBLE
            mBtnPracticeNext.visibility = View.GONE
        }

        if (!next && mCurrentIndex == mLimitRepetitions - 1) {
            mBtnPracticeCheck.visibility = View.GONE
            mBtnPracticeNext.visibility = View.GONE
            mBtnPracticeShowTheResults.visibility = View.VISIBLE
        }

    }

    companion object {
        const val PARAM_VERBS = "PARAM_VERBS"
        const val PARAM_DURATION_TYPE = "PARAM_DURATION_TYPE"
        const val PARAM_DURATION_VALUE = "PARAM_DURATION_VALUE"
    }

}
