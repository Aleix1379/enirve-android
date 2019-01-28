package com.aleixmp.enirve.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.aleixmp.enirve.R
import com.aleixmp.enirve.model.Verb
import com.aleixmp.enirve.repository.VerbRepo

class ChooseVerbsAllFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var mTxtChooseAll: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_verbs_all, container, false)

        mTxtChooseAll = view.findViewById(R.id.text_choose_all) as TextView

        val verbRepo = VerbRepo(activity!!)
        val verbs = verbRepo.getAllVerbs()

        mTxtChooseAll!!.text = verbs.toString().replace("[", "").replace("]","")

        listener?.onVerbsSelected(verbs)

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

    companion object {
        @JvmStatic
        fun newInstance() = ChooseVerbsAllFragment()
    }
}
