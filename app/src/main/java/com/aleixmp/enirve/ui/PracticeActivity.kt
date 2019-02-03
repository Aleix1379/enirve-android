package com.aleixmp.enirve.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*
import kotlin.concurrent.fixedRateTimer


class PracticeActivity : AppCompatActivity(), View.OnClickListener {
    private var mVerbsSelected: ArrayList<Verb>? = null
    private var mDurationType: String = ""
    private var mDurationValue: Int = -1
    private var mDurationSeconds: Int = -1
    private val mTag: String = "EnirvePracticeActivity"
    private var mLimitRepetitions: Int = -1
    private var mCurrentIndex: Int = 0
    private var mCurrentVerb: Verb? = null

    private val handler = Handler()
    private lateinit var txtProgressbar: TextView
    private lateinit var mDrawableProgressbarByTime: Drawable
    private lateinit var mProgress: ProgressBar

    private var mSuccess: Int = 0
    private var mErrors: Int = 0

    private lateinit var mRdLimitPractice: RoundCornerProgressBar
    private lateinit var mTxtTitleRepetitions: TextView
    private lateinit var mClProgressbarRepetitions: ConstraintLayout
    private lateinit var mClProgressbarTime: ConstraintLayout
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
        setupProgressbar()

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
        return if (mDurationType == "repetitions") {
            mVerbsSelected!![mCurrentIndex]
        } else //if (mDurationType == "time") {
            mVerbsSelected!![mCurrentIndex % mVerbsSelected!!.size]
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
        mClProgressbarTime.visibility = View.VISIBLE

        mDurationSeconds = mDurationValue * 60

/*        val myTimer = Timer()
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                mDurationSeconds -= 1
                updateProgressbarByTime()
            }
        }, 1000)*/

        fixedRateTimer("default", false, 0L, 1000) {
            mDurationSeconds -= 1
            Log.d(mTag, "time left: $mDurationSeconds")
            updateProgressbarByTime()
            if (mDurationSeconds == 0) cancel()
        }

    }

    private fun initRepetitionsPractice() {
        mLimitRepetitions = mVerbsSelected!!.size

        mRdLimitPractice = findViewById(R.id.rc_limit_practice)
        updateProgressbar()

        mTxtTitleRepetitions = findViewById(R.id.text_view_practice_title_repetitions)

        updateRepetitionsTitle()

        mClProgressbarRepetitions.visibility = View.VISIBLE
        mClProgressbarTime.visibility = View.GONE
    }

    private fun loadViews() {
        mClProgressbarTime = findViewById(R.id.cl_progressbar_time_container)
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
        txtProgressbar = findViewById<View>(R.id.text_view_progressbar_practice) as TextView
        mProgress = findViewById<View>(R.id.circularProgressbar) as ProgressBar
        mDrawableProgressbarByTime = resources.getDrawable(R.drawable.circular)
    }

    private fun updateProgressbar() {
        if (mDurationType == "repetitions") {
            mRdLimitPractice.progress = getPercentage()
        }
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
        if (mDurationType == "repetitions") {
            mTxtTitleRepetitions.text = getString(
                R.string.practice_title_repetitions,
                (mSuccess + mErrors), mLimitRepetitions
            )
        }
    }

    private fun getPercentage(): Float {
        val value = mSuccess + mErrors
        return (value * 100 / mVerbsSelected!!.size).toFloat()
    }

    override fun onClick(button: View?) {
        button?.let {
            when {
                it.tag == "check" -> {
                    checkVerb()
                    updateRepetitionsTitle()
                    updateProgressbar()
                    toggleVisivilityButtons(false)
                }
                it.tag == "next" -> {
                    nextVerb()
                    cleanEditText()
                    toggleVisivilityButtons(true)
                    mEdtPastSimple.requestFocus()
                }
                it.tag == "results" -> {
                    //                Toast.makeText(baseContext, "Success: $mSuccess Errors: $mErrors", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ResultActivity::class.java)
                    val gson = Gson()
                    intent.putExtra(ResultActivity.PARAM_VERBS, gson.toJson(mVerbsSelected))
                    intent.putExtra(ResultActivity.PARAM_SUCCESS, mSuccess)
                    intent.putExtra(ResultActivity.PARAM_ERRORS, mErrors)
                    intent.putExtra(ResultActivity.PARAM_DURATION_TYPE, mDurationType)
                    intent.putExtra(ResultActivity.PARAM_DURATION_VALUE, mDurationValue)
                    startActivity(intent)
                }
                else -> {
                }
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
        val resultPastSimple = verbValidation(
            mEdtPastSimple.text.toString(),
            mCurrentVerb!!.simple
        )// == mEdtPastSimple.text.toString().toLowerCase().trim()
        val resultPastParticiple = verbValidation(
            mEdtPastParticiple.text.toString(),
            mCurrentVerb!!.participle
        )//mCurrentVerb!!.participle == mEdtPastParticiple.text.toString().toLowerCase().trim()

//        resposta == verb.split("/")[0] ||
//                resposta. == verb.split("/")[1]

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

    private fun verbValidation(newValue: String, verb: String): Boolean {
        return if (verb.contains("/")) {
            newValue.toLowerCase().trim() == verb.split("/")[0] ||
                    newValue.toLowerCase().trim() == verb.split("/")[1]
        } else {
            newValue.toLowerCase().trim() == verb
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
                layout.visibility = View.INVISIBLE
                imgOk.visibility = View.VISIBLE
            }
            else -> {
                layout.visibility = View.VISIBLE
                imgOk.visibility = View.INVISIBLE
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

        if ((mDurationType == "repetitions" && !next && mCurrentIndex == mLimitRepetitions - 1) ||
            mDurationType == "time" && mDurationSeconds == 0
        ) {
            mBtnPracticeCheck.visibility = View.GONE
            mBtnPracticeNext.visibility = View.GONE
            mBtnPracticeShowTheResults.visibility = View.VISIBLE
        }

    }

    private fun setupProgressbar() {
        mProgress.progress = 0
        mProgress.secondaryProgress = 100
        mProgress.max = 100
        mProgress.progressDrawable = mDrawableProgressbarByTime

        updateProgressbarByTime()
    }

    private fun updateProgressbarByTime() {

        runOnUiThread {
            // Stuff that updates the UI
            mProgress.progress = getPercentageByTime()
            txtProgressbar.text = getTextProgressbarByTime()
        }

    }

    private fun getTextProgressbarByTime(): CharSequence? {
        val hours = mDurationSeconds / 3600
        val minutes = (mDurationSeconds - hours * 3600) / 60
        val seconds = mDurationSeconds - hours * 3600 - minutes * 60

        var strHours = hours.toString()
        var strMinutes = minutes.toString()
        var strSeconds = seconds.toString()

        if (hours < 10) {
            strHours = "0$hours"
        }
        if (minutes < 10) {
            strMinutes = "0$minutes"
        }
        if (seconds < 10) {
            strSeconds = "0$seconds"
        } else if (seconds == 0) {
            strSeconds = "0"
        }

        if (mDurationSeconds < 60) {
            return strSeconds
        } else if (mDurationSeconds < 3600) {
            return "$strMinutes:$strSeconds"
        }
        return "$strHours:$strMinutes:$strSeconds"
    }

    private fun getPercentageByTime(): Int {
        return (mDurationSeconds * 100) / (mDurationValue * 60)
    }

    companion object {
        const val PARAM_VERBS = "PARAM_VERBS"
        const val PARAM_DURATION_TYPE = "PARAM_DURATION_TYPE"
        const val PARAM_DURATION_VALUE = "PARAM_DURATION_VALUE"
    }

}
