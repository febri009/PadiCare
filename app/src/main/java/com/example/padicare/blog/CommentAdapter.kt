package com.example.padicare.blog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.padicare.R
import com.example.padicare.blog.model.Comment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CommentAdapter(options: FirestoreRecyclerOptions<Comment>) : FirestoreRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder>(
    options
) {

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val commentUserName:TextView=itemView.findViewById(R.id.commentedByUserName)
        val createdTime:TextView=itemView.findViewById(R.id.createdTime)
        val commentText:TextView=itemView.findViewById(R.id.commentTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val viewHolder =  CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comment) {
        holder.commentText.text = model.comment
        holder.commentUserName.text = model.displayName
        holder.createdTime.text = Utils.getTimeAgo(model.createdAt)
    }
}