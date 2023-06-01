package com.example.talk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.talk.R
import com.example.talk.utilites.CHILD_BIO
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.UID
import com.example.talk.utilites.USER
import com.example.talk.utilites.showToast

class ChangeBioFragment : BaseChangeFragment(layout = R.layout.fragment_change_bio) {
    private lateinit var settings_input_change_bio: EditText

    override fun initWidgets(view: View) {
        settings_input_change_bio = view.findViewById(R.id.settings_input_change_bio)
    }

    override fun onResume() {
        super.onResume()
        settings_input_change_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_change_bio.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO)
            .setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Данные обновлены")
                    USER.bio = newBio
                    parentFragmentManager.popBackStack()
                }
            }
    }
}