package com.aleixmp.enirve.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.aleixmp.enirve.R
import com.aleixmp.enirve.ui.ChooseManualVerbsActivity
import com.aleixmp.enirve.model.Verb


class VerbsAdapter(private var mActivity: ChooseManualVerbsActivity, private var verbs: List<Verb>) : BaseAdapter() {

    private var mVerbsSelected: ArrayList<Verb> = ArrayList()

    init {
        verbs.forEach {
            if (it.matched) {
                mVerbsSelected.add(it)
            }
        }
    }

    override fun getCount(): Int {
        return verbs.count()
    }

    override fun getItemId(position: Int): Long {
        return verbs[position].id.toLong()
    }

    override fun getItem(position: Int): Any {
        return verbs[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val verb = getItem(position) as Verb
        var view: View? = convertView

        if (view == null) {
            val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
            view = layoutInflater.inflate(R.layout.layout_verb, null)
        }

        val buton: Button = view!!.findViewById(R.id.manual_verb_button)
        buton.text = verb.present

        changeButtonBackground(verb, buton)

        buton.setOnClickListener {
            verb.matched = !verb.matched
            if (verb.matched) {
                mVerbsSelected.add(verb)
            } else {
                mVerbsSelected.remove(verb)
            }
            mActivity.updateVerbsSelected(mVerbsSelected)
            changeButtonBackground(verb, buton)
        }

        return view
    }

    private fun changeButtonBackground(verb: Verb, buton: Button) {
        if (verb.matched) {
            buton.background = mActivity.resources.getDrawable(R.drawable.verb_selected)
        } else {
            buton.background = mActivity.resources.getDrawable(R.drawable.verb_unselected)
        }
    }

    fun getVerbsSelected(): ArrayList<Verb> {
        return mVerbsSelected
    }

    fun setData(newData: List<Verb>) {
        verbs = newData

        verbs.forEach {
            if (it.matched && !mVerbsSelected.contains(it)) {
                mVerbsSelected.add(it)
            } else if (!it.matched && mVerbsSelected.contains(it)) {
                mVerbsSelected.remove(it)
            }
        }
    }

}
