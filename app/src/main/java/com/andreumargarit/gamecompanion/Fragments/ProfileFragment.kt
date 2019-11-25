package com.andreumargarit.gamecompanion.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andreumargarit.gamecompanion.Activities.RegisterActivity
import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_profile.*

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
            registerButton.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }
        }
        else{
            registerButton.visibility = View.GONE;
            logOutButton.visibility = View.VISIBLE;
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

}
