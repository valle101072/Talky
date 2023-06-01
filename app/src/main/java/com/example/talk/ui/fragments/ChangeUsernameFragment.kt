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
import com.example.talk.utilites.AppValueEventListener
import com.example.talk.utilites.CHILD_USERNAME
import com.example.talk.utilites.NODE_USERNAMES
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.UID
import com.example.talk.utilites.USER
import com.example.talk.utilites.showToast
import java.util.Locale
import java.util.UUID

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String
    private lateinit var settings_input_change_username: EditText

    override fun onResume() {
        super.onResume()
        settings_input_change_username.setText(USER.username)
    }

    override fun change() {
        mNewUsername = settings_input_change_username.text.toString().lowercase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast("Такой пользователь уже существует!")
                    } else {
                        changeUserName()
                    }
                })
        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername)
            .setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username)
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.settings_name_changed))
                    parentFragmentManager.popBackStack()
                    USER.username = mNewUsername
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    override fun initWidgets(view: View) {
        settings_input_change_username = view.findViewById(R.id.settings_input_change_username)
    }
}