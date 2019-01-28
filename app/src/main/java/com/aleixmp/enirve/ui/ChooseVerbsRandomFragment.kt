package com.aleixmp.enirve.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.aleixmp.numberpicker.NumberPicker
import com.aleixmp.enirve.repository.VerbRepo

class ChooseVerbsRandomFragment : Fragment(), NumberPicker.OnValueChanged {
    private var listener: OnFragmentInteractionListener? = null
    private var npRandomValue: NumberPicker? = null
    private var mTxtChooseRandom: TextView? = null
    private var btnRefreshRandom: Button? = null

    private var mMaxRandomVerbs = 5

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_verbs_random, container, false)

        mTxtChooseRandom = view.findViewById(R.id.text_choose_random)

        npRandomValue = view.findViewById(R.id.np_random_value_container)
        npRandomValue!!.setValue(mMaxRandomVerbs)
        npRandomValue!!.setOnValueChangedListener(this)

        btnRefreshRandom = view.findViewById(R.id.button_refresh_reandom)
        btnRefreshRandom!!.setOnClickListener { refreshVerbs() }

        refreshVerbs()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onVerbsSelected(verbs: List<Verb>)
    }

    override fun onUpdateValue(newValue: Int) {
        mMaxRandomVerbs = newValue
        refreshVerbs()
    }

    private fun refreshVerbs() {
        val verbRepo = VerbRepo(activity!!)
        val verbs = verbRepo.getRandomVerbs(mMaxRandomVerbs)

        mTxtChooseRandom!!.text = verbs.toString().replace("[", "").replace("]", "")
        listener?.onVerbsSelected(verbs)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseVerbsRandomFragment()
    }
}
