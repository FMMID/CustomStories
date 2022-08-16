package com.app.stories

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.app.stories.segmented_progress_bar.SegmentParams
import com.app.stories.segmented_progress_bar.SegmentProgressBar

object StoriesUtils {

    private const val SEGMENT_PROGRESS_BAR_ID = 10101

    fun initSegmentBar(
        rootView: ConstraintLayout,
        segmentParams: SegmentParams = SegmentParams(
            segmentBackgroundColor = ContextCompat.getColor(rootView.context, R.color.carousel_default_segment),
            segmentSelectedBackgroundColor = ContextCompat.getColor(rootView.context, R.color.carousel_default_selected),
            segmentStrokeColor = ContextCompat.getColor(rootView.context, R.color.white),
        ),
        segmentBarHeight: Int = 20
    ): SegmentProgressBar {

        segmentParams.segmentCount = segmentParams.segmentCount ?: 4
        val segmentProgressBar = SegmentProgressBar(rootView.context, segmentParams).apply {
            this.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, segmentBarHeight)
            id = SEGMENT_PROGRESS_BAR_ID
        }
        rootView.addView(segmentProgressBar)
        ConstraintSet().apply {
            clone(rootView)
            segmentProgressBar.let {
                connect(it.id, ConstraintSet.TOP, rootView.id, ConstraintSet.TOP, 15)
                connect(it.id, ConstraintSet.END, rootView.id, ConstraintSet.END, 15)
                connect(it.id, ConstraintSet.START, rootView.id, ConstraintSet.START, 15)
            }
            applyTo(rootView)
        }
        return segmentProgressBar
    }

    fun getStoriesDuration(context: Context, stories: List<Story>): List<Long> {
        return stories.map { story ->
            when (story) {
                is Story.VideoStory -> {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(context, story.uriVideo)
                    val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    retriever.release()
                    time?.toLong() ?: 3600
                }
                is Story.ImageStory -> {
                    story.duration
                }
            }
        }
    }
}