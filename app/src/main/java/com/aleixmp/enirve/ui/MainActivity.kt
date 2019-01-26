package com.aleixmp.enirve.ui

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aleixmp.enirve.R
import com.aleixmp.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    IrregularVerbsFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener {

    private val mTAG = "IrregularVerbs"

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(mTAG, "onFragmentInteraction uri")
        Log.d(mTAG, "$uri")
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment
        val transaction: FragmentTransaction

        when (item.itemId) {
            R.id.navigation_home -> {
                selectedFragment = IrregularVerbsFragment.newInstance()

                transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.app_title)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val transaction: FragmentTransaction  = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frame_layout,
            IrregularVerbsFragment.newInstance()
        )
        transaction.commit()

    }
}
