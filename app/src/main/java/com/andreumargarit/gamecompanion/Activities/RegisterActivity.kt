package com.andreumargarit.gamecompanion.Activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var passHiden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        


        Login.setOnClickListener {
            Login.setPaintFlags(Login.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            //startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
            //TODO: strings.xml i dimens

        ShowPasswordButton.setOnClickListener {
            if(passHiden) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance();
                passHiden = false;
                ShowPasswordButton.setBackgroundResource(R.drawable.icon_password_show);
            }
            else {
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
                //Show error
                //usernameEditText.error = getString(R.string.register_error_invalid_username)
                Toast.makeText(usernameEditText.context, getString(R.string.register_error_invalid_username), Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            if(email.isBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(emailEditText.context, getString(R.string.register_login_error_invalid_email), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(password.isBlank() && !PasswordValid(password))
            {
                Toast.makeText(passwordEditText.context, "Password must be 4 characters and contain, at least, 1 letter and 1 digit", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //Send to firebase
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {authResult ->
                    //Success
                    //ctrlActivityIndicator.visibility = View.GONE;
                    Toast.makeText(emailEditText.context, "User Created Successfully", Toast.LENGTH_LONG).show()

                    //create user profile
                    val user = UserModel(
                        userName,
                        email,
                        authResult.user?.uid
                    )
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(authResult.user?.uid ?: "")
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(usernameEditText.context, "username created successfully", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(usernameEditText.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    //Close Activity
                    finish()
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
