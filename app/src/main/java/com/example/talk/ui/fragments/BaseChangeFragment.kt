package com.example.talk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.talk.MainActivity
import com.example.talk.R
import com.example.talk.utilites.APP_ACTIVITY


open class BaseChangeFragment(layout: Int) : BaseFragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_name -> change()
        }
        return true
    }

    open fun change() {

    }
}