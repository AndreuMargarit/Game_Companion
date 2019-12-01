package com.andreumargarit.gamecompanion.Activities

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Models.UserModel
import com.andreumargarit.gamecompanion.Utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.registerButton
import kotlinx.android.synthetic.main.fragment_profile.*

class RegisterActivity : AppCompatActivity() {

    var passHiden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ctrlActivityIndicator.visibility = View.GONE

        Login.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_LOGINCLICKONREGISTERSCENE, null)
            Login.setPaintFlags(Login.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
            //TODO: strings.xml i dimens

        ShowPasswordButton.setOnClickListener {

            FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_REGISTERPASSWORDBUTTONCLICK, null)

            if(passHiden)
            {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance();
                passHiden = false;
                ShowPasswordButton.setBackgroundResource(R.drawable.icon_password_show);
            }
            else
            {
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance();
                passHiden = true;
                ShowPasswordButton.setBackgroundResource(R.drawable.icon_password_hiden);
            }

        }
        registerButton.setOnClickListener {
            //Username, email and password
            val userName = usernameEditText.text.toString();
            val password = passwordEditText.text.toString();
            val email = emailEditText.text.toString();

            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance();

            if(userName.isBlank())
            {
                FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_WRONGUSERNAMEREGISTER, null)
                //Show error
                //usernameEditText.error = getString(R.string.register_error_invalid_username)
                Toast.makeText(usernameEditText.context, getString(R.string.register_error_invalid_username), Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            if(email.isBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_WRONGEMAILREGISTER, null)
                Toast.makeText(emailEditText.context, getString(R.string.register_login_error_invalid_email), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(password.isBlank() && !PasswordValid(password))
            {
                FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_WRONGPASSWORDREGISTER, null)
                Toast.makeText(passwordEditText.context, getString(R.string.register_error_password), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            ctrlActivityIndicator.visibility = View.VISIBLE

            //Send to firebase
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {authResult ->
                    //Success
                    Toast.makeText(emailEditText.context, getString(R.string.register_user_created), Toast.LENGTH_LONG).show()

                    getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE)
                        .edit().putString(Constants.FIELD_EMAIL, email)
                        .apply()

                    getSharedPreferences(Constants.FIELD_USERPROFILE, Context.MODE_PRIVATE)
                        .edit().putString(Constants.FIELD_USERPHOTO, "")
                        .apply()

                    avatarImageView.setImageResource(R.drawable.icon_profile)

                    //create user profile
                    val user = UserModel(
                        userName,
                        email,
                        authResult.user?.uid
                    )

                    FirebaseFirestore.setLoggingEnabled(true)

                    FirebaseFirestore.getInstance()
                        .collection(Constants.FIELD_USERS)
                        .document(authResult.user?.uid ?: "")
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(usernameEditText.context, getString(R.string.register_username_created), Toast.LENGTH_LONG).show()
                            //Close Activity
                            FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_SUCCESSFULLREGISTER, null)
                            finish()
                        }
                        .addOnFailureListener{
                            FirebaseAnalytics.getInstance(this).logEvent(Constants.ANALYTICEVENT_FAILUREREGISTER, null)
                            Toast.makeText(usernameEditText.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                            ctrlActivityIndicator.visibility = View.GONE
                        }
                }
                .addOnFailureListener{
                    Toast.makeText(emailEditText.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun PasswordValid(password: String):Boolean
    {
        if(password.length < 6) return false;

        var hasDigit = false;
        var hasLetter = false;
        for(char in password) {
            if (char.isDigit()) hasDigit = true;
            if(char.isLetter()) hasLetter = true;
        }
        return hasDigit && hasLetter
    }
}
