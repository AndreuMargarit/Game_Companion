package com.andreumargarit.gamecompanion.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.andreumargarit.gamecompanion.Fragments.NewsFragment
import com.andreumargarit.gamecompanion.Fragments.ProfileFragment
import com.andreumargarit.gamecompanion.Fragments.StreamFragment
import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Hey!")
            .setMessage("Are you sure you want to leave??")
            .setPositiveButton("Yes!"){dialog, buttonId ->
                finish()
            }
            .setNegativeButton("No!"){dialog, buttonId ->
                dialog.dismiss()
            }.show()
    }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_APPOPEN, null)

        instantiateAdd()

        bottomNavigationView.setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.chat ->{
                    FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_TABCHATCLICK, null)
                }
                R.id.news ->{
                    FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_TABNEWSCLICK, null)
                    val newsFragment = NewsFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, newsFragment)
                    fragmentTransaction.commit()
                }
                R.id.profile ->{
                    FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_TABPROFILECLICK, null)
                    val profileFragment = ProfileFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, profileFragment)
                    fragmentTransaction.commit()
                }
                R.id.Stream ->{
                    FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_TABSTREAMCLICK, null)
                    val streamFragment = StreamFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, streamFragment)
                    fragmentTransaction.commit()
                }
            }
        true
        }

        bottomNavigationView.selectedItemId = R.id.profile
    }

    private fun instantiateAdd(){
        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

}
