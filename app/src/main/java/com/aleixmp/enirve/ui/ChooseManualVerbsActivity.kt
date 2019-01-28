package com.aleixmp.enirve.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import com.aleixmp.enirve.R
import com.aleixmp.enirve.adapter.VerbsAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.aleixmp.enirve.model.Verb
import com.aleixmp.enirve.repository.VerbRepo


class ChooseManualVerbsActivity : AppCompatActivity(), TextWatcher {
    private lateinit var mGvManualVerbs: GridView
    private lateinit var mBtnManualVerbsSave: Button
    private lateinit var mBtnManualVerbsCancel: Button
    private lateinit var mBtnManualVerbsSelectAll: Button
    private lateinit var mBtnManualVerbsDeSelectAll: Button
    private lateinit var mTxtTitle: TextView
    private var mVerbs: List<Verb> = ArrayList()
    private var mOriginalVerbsSelected: List<Verb> = ArrayList()
    private lateinit var mAdapter: VerbsAdapter
    private lateinit var mExtSearchVerbs: EditText
    private val mVerbRepo = VerbRepo(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_manual_verbs)

        mGvManualVerbs = findViewById(R.id.grid_view_manual_verbs)
        mBtnManualVerbsSave = findViewById(R.id.button_manual_verbs_save)
        mBtnManualVerbsCancel = findViewById(R.id.button_manual_verbs_cancel)
        mBtnManualVerbsSelectAll = findViewById(R.id.button_verbs_select_all)
        mBtnManualVerbsDeSelectAll = findViewById(R.id.button_verbs_deselect_all)
        mExtSearchVerbs = findViewById(R.id.text_search_verbs)
        mTxtTitle = findViewById(R.id.text_manual_choose_your_verbs)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        mExtSearchVerbs.addTextChangedListener(this)

        mVerbs = mVerbRepo.getAllVerbs()

        val json = intent.getStringExtra(ChooseManualVerbsActivity.PARAM_VERBS)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        val paramVerbs = ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())
        val mOriginalVerbsSelected = ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())

        setMatchVerbsSelected(mVerbs, paramVerbs)

        mAdapter = VerbsAdapter(this, mVerbs)
        mGvManualVerbs.adapter = mAdapter

        mBtnManualVerbsSelectAll.setOnClickListener {
            toggleAllVerbs(true)
        }
        mBtnManualVerbsDeSelectAll.setOnClickListener {
            toggleAllVerbs(false)
        }

        mBtnManualVerbsCancel.setOnClickListener {
            val resultIntent = Intent()
            val gson = Gson()
            resultIntent.putExtra(INPUT_NAME, gson.toJson(mOriginalVerbsSelected))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        mBtnManualVerbsSave.setOnClickListener {
            val resultIntent = Intent()
            val gson = Gson()
            resultIntent.putExtra(INPUT_NAME, gson.toJson(mAdapter.getVerbsSelected()))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        updateTitle(mAdapter.getVerbsSelected().count())
    }

    private fun setMatchVerbsSelected(verbs: List<Verb>, paramVerbs: ArrayList<Verb>) {
        verbs.forEach { it.matched = getVerbById(it.id, paramVerbs)?.matched ?: false }
    }

    private fun getVerbById(id: Int, paramVerbs: ArrayList<Verb>): Verb? {
        return paramVerbs.find { it.id == id }
    }

    private fun toggleAllVerbs(matched: Boolean) {
        mVerbs.forEach { it.matched = matched }
        mAdapter.setData(mVerbs)
        mAdapter.notifyDataSetChanged()
        updateTitle(mAdapter.getVerbsSelected().count())
    }

    fun updateVerbsSelected(mVerbsSelected: List<Verb>) {
        mTxtTitle.text = getString(R.string.manual_choose_your_verbs, (mVerbsSelected.filter { it.matched }).count())
    }

    private fun updateTitle(num: Int) {
        mTxtTitle.text = getString(R.string.manual_choose_your_verbs, num)
    }

    companion object {
        const val INPUT_NAME =
            "com.example.app.INPUT_NAME" //this is basically a static field in java that can be accessed from other classes
        const val PARAM_VERBS = "PARAM_VERBS"
    }

    override fun afterTextChanged(s: Editable?) {
        mVerbs = mVerbRepo.getVerbs(s.toString())

//        mAdapter = VerbsAdapter(this, mVerbs)
//        mGvManualVerbs.adapter = mAdapter

        mAdapter.setData(mVerbs)

        mAdapter.notifyDataSetChanged()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

}

