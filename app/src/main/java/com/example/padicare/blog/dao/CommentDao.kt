package com.example.padicare.blog.dao

import com.example.padicare.blog.model.Comment
import com.example.padicare.blog.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommentDao {
    private val db = FirebaseFirestore.getInstance()
    val commentsCollections = db.collection("posts")
    private val auth = Firebase.auth

    fun addComment(text:String,postId:String)
    {
        GlobalScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            if (userId != null){
                val userDao = UserDao()
                val user = userDao.getUserById(userId).await().toObject(User::class.java)!!

                val currentTime = System.currentTimeMillis()
                val comment=Comment(userId,text,user.displayName,currentTime)

                commentsCollections.document(postId)
                    .collection("comments")
                    .document()
                    .set(comment)
            }
        }
    }
}