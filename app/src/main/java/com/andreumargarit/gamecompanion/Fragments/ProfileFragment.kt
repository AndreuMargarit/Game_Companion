package com.andreumargarit.gamecompanion.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        if(FirebaseAuth.getInstance().currentUser == null) {
            registerButton.visibility = View.VISIBLE;
            logOutButton.visibility = View.GONE;
            loginButton.visibility = View.VISIBLE;
            avatarImageView.visibility = View.GONE;
            registerButton.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }
            loginButton.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
        else{
            registerButton.visibility = View.GONE;
            logOutButton.visibility = View.VISIBLE;
            loginButton.visibility = View.GONE;
            logOutButton.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                //Toast
                initUI()
            };
            FirebaseAuth.getInstance().currentUser?.uid?.let {userID ->
                UserDao().get(UserId = userID, successListener = {user -> usernameTextView.text = user?.userName },
                    failureListener = {
                        Log.w("ProfileFragment", it)
                    })
            }
        }
        avatarImageView.visibility = View.VISIBLE;
        avatarImageView.setOnClickListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK)
        {//Camera result
            val image = data?.extras?.get("data") as? Bitmap
            image?.let {
                avatarImageView.setImageBitmap(it)
                //Save image to cloud
                SaveImageToCloud(it)
            }
        }
    }

    private  fun SaveImageToCloud(bitmap: Bitmap)
    {//Create
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val folderReference = FirebaseStorage.getInstance().reference.child("public/images/avatars/")
        val imageReference = folderReference.child("avatar.jpg") //TODO: unique name
        imageReference.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                Log.i("Profile Fragment", "Success uploading image")
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                }
            }
            .addOnFailureListener{
                Log.w("Profile Fragment", "Error uploading image")
                it.printStackTrace()
            }
    }

}
