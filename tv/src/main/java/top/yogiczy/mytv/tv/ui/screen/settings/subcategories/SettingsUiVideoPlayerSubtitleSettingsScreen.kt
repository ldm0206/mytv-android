package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import android.util.TypedValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import top.yogiczy.mytv.tv.ui.theme.SAFE_AREA_HORIZONTAL_PADDING
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.core.data.entities.subtitle.VideoPlayerSubtitleStyle
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.material.LazyRow

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
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxWidth()
                .padding(SAFE_AREA_HORIZONTAL_PADDING.dp),
        ) {
            ColorPickerSection(
                title = "字体颜色",
                selectedColor = foregroundColor.value,
                onColorSelected = { color ->
                    foregroundColor.value = color
                    updateSubtitleSettings()
                }
            )
            ColorPickerSection(
                title = "背景颜色",
                selectedColor = backgroundColor.value,
                onColorSelected = { color ->
                    backgroundColor.value = color
                    updateSubtitleSettings()
                }
            )
            ColorPickerSection(
                title = "边框颜色",
                selectedColor = edgeColor.value,
                onColorSelected = { color ->
                    edgeColor.value = color
                    updateSubtitleSettings()
                }
            )
            ColorPickerSection(
                title = "窗口颜色",
                selectedColor = windowColor.value,
                onColorSelected = { color ->
                    windowColor.value = color
                    updateSubtitleSettings()
                }
            )
            SizePickerSection(
                title = "字体大小",
                selectedSize = textSize.value,
                onSizeSelected = { size ->
                    textSize.value = size
                    updateSubtitleSettings()
                }
            )
        }
        AndroidView(
            factory = { SubtitleView(it) },
            update = { subtitleView ->
                subtitleView.setFixedTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.value)
                subtitleView.setStyle(currentSubtitleSettings.style)
                val exampleCue = Cue.Builder()
                    .setText("示例字幕") // 设置字幕内容
                    .build()
                subtitleView.setCues(listOf(exampleCue)) // 将字幕内容应用到 SubtitleView
            }
        )
    }
}

@Composable
fun ColorPickerSection(
    title: String,
    selectedColor: Int,
    onColorSelected: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        ColorPicker(
            modifier = Modifier.fillMaxHeight(),
            selectedColor = selectedColor,
            onColorSelected = onColorSelected
        )
    }
}

@Composable
fun SizePickerSection(
    title: String,
    selectedSize: Float,
    onSizeSelected: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        SizePicker(
            modifier = Modifier.fillMaxHeight(),
            selectedSize = selectedSize,
            onSizeSelected = onSizeSelected
        )
    }
}

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    selectedColor: Int,
    onColorSelected: (Int) -> Unit
) {
    // 简单的颜色选择器实现
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(12),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        // verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items( listOf(Color.Red, Color.Magenta, Color.Green, Color.Blue, Color.Cyan, Color.Yellow, 
            Color.Black, Color.DarkGray, Color.Gray, Color.LightGray, Color.White, Color.Transparent)) { color ->
            ListItem(
                modifier = Modifier
                    .handleKeyEvents(onSelect = { onColorSelected(color.toArgb()) })
                    .width(45.dp) 
                    .height(45.dp)
                    .border(2.dp, Color.DarkGray), 

                headlineContent = {
                    if (selectedColor == color.toArgb()) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                trailingContent = {},
                colors = ListItemDefaults.colors(
                    containerColor = color,
                ),
                selected = false,
                onClick = {},
            )
        }
    }
}

@Composable
fun SizePicker(
    modifier: Modifier = Modifier,
    selectedSize: Float,
    onSizeSelected: (Float) -> Unit
) {
    // 简单的大小选择器实现
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(8),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        // verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items((1..8).map { it * 5f }){ size ->
            ListItem(
                modifier = Modifier
                    .handleKeyEvents(onSelect = { onSizeSelected(size) })
                    .width(60.dp) // 设置宽度
                    .height(45.dp), // 设置高度

                headlineContent = {
                    Text(
                        text = String.format("%.0f", size), // 保留 1 位小数
                        textAlign = TextAlign.Center,
                        modifier = Modifier.size(35.dp)
                    )
                    if (selectedSize == size) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                trailingContent = {
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                ),
                selected = false,
                onClick = {},
            )
        }
    }
}
