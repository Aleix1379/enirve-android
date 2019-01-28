package com.aleixmp.enirve.repository

import android.content.Context
import com.google.gson.GsonBuilder
import com.aleixmp.enirve.model.Verb

class VerbRepo(context: Context) {

    private val mContext: Context = context
    private var mVerbs: ArrayList<Verb>? = null

    private fun loadVerbs(): ArrayList<Verb> {
        val json = mContext.assets.open("verbs.json").readBytes().toString(Charsets.UTF_8)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        return ArrayList(gson.fromJson(json, Array<Verb>::class.java).toList())
    }

    fun getAllVerbs(): ArrayList<Verb> {
        if (mVerbs == null) {
            mVerbs = loadVerbs()
        }
        return mVerbs!!
    }

    fun getRandomVerbs(max: Int): List<Verb> {
        val verbs: ArrayList<Verb> = ArrayList(getAllVerbs())
        val result: ArrayList<Verb> = ArrayList()

        for (i in 1..max) {
            val index = (0 until verbs.count()).random()
            result.add(verbs[index])
            verbs.removeAt(index)
        }

        return result
    }

    fun getVerbs(searchText: String): List<Verb> {
        if (searchText.isEmpty()) {
            return getAllVerbs()
        }
        return getAllVerbs().filter {
            it.present.contains(searchText, true) ||
                    it.simple.contains(searchText, false) ||
                    it.participle.contains(searchText, false)
        }
    }

}
