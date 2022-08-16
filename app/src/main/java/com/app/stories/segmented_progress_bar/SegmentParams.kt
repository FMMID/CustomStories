package com.app.stories.segmented_progress_bar

/**
 * Params for segment bar carousel.
 * Colors need set through getColor()
 * **/
data class SegmentParams(

    var segmentCount: Int? = null,

    var margin: Int? = null,

    var radius: Int? = null,

    var duration: List<Long>? = null,

    var segmentStrokeWidth: Int? = null,

    var segmentBackgroundColor: Int? = null,

    var segmentSelectedBackgroundColor: Int? = null,

    var segmentStrokeColor: Int? = null,

    var segmentSelectedStrokeColor: Int? = null
)