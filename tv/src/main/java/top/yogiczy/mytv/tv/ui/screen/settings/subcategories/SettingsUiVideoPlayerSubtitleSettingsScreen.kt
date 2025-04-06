package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.SubtitleView
import androidx.media3.ui.CaptionStyleCompat
import androidx.media3.common.text.Cue
import androidx.compose.ui.graphics.Color
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.core.data.entities.subtitle.VideoPlayerSubtitleStyle
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.text.DecimalFormat

@Composable
fun SettingsUiVideoPlayerSubtitleSettingsScreen(
    modifier: Modifier = Modifier,
    subtitleSettingsProvider: () -> VideoPlayerSubtitleStyle = { Configs.uiVideoPlayerSubtitle },
    onSubtitleSettingsChanged: (VideoPlayerSubtitleStyle) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    var currentSubtitleSettings = subtitleSettingsProvider()

    val childPadding = rememberChildPadding()

    val textSize = remember { mutableStateOf(currentSubtitleSettings.textSize) }
    val foregroundColor = remember { mutableStateOf(currentSubtitleSettings.style.foregroundColor) }
    val backgroundColor = remember { mutableStateOf(currentSubtitleSettings.style.backgroundColor) }
    val edgeColor = remember { mutableStateOf(currentSubtitleSettings.style.edgeColor) }
    val windowColor = remember { mutableStateOf(currentSubtitleSettings.style.windowColor) }

    fun updateSubtitleSettings() {
        currentSubtitleSettings = VideoPlayerSubtitleStyle(
            textSize = textSize.value,
            style = CaptionStyleCompat(
                foregroundColor.value,
                backgroundColor.value,
                windowColor.value,
                CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                edgeColor.value,
                null // 字体类型可以设置为 null
            )
        )
        onSubtitleSettingsChanged(currentSubtitleSettings)
    }

    AppScreen(
        modifier = Modifier.padding(top = 10.dp),
        header = { Text("设置 / 界面 / 字幕设置") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        Box(
        modifier = modifier
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("字体颜色", style = MaterialTheme.typography.bodyMedium)
                ColorPicker(
                    selectedColor = foregroundColor.value,
                    onColorSelected = { color ->
                        foregroundColor.value = color
                        updateSubtitleSettings()
                    }
                )

                Text("背景颜色", style = MaterialTheme.typography.bodyMedium)
                ColorPicker(
                    selectedColor = backgroundColor.value,
                    onColorSelected = { color -> 
                        backgroundColor.value = color
                        updateSubtitleSettings()
                    }
                )

                Text("边框颜色", style = MaterialTheme.typography.bodyMedium)
                ColorPicker(
                    selectedColor = edgeColor.value,
                    onColorSelected = { 
                        color -> edgeColor.value = color
                        updateSubtitleSettings()
                    }
                )

                Text("窗口颜色", style = MaterialTheme.typography.bodyMedium)
                ColorPicker(
                    selectedColor = windowColor.value,
                    onColorSelected = { color -> 
                        windowColor.value = color
                        updateSubtitleSettings()
                    }
                )
                Text("预览", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                AndroidView(
                    factory = { SubtitleView(it) },
                    update = { subtitleView ->
                        subtitleView.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * textSize.value)
                        subtitleView.setStyle(currentSubtitleSettings.style)
                        val exampleCue = Cue.Builder()
                            .setText("示例字幕") // 设置字幕内容
                            .build()
                        subtitleView.setCues(listOf(exampleCue)) // 将字幕内容应用到 SubtitleView
                    }
                )
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Int,
    onColorSelected: (Int) -> Unit
) {
    // 简单的颜色选择器实现
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black, Color.White, Color.Transparent).forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color)
                    .border(2.dp, Color(0xFFFFD700)) // 添加金色边框
                    .clickable { onColorSelected(color.toArgb()) }
            )
        }
    }
}