package repository

import android.content.Context
import com.google.gson.GsonBuilder
import model.Verb

class VerbRepo(private val context: Context) {

    fun getAllVerbs(): ArrayList<Verb> {
        val json = context.assets.open("verbs.json").readBytes().toString(Charsets.UTF_8)

        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()

        return ArrayList (gson.fromJson(json, Array<Verb>::class.java).toList())
    }

    fun getRandomVerbs(max: Int): List<Verb> {
        val verbs: ArrayList<Verb> = getAllVerbs()
        val result: ArrayList<Verb> = ArrayList()

        for (i in 1..max) {
            val index = (0 until verbs.count()).random()
            result.add(verbs[index])
            verbs.removeAt(index)
        }

        return result
    }

}
