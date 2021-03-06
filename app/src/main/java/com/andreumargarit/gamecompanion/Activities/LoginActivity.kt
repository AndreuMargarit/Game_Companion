package com.andreumargarit.gamecompanion.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.andreumargarit.gamecompanion.Models.UserModel
import com.andreumargarit.gamecompanion.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.ShowPasswordButton
import kotlinx.android.synthetic.main.activity_register.emailEditText
import kotlinx.android.synthetic.main.activity_register.passwordEditText
import kotlinx.android.synthetic.main.fragment_profile.*

class LoginActivity : AppCompatActivity() {

    var passHiden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance();
        loginCtrlActivityIndicator.visibility = View.GONE

        ShowPasswordButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent("LoginPasswordButtonClick", null)
            if (passHiden) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance();
                passHiden = false;
                ShowPasswordButton.setBackgroundResource(R.drawable.icon_password_show);
            } else {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance();
                passHiden = true;
                ShowPasswordButton.setBackgroundResource(R.drawable.icon_password_hiden);
            }
        }
            activityLoginButton.setOnClickListener {
                //Email and password
                val password = passwordEditText.text.toString();
                val email = emailEditText.text.toString();

                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance();


                if(email.isBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    FirebaseAnalytics.getInstance(this).logEvent("WrongEmailLogin", null)
                    Toast.makeText(emailEditText.context, getString(R.string.register_login_error_invalid_email), Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if(password.isBlank() && !PasswordValid(password))
                {
                    FirebaseAnalytics.getInstance(this).logEvent("WrongPasswordLogin", null)
                    Toast.makeText(passwordEditText.context, "Password must be 4 characters and contain, at least, 1 letter and 1 digit", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                loginCtrlActivityIndicator.visibility = View.VISIBLE

                //Send to firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {authResult ->
                        //Success
                        Toast.makeText(emailEditText.context, "Welcome back", Toast.LENGTH_LONG).show()
                        FirebaseAnalytics.getInstance(this).logEvent("LoginSuccessful", null)
                        //Close Activity
                        finish()
                    }
                    .addOnFailureListener{exception ->
                        FirebaseAnalytics.getInstance(this).logEvent("LoginFailure", null)
                        if(exception is FirebaseAuthInvalidUserException)
                            emailEditText.error = getString(R.string.register_login_error_invalid_email)
                        else if(exception is FirebaseAuthInvalidCredentialsException)
                            passwordEditText.error = getString(R.string.login_error_invalid_password)
                        else
                            Toast.makeText(loginButton.context, exception.localizedMessage, Toast.LENGTH_LONG).show()

                        loginCtrlActivityIndicator.visibility = View.GONE
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
