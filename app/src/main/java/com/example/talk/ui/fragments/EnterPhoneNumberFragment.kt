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
import com.example.talk.utilites.replaceActivity
import com.example.talk.utilites.replaceFragment
import com.example.talk.utilites.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment() : Fragment(R.layout.fragment_enter_phone_number) {
    lateinit var registerBtnNext: FloatingActionButton
    lateinit var registerInputPhoneNumber: EditText
    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        registerBtnNext.setOnClickListener { sendCode() }
        //mAuth = FirebaseAuth.getInstance()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast("Добро пожаловать!")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else {
                        showToast(it.exception?.message.toString())
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_phone_number, container, false)
        registerBtnNext = view.findViewById(R.id.registerBtnNext)
        registerInputPhoneNumber = view.findViewById(R.id.registerInputPhoneNumber)
        return view
    }

    private fun sendCode() {
        if (registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.enter_phone_number))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = registerInputPhoneNumber.text.toString()

        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder(AUTH)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity as RegisterActivity)
                .setCallbacks(mCallBack)
                .build()
        )
    }
}
