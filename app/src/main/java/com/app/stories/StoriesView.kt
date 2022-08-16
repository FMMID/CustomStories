package com.app.stories

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.app.stories.segmented_progress_bar.SegmentParams
import com.app.stories.segmented_progress_bar.SegmentProgressBar
import com.app.stories.segmented_progress_bar.SegmentedProgressBarListener

@SuppressLint("ClickableViewAccessibility")
open class StoriesView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val closeStoryButton: ImageView
    private val userAvatar: ImageView
    private val dataStoryText: TextView
    private val viewPager: ViewPager2

    private var segmentProgressBar: SegmentProgressBar? = null
    var closeStoryCallback: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.stories_layout, this)
        closeStoryButton = findViewById(R.id.stories_close_view)
        dataStoryText = findViewById(R.id.stories_data_view)
        userAvatar = findViewById(R.id.stories_user_avatar_view)
        viewPager = findViewById(R.id.stories_view_pager)

        closeStoryButton.setOnClickListener {
            closeStoryCallback?.invoke()
        }
    }

    fun showStories(
        userStory: UserStory,
        allStoriesDuration: Long? = null,
        segmentParams: SegmentParams? = null,
        heightOfSegmentProgressBar: Int? = null
    ) {

        val duration = if (allStoriesDuration != null) {
            List(userStory.stories.size) { allStoriesDuration }
        } else {
            StoriesUtils.getStoriesDuration(context, userStory.stories)
        }

        userAvatar.setImageURI(userStory.avatar)
        dataStoryText.text = userStory.name

        val segmentParams = segmentParams?.apply {
            this.segmentCount = userStory.stories.size
            this.duration = duration
        } ?: SegmentParams(
            radius = 0,
            segmentCount = userStory.stories.size,
            duration = duration,
            segmentBackgroundColor = ContextCompat.getColor(context, R.color.carousel_default_segment),
            segmentSelectedBackgroundColor = ContextCompat.getColor(context, R.color.carousel_default_selected),
            segmentStrokeColor = ContextCompat.getColor(context, R.color.white),
        )

        segmentProgressBar = StoriesUtils.initSegmentBar(
            rootView = this,
            segmentParams = segmentParams,
            segmentBarHeight = heightOfSegmentProgressBar ?: 20
        )

        segmentProgressBar?.listener = object : SegmentedProgressBarListener {

            override fun onFinished() {

            }

            override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
                viewPager.currentItem = newPageIndex
            }
        }

        val viewPagerAdapter = StoriesViewPagerAdapter(userStory.stories) {
            when (it) {
                StoriesEvent.OnPauseEvent -> {
                    segmentProgressBar?.pause()
                }
                StoriesEvent.OnStartEvent -> {
                    segmentProgressBar?.start()
                }
                is StoriesEvent.OnChangePositionEvent -> {
                    segmentProgressBar?.setPosition(it.position)
                }
            }
        }

        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 1
        segmentProgressBar?.start()
    }
}