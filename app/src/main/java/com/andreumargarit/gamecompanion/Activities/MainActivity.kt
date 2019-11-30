package com.andreumargarit.gamecompanion.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andreumargarit.gamecompanion.Fragments.NewsFragment
import com.andreumargarit.gamecompanion.Fragments.ProfileFragment
import com.andreumargarit.gamecompanion.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest);

        bottomNavigationView.setOnNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.chat ->{

                }
                R.id.news ->{
                    FirebaseAnalytics.getInstance(this).logEvent("NewsTabClick", null)
                    val newsFragment = NewsFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, newsFragment)
                    fragmentTransaction.commit()
                }
                R.id.profile ->{
                    FirebaseAnalytics.getInstance(this).logEvent("ProfileTabClick", null)
                    val profileFragment = ProfileFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, profileFragment)
                    fragmentTransaction.commit()
                }
                R.id.Stream ->{

                }
            }
        true;
        }
        bottomNavigationView.selectedItemId = R.id.profile
    }

}
