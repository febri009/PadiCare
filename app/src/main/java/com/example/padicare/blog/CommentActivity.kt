package com.example.padicare.blog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicare.R
import com.example.padicare.blog.dao.CommentDao
import com.example.padicare.blog.model.Comment
import com.example.padicare.databinding.ActivityCommentBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class CommentActivity : AppCompatActivity() {
    private lateinit var commentDao:CommentDao
    private lateinit var adapter:CommentAdapter
    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent
        val postId=intent.getStringExtra("postId")

        binding.chatSendBtn.setOnClickListener {
            val commentText:String= binding.chatMessageView.text.toString()
            if(commentText.isNotEmpty())
            {
                commentDao.addComment(commentText, postId.toString())
                binding.chatMessageView.setText("")
                adapter.notifyDataSetChanged()
                Toast.makeText(this,"Comment Added !",Toast.LENGTH_SHORT).show()
            }
        }
        setUpCommentsRecyclerView(postId.toString())
    }

    fun setUpCommentsRecyclerView(postId:String)
    {
        commentDao = CommentDao()
        val commentsCollections = commentDao.commentsCollections
        val query = commentsCollections
            .document(postId).collection("comments")
            .orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions
            .Builder<Comment>().setQuery(query, Comment::class.java).build()

        adapter = CommentAdapter(recyclerViewOptions)

        binding.messagesList.adapter = adapter
        binding.messagesList.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}