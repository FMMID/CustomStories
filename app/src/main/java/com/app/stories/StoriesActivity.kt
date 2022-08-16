package com.app.stories

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class StoriesActivity : AppCompatActivity() {

    lateinit var storiesView: StoriesView
    lateinit var storiesRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        storiesView = findViewById(R.id.stories_view)
        storiesRecycler = findViewById(R.id.stories_recycler)

        storiesView.isVisible = false

        val adapter = StoriesRecyclerAdapter {
            storiesRecycler.isVisible = false
            storiesView.isVisible = true
            storiesView.showStories(it)
        }

        storiesView.closeStoryCallback = {
            storiesView.isVisible = false
            storiesRecycler.isVisible = true
        }

        val stories = listOf(
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.vedio_two)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.clover_logo), duration = 2000),
            Story.VideoStory(uriVideo = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_four)),
            Story.ImageStory(uriImage = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan), duration = 4000),
        )

        val userStory = UserStory(
            name = "Stan",
            avatar = Uri.parse("android.resource://" + packageName + "/" + R.drawable.stan),
            stories = stories
        )

        adapter.setUsers(List(10) { userStory })

        storiesRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        storiesRecycler.adapter = adapter
    }
}