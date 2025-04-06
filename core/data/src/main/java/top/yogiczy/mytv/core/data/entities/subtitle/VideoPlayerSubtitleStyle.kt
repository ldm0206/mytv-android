package top.yogiczy.mytv.core.data.entities.subtitle

import android.graphics.Color
import androidx.compose.runtime.Immutable
import androidx.media3.ui.CaptionStyleCompat
/**
 * 频道节目列表
 */

@Immutable
data class VideoPlayerSubtitleStyle(
    val textSize: Float = 1f,
    val style: CaptionStyleCompat = CaptionStyleCompat(
                Color.WHITE,
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_NONE,
                Color.TRANSPARENT,
                null
            ),
) {
    companion object {
        val EXAMPLE = VideoPlayerSubtitleStyle(
            textSize = 1f,
            style = CaptionStyleCompat(
                Color.WHITE,
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_NONE,
                Color.TRANSPARENT,
                null
            )
        )
    }
}