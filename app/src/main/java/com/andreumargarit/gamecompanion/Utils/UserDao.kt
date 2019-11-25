package com.andreumargarit.gamecompanion.Utils

import com.andreumargarit.gamecompanion.Models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.concurrent.Future

class UserDao {

    fun get(
        UserId: String,
        successListener: (user: UserModel?) -> Unit,
        failureListener: (error: Exception) -> Unit
    )
    {
        FirebaseFirestore.getInstance()
            .collection((Constants.COLLECTION_USERS))
            .document(UserId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                successListener(documentSnapshot.toObject(UserModel::class.java))
            }
            .addOnFailureListener {
                failureListener(it)
            }
    }

    fun updateUser(
        user: UserModel,
        successListener: ()-> Unit,
        failureListener: (Error: Exception)-> Unit
    )
    {
        user.userID?.let { userID ->


            FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_USERS)
                .document(user.userID)
                .set(user)
                .addOnSuccessListener { documentSnapshot ->
                    successListener()
                }
                .addOnFailureListener {
                    failureListener(it)
                }
        }?: run {
            failureListener(Exception("User Id was null"))
        }
    }

    fun getAll(successListener: (users: List<UserModel>) -> (Unit), failureListener: (Error: Exception) -> (Unit)){
        FirebaseFirestore.getInstance()
        .collection(Constants.COLLECTION_USERS)
            //Get All Documents
        .get()
            //On Success
        .addOnSuccessListener { querySnapshot ->
            //Query Snapshot contains documents & metadata
            val documents = querySnapshot.documents
            val userList = ArrayList<UserModel>()
            documents.forEach { documentSnapshot->
                //Document snapshot contains data & metadata
                val user = documentSnapshot.toObject(UserModel::class.java)
                user?.let {
                    userList.add(user);

                }
            }
            successListener(userList);
        }
            .addOnFailureListener {
                failureListener(it);
            }

    }
}