package com.example.talk.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.talk.R
import com.example.talk.ui.fragments.SettingsFragment
import com.example.talk.utilites.USER
import com.example.talk.utilites.downloadAndSetImage
import com.example.talk.utilites.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer(val mainActivity: AppCompatActivity, val mToolbar: Toolbar) {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfileDrawerItem: ProfileDrawerItem

    fun Create() {
        initLoader()
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        mToolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(mToolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                ProfileDrawerItem().withIdentifier(100)
                    .withName("Создать группу")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_peoples_96),

                ProfileDrawerItem().withIdentifier(101)
                    .withName("Создать секретный чат")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_secret_96),

                ProfileDrawerItem().withIdentifier(102)
                    .withName("Создать канал")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_chanel_96),

                ProfileDrawerItem().withIdentifier(103)
                    .withName("Контакты")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_contacts_96),

                ProfileDrawerItem().withIdentifier(104)
                    .withName("Звонки")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_phone_96),

                ProfileDrawerItem().withIdentifier(105)
                    .withName("Избранное")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_priority_96),

                ProfileDrawerItem().withIdentifier(106)
                    .withName("Настройки")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_settings_96),

                DividerDrawerItem(),

                ProfileDrawerItem().withIdentifier(107)
                    .withName("Пригласить друзей")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_get_user_96),

                ProfileDrawerItem().withIdentifier(108)
                    .withName("Вопросы обо мне")
                    .withSelectable(false)
                    .withIcon(R.drawable.icons8_question_96)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        7 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }
            }).build()
    }

    private fun createHeader() {
        mCurrentProfileDrawerItem = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                mCurrentProfileDrawerItem
            ).build()
    }

    fun updateHeader(){
        mCurrentProfileDrawerItem
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)
        mHeader.updateProfile(mCurrentProfileDrawerItem)
    }

    private fun initLoader(){
        DrawerImageLoader.init(object :AbstractDrawerImageLoader(){
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }
}