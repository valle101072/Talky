package com.example.talk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.talk.MainActivity
import com.example.talk.R
import com.example.talk.utilites.CHILD_FULLNAME
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.UID
import com.example.talk.utilites.USER
import com.example.talk.utilites.showToast


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    private lateinit var settings_input_change_name: EditText
    private lateinit var settings_input_change_surname: EditText

    override fun initWidgets(view: View) {
        settings_input_change_name = view.findViewById(R.id.settings_input_change_name)
        settings_input_change_surname = view.findViewById(R.id.settings_input_change_surname)
    }

    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1){
            settings_input_change_name.setText(fullnameList[0])
            settings_input_change_surname.setText(fullnameList[1])
        } else {
            settings_input_change_name.setText(fullnameList[0])
        }
    }

    override fun change() {
        val name = settings_input_change_name.text.toString()
        val surname = settings_input_change_surname.text.toString()
        if (name.isEmpty()){
            showToast(getString(R.string.settings_input_change_name_empty))
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful){
                        showToast(getString(R.string.settings_name_changed))
                        USER.fullname = fullname
                        parentFragmentManager.popBackStack()
                    }
                }
        }
    }
}