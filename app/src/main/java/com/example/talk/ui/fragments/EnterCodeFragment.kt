package com.example.talk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.talk.MainActivity
import com.example.talk.R
import com.example.talk.activities.RegisterActivity
import com.example.talk.utilites.AUTH
import com.example.talk.utilites.AppTextWatcher
import com.example.talk.utilites.CHILD_ID
import com.example.talk.utilites.CHILD_PHONE
import com.example.talk.utilites.CHILD_USERNAME
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.initFirebase
import com.example.talk.utilites.replaceActivity
import com.example.talk.utilites.showToast
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {
    lateinit var enterCodeNumberInput: EditText

    override fun onStart() {
        super.onStart()
        initFirebase()
        (activity as RegisterActivity).title = phoneNumber
        enterCodeNumberInput.addTextChangedListener(AppTextWatcher() {
            val string: String = enterCodeNumberInput.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_code, container, false)
        enterCodeNumberInput = view.findViewById(R.id.enter_code_number_input)
        return view
    }

    private fun enterCode() {
        val code = enterCodeNumberInput.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid
                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            showToast("Добро пожаловать!")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        } else showToast(task2.exception?.message.toString())
                    }
            } else {
                showToast(task.exception?.message.toString())
            }
        }
    }
}