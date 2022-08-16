package com.app.stories

import android.net.Uri

sealed class Story {
    data class VideoStory(val uriVideo: Uri) : Story()
    data class ImageStory(val uriImage: Uri, val duration: Long) : Story()
}

data class UserStory(
    val name: String,
    val avatar: Uri,
    val stories: List<Story>
)