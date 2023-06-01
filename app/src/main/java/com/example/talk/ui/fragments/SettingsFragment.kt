package com.example.talk.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.talk.MainActivity
import com.example.talk.R
import com.example.talk.activities.RegisterActivity
import com.example.talk.utilites.APP_ACTIVITY
import com.example.talk.utilites.AUTH
import com.example.talk.utilites.CHILD_PHOTO_URL
import com.example.talk.utilites.NODE_USERS
import com.example.talk.utilites.PROFILE_FOLDER_IMAGE
import com.example.talk.utilites.REF_DATABASE_ROOT
import com.example.talk.utilites.REF_STORAGE_ROOT
import com.example.talk.utilites.UID
import com.example.talk.utilites.USER
import com.example.talk.utilites.downloadAndSetImage
import com.example.talk.utilites.getUrlFromStorage
import com.example.talk.utilites.putImageToStorage
import com.example.talk.utilites.putUrlToDatabase
import com.example.talk.utilites.replaceActivity
import com.example.talk.utilites.replaceFragment
import com.example.talk.utilites.showToast
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private lateinit var settings_text_username: TextView
    private lateinit var settings_phone_text: TextView
    private lateinit var settings_bio_text: TextView
    private lateinit var settings_user_status: TextView
    private lateinit var settings_text_fullname: TextView
    private lateinit var settings_btn_change_username: ConstraintLayout
    private lateinit var settings_btn_change_bio: ConstraintLayout
    private lateinit var settings_change_photo: CircleImageView
    private lateinit var settings_user_photo: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        settings_text_username = view.findViewById(R.id.settings_text_username)
        settings_phone_text = view.findViewById(R.id.settings_phone_text)
        settings_bio_text = view.findViewById(R.id.settings_bio_text)
        settings_user_status = view.findViewById(R.id.settings_user_status)
        settings_text_fullname = view.findViewById(R.id.settings_text_fullname)
        settings_btn_change_username = view.findViewById(R.id.settings_btn_change_username)
        settings_btn_change_bio = view.findViewById(R.id.settings_btn_change_bio)
        settings_change_photo = view.findViewById(R.id.settings_change_photo)
        settings_user_photo = view.findViewById(R.id.settings_user_photo)
        return view
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_text_username.text = USER.username
        settings_phone_text.text = USER.phone
        settings_bio_text.text = USER.bio
        settings_user_status.text = USER.state
        settings_text_fullname.text = USER.fullname
        settings_btn_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_change_photo.setOnClickListener { changePhotoUser() }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri.normalizeScheme()
            val path = REF_STORAGE_ROOT.child(PROFILE_FOLDER_IMAGE)
                .child(UID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.settings_name_changed))
                        USER.photoURL = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }

            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }
}