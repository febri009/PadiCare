package com.example.padicare.blog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.padicare.R
import com.example.padicare.blog.model.Comment
import com.example.padicare.blog.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {
    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val commentButton:ImageView=itemView.findViewById(R.id.commentButton)
        val postImageImageView:ImageView=itemView.findViewById(R.id.postImageImageView)
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

        viewHolder.likeButton.setOnClickListener{
            val position = viewHolder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLikeClicked(snapshots.getSnapshot(position).id)
            }
        }

        viewHolder.commentButton.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onCommentClicked(snapshots.getSnapshot(position).id)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        //val post = snapshots.getSnapshot(position).toObject(Post::class.java)!!

        holder.userText.text = model.createdBy.displayName
        holder.postText.text = model.text
        Glide.with(holder.postImageImageView.context).load(model.postImageUrl).into(holder.postImageImageView)
        holder.likeCount.text = model.likedBy.size.toString()

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unliked))
        }
    }

}

interface IPostAdapter {
    fun onLikeClicked(postId: String)
    fun onCommentClicked(postId: String)
}