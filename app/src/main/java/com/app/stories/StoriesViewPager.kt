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

class StoriesViewPagerAdapter(
    val stories: List<Story>,
    private val segmentProgressBarCallback: (StoriesEvent) -> Unit
) : RecyclerView.Adapter<StoriesViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewPagerViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stories_view_pager_item, parent, false)
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
                    playVideo(play = false)
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
                                playVideo(play = true)
                            }
                        }
                    } else {
                        segmentProgressBarCallback.invoke(StoriesEvent.OnStartEvent)
                        playVideo(play = true)
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
        imagePlaceholder.isVisible = when (story) {
            is Story.VideoStory -> {
                videoPlaceholder.setVideoURI(story.uriVideo)
                videoPlaceholder.start()
                false
            }
            is Story.ImageStory -> {
                imagePlaceholder.setImageURI(story.uriImage)
                true
            }
        }
        videoPlaceholder.isVisible = !imagePlaceholder.isVisible
    }

    /**
     * @param play - true, то попробовать воспроизвести видео, иначе остановить
     */
    private fun playVideo(play: Boolean) {
        when {
            play && videoPlaceholder.isVisible && mediaPlayer?.isPlaying == false -> mediaPlayer?.start()
            !play && videoPlaceholder.isVisible && mediaPlayer?.isPlaying == true -> mediaPlayer?.stop()
        }
    }
}

sealed class StoriesEvent {
    object OnPauseEvent : StoriesEvent()
    object OnStartEvent : StoriesEvent()
    data class OnChangePositionEvent(val position: Int) : StoriesEvent()
}