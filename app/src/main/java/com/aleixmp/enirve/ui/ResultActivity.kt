package com.aleixmp.enirve.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class ResultActivity : AppCompatActivity(), View.OnClickListener {
    private var mVerbsSelected: ArrayList<Verb>? = null
    private var mSuccess: Int = 0
    private var mErrors: Int = 0

    private var mDurationType: String = ""
    private var mDurationValue: Int = -1

    private var pStatus = 0
    private val handler = Handler()
    private lateinit var tv: TextView

    private lateinit var mTxtResultSuccess: TextView
    private lateinit var mTxtResultErrors: TextView

    private lateinit var mBtnResultRepeat: Button
    private lateinit var mBtnResultFinish: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        loadParams()
        loadViews()
        setResultText()
        setupProgressbar()

    }

    private fun setResultText() {
//getString(R.string.manual_choose_your_verbs, (mVerbsSelected.filter { it.matched }).count())
        mTxtResultSuccess.text = getString(R.string.result_success, mSuccess)
        mTxtResultErrors.text = getString(R.string.result_errors, mErrors)
    }

    private fun loadViews() {
        mTxtResultSuccess = findViewById(R.id.text_view_result_success)
        mTxtResultErrors = findViewById(R.id.text_view_result_errors)

        mBtnResultRepeat = findViewById(R.id.button_result_repeat)
        mBtnResultFinish = findViewById(R.id.button_result_finish)

        mBtnResultRepeat.setOnClickListener(this)
        mBtnResultFinish.setOnClickListener(this)
    }

    private fun setupProgressbar() {
        val res = resources
        val drawable = res.getDrawable(R.drawable.circular)
        val mProgress = findViewById<View>(R.id.circularProgressbar) as ProgressBar
        mProgress.progress = 0
        mProgress.secondaryProgress = 100
        mProgress.max = 100
        mProgress.progressDrawable = drawable

        tv = findViewById<View>(R.id.tv) as TextView
        Thread(Runnable {
            // TODO Auto-generated method stub
            while (pStatus < getPercentage()) {
                pStatus += 1

                handler.post {
                    mProgress.progress = pStatus
                    tv.text = "$pStatus%"
                }
                try {
                    // Sleep for 200 milliseconds.
                    // Just to display the progress slowly
                    Thread.sleep(16) //thread will take approx 3 seconds to finish
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()
    }

    private fun getPercentage(): Int {
        return mSuccess * 100 / (mSuccess + mErrors)
    }

    private val mTag: String = "EnirveResultActivity"

    private fun loadParams() {
        mDurationValue = intent.getIntExtra(ResultActivity.PARAM_DURATION_VALUE, -1)
        mDurationType = intent.extras?.getString(ResultActivity.PARAM_DURATION_TYPE)!!

        mSuccess = intent.getIntExtra(ResultActivity.PARAM_SUCCESS, -1)
        mErrors = intent.getIntExtra(ResultActivity.PARAM_ERRORS, -1)

        val json = intent.extras?.getString(ResultActivity.PARAM_VERBS)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        mVerbsSelected = ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())

        Log.d(mTag, mSuccess.toString())
        Log.d(mTag, mErrors.toString())
        Log.d(mTag, mVerbsSelected.toString())
    }

    override fun onClick(button: View?) {
        if (button != null) {
            if (button.tag == "repeat") {
                val intent = Intent(this, PracticeActivity::class.java)

                val gson = Gson()
                val json = gson.toJson(mVerbsSelected)
                intent.putExtra(PracticeActivity.PARAM_VERBS, json)
                intent.putExtra(PracticeActivity.PARAM_DURATION_VALUE, mDurationValue)
                intent.putExtra(PracticeActivity.PARAM_DURATION_TYPE, mDurationType)

                startActivity(intent)

            } else if (button.tag == "finish") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val PARAM_VERBS = "PARAM_VERBS"
        const val PARAM_SUCCESS = "PARAM_SUCCESS"
        const val PARAM_ERRORS = "PARAM_ERRORS"
        const val PARAM_DURATION_TYPE = "PARAM_DURATION_TYPE"
        const val PARAM_DURATION_VALUE = "PARAM_DURATION_VALUE"
    }
}
