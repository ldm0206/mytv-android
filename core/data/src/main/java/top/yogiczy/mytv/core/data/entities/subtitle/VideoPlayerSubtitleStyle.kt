package top.yogiczy.mytv.core.data.entities.subtitle

import android.graphics.Color
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 频道节目列表
 */
@Serializable
@Immutable
data class VideoPlayerSubtitleStyle(
    val textSize: Float = 1f,
    val foregroundColor: Int = Color.WHITE,
    val backgroundColor: Int = Color.BLACK,
    val outlineColor: Int = Color.BLACK,
    val outlineWidth: Float = 0.5f,
) {
    companion object {
        val EXAMPLE = VideoPlayerSubtitleStyle(
            textSize = 1f,
            foregroundColor = Color.WHITE,
            backgroundColor = Color.BLACK,
            outlineColor = Color.BLACK,
            outlineWidth = 0.5f,
        )
    }
}