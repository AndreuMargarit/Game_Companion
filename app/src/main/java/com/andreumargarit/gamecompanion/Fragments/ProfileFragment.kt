package com.andreumargarit.gamecompanion.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andreumargarit.gamecompanion.Activities.LoginActivity
import com.andreumargarit.gamecompanion.Activities.RegisterActivity
import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.andreumargarit.gamecompanion.Utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        initUI();
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initUI()
    {
        if(FirebaseAuth.getInstance().currentUser == null)
        {
            profileLoggedLayout.visibility = View.GONE
            profileNonLoggedLayout.visibility = View.VISIBLE

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            //googleApu

            registerButton.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }

            loginButton.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }

            registerGoogleLogin.setOnClickListener{
                Toast.makeText((requireContext()), "Hi", Toast.LENGTH_LONG).show()
            }

        }
        else
        {
            profileLoggedLayout.visibility = View.VISIBLE
            profileNonLoggedLayout.visibility = View.GONE

            logOutButton.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                //Toast
                initUI()
            };

            FirebaseAuth.getInstance().currentUser?.uid?.let {userID ->
                val username = requireContext().getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE).getString(Constants.FIELD_USERNAME, "")
                usernameTextView.text = username

                val userPhoto = requireContext().getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE).getString(Constants.FIELD_USERPHOTO, "")

                /*if(userPhoto != "")
                {
                    val imageBytes = Base64.decode(userPhoto, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    avatarImageView.setImageBitmap(image)
                }*/

                UserDao().get(
                    UserId = userID,
                    successListener = {user ->
                        usernameTextView.text = user?.userName
                        requireContext().getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE)
                            .edit().putString(Constants.FIELD_USERNAME, user?.userName)
                            .apply()
                    },
                    failureListener = {
                        Log.w("ProfileFragment", it)
                    })
            }
        }

        avatarImageView.setOnClickListener {
            FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.ANALYTICEVENT_PROFILEPICTRETAKEN, null)
            TakePicture()
        }
    }

    private fun getAllUsers()
    {
        UserDao().getAll(successListener = {users ->
                //adapter.users = users

            },
            failureListener = {
                Toast.makeText((requireContext()), it.localizedMessage, Toast.LENGTH_LONG).show();
        })
        UserDao().get(UserId = "123242wer",
            successListener = {

        },
            failureListener = {

            }
        )
    }

    private fun TakePicture()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        activity.let {
            cameraIntent.resolveActivity(requireActivity().packageManager)
            startActivityForResult(cameraIntent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK)
        {//Camera result
            val image = data?.extras?.get(Constants.FIELD_DATA) as? Bitmap
            image?.let {
                avatarImageView.setImageBitmap(it)
                //Save image to cloud
                SaveImageToCloud(it)
                //Save image to Shared Preferences
                SaveProfileImageToSharedPreferences(it)
            }
        }
    }

    private  fun SaveImageToCloud(bitmap: Bitmap)
    {//Create
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val folderReference = FirebaseStorage.getInstance().reference.child(Constants.FOLDER_AVATARS)
        val imageReference = folderReference.child("avatar.jpg") //TODO: unique name
        imageReference.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                Log.i("Profile Fragment", getString(R.string.profile_success_uploading_image_message))
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                }
            }
            .addOnFailureListener{
                Log.w("Profile Fragment", getString(R.string.profile_error_uploading_image_message))
                it.printStackTrace()
            }
    }

    private fun SaveProfileImageToSharedPreferences(bitmap: Bitmap)
    {
        val baosPhoto = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baosPhoto)
        val imagePhoto = baosPhoto.toByteArray()
        val img_strPhoto: String = Base64.encodeToString(imagePhoto, 0);

        requireContext().getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE)
            .edit().putString(Constants.FIELD_USERPHOTO, img_strPhoto)
            .apply()
    }
}
