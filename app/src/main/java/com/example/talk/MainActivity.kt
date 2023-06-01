package com.example.talk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import com.example.talk.activities.RegisterActivity
import com.example.talk.databinding.ActivityMainBinding
import com.example.talk.models.User
import com.example.talk.ui.fragments.ChatFragment
import com.example.talk.ui.objects.AppDrawer
import com.example.talk.utilites.APP_ACTIVITY
import com.example.talk.utilites.AUTH
import com.example.talk.utilites.AppStates
import com.example.talk.utilites.AppValueEventListener
import com.example.talk.utilites.CHILD_PHOTO_URL
import com.example.talk.utilites.InitUser
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.PROFILE_FOLDER_IMAGE
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.REF_STORAGE_ROOT
import com.example.talk.utilites.UID
import com.example.talk.utilites.USER
import com.example.talk.utilites.initFirebase
import com.example.talk.utilites.replaceActivity
import com.example.talk.utilites.replaceFragment
import com.example.talk.utilites.showToast
import com.theartofdev.edmodo.cropper.CropImage


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        InitUser {
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.Create()
            replaceFragment(ChatFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }
}