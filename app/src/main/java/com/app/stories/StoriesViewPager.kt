package com.app.stories

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class StoriesViewPagerAdapter(val stories: List<Story>, private val segmentProgressBarCallback: (StoriesEvent) -> Unit) :
    RecyclerView.Adapter<StoriesViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewPagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stories_view_pager_item, parent, false)
        return StoriesViewPagerViewHolder(itemView, segmentProgressBarCallback)
    }

    override fun onBindViewHolder(holder: StoriesViewPagerViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}

@SuppressLint("ClickableViewAccessibility")
class StoriesViewPagerViewHolder(
    itemView: View,
    private val segmentProgressBarCallback: (StoriesEvent) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val videoPlaceholder: VideoView = itemView.findViewById(R.id.stories_view_pager_item_video_view)
    private val imagePlaceholder: ImageView = itemView.findViewById(R.id.stories_view_pager_item_image_view)
    private var mediaPlayer: MediaPlayer? = null

    private val onTouchListener = object : View.OnTouchListener {
        private var timeDown = 0L
        private var timeUp = 0L
        private val LONG_PRESS_TIME = 1000L

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    timeDown = System.currentTimeMillis()
                    segmentProgressBarCallback.invoke(StoriesEvent.OnPauseEvent)
                    if (videoPlaceholder.isVisible) {
                        mediaPlayer?.pause()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    timeUp = System.currentTimeMillis()
                    if (timeUp - timeDown < LONG_PRESS_TIME) {
                        when {
                            (motionEvent.x >= (itemView.width - itemView.width / 4)) -> {
                                segmentProgressBarCallback.invoke(StoriesEvent.OnChangePositionEvent(adapterPosition + 1))
                            }
                            (motionEvent.x <= itemView.width / 4) -> {
                                segmentProgressBarCallback.invoke(StoriesEvent.OnChangePositionEvent(adapterPosition - 1))
                            }
                            else -> {
                                segmentProgressBarCallback.invoke(StoriesEvent.OnStartEvent)
                                mediaPlayer?.start()
                            }
                        }
                    } else {
                        segmentProgressBarCallback.invoke(StoriesEvent.OnStartEvent)
                        if (videoPlaceholder.isVisible) {
                            mediaPlayer?.start()
                        }
                    }
                }
            }
            return true
        }
    }

    init {
        videoPlaceholder.setOnPreparedListener {
            mediaPlayer = it
        }

        videoPlaceholder.setOnTouchListener(onTouchListener)

        imagePlaceholder.setOnTouchListener(onTouchListener)
    }

    fun bind(story: Story) {
        when (story) {
            is Story.VideoStory -> {
                imagePlaceholder.isVisible = false
                videoPlaceholder.isVisible = true
                videoPlaceholder.setVideoURI(story.uriVideo)
                videoPlaceholder.start()
            }
            is Story.ImageStory -> {
                imagePlaceholder.isVisible = true
                videoPlaceholder.isVisible = false
                imagePlaceholder.setImageURI(story.uriImage)
            }
        }
    }
}

sealed class StoriesEvent {
    object OnPauseEvent : StoriesEvent()
    object OnStartEvent : StoriesEvent()
    data class OnChangePositionEvent(val position: Int) : StoriesEvent()
}