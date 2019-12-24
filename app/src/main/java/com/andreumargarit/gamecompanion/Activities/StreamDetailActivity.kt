package com.andreumargarit.gamecompanion.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.andreumargarit.gamecompanion.Models.GameModel
import com.andreumargarit.gamecompanion.Models.StreamModel
import com.andreumargarit.gamecompanion.Network.TwitchHttpClient
import com.andreumargarit.gamecompanion.R
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_stream_detail.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StreamDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_detail)

        //val game = intent.getSerializableExtra("game") as? GameModel
        lifecycleScope.launch {
            try {
                val stream = intent.getSerializableExtra("stream") as? StreamModel
                val thumbnail: ImageView = detailStreamUserImageView
                Picasso.get().load(stream?.thumbnailUrl).into(thumbnail)
                detailStreamUserNameView.text = stream?.userName
                detailStreamViewersCounterView.text = stream?.viewerCount.toString()
                detailStreamTitleView.text = stream?.title
                detailStreamUserDescriptionView.text = stream?.user?.description
                detailStreamUserTotalViewsCounterView.text = stream?.user?.viewCount.toString()
                val userProfileImage: ImageView = detailStreamUserProfileImageView
                Picasso.get().load(stream?.user?.profileImage).into(userProfileImage)

            } catch (error: IOException) {
                //No response
                Log.w("StreamFragment", "No internet")
            } catch (error: HttpException) {
                Log.w("StreamFragment", error.message())
            }
        }
    }
}
