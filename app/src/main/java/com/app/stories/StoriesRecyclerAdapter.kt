package com.app.stories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoriesRecyclerAdapter(private val clickUserStoryCallback: (UserStory) -> Unit) : RecyclerView.Adapter<StoriesViewHolder>() {

    private var users: List<UserStory> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setUsers(users: List<UserStory>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stories_item_user, parent, false)
        return StoriesViewHolder(itemView, clickUserStoryCallback)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size
}

class StoriesViewHolder(itemView: View, val clickUserStoryCallback: (UserStory) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val userAvatarView: ImageView = itemView.findViewById(R.id.stories_item_user_avatar)
    private val userDataView: TextView = itemView.findViewById(R.id.stories_item_user_data)

    fun bind(userStory: UserStory) {
        userAvatarView.setImageURI(userStory.avatar)
        userDataView.text = userStory.name

        itemView.setOnClickListener {
            clickUserStoryCallback.invoke(userStory)
        }
    }
}