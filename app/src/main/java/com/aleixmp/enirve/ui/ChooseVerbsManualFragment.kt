package com.aleixmp.enirve.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.aleixmp.enirve.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.aleixmp.enirve.model.Verb

class ChooseVerbsManualFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var btnChooseVverbs: Button? = null
    private val REQUEST_FORM = 1
    private var  mVerbsSelected: ArrayList<Verb> = ArrayList()
    private lateinit var mTxtChooseManual: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_verbs_manual, container, false)

        btnChooseVverbs = view.findViewById(R.id.button_choose_verbs)
        btnChooseVverbs!!.setOnClickListener {
            val intent = Intent(context, ChooseManualVerbsActivity::class.java)
            val gson = Gson()
            intent.putExtra(ChooseManualVerbsActivity.PARAM_VERBS, gson.toJson(mVerbsSelected))
            startActivityForResult(intent, REQUEST_FORM)
        }

        mTxtChooseManual = view.findViewById(R.id.text_choose_manual)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the requestCode is the wanted one and if the result is what we are expecting
        if (requestCode == REQUEST_FORM && resultCode == AppCompatActivity.RESULT_OK) {
            val json = data?.getStringExtra(ChooseManualVerbsActivity.INPUT_NAME)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            mVerbsSelected = ArrayList (gson.fromJson(json, Array<Verb>::class.java).toList())

            mTxtChooseManual.text = mVerbsSelected.toString().replace("[", "").replace("]","")
            listener?.onVerbsSelected(mVerbsSelected)

            if (mTxtChooseManual.text.isEmpty()) {
                mTxtChooseManual.text = resources.getString(R.string.choose_manual)
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onVerbsSelected(verbs: List<Verb>)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseVerbsManualFragment()
    }
}
