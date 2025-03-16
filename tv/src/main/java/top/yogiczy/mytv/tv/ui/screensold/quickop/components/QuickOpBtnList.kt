package top.yogiczy.mytv.tv.ui.screensold.quickop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

@Composable
fun QuickOpBtnList(
    modifier: Modifier = Modifier,
    playerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    onShowEpg: () -> Unit = {},
    onShowChannelLine: () -> Unit = {},
    onShowVideoPlayerController: () -> Unit = {},
    onShowVideoPlayerDisplayMode: () -> Unit = {},
    onShowVideoTracks: () -> Unit = {},
    onShowAudioTracks: () -> Unit = {},
    onShowSubtitleTracks: () -> Unit = {},
    onShowMoreSettings: () -> Unit = {},
    onShowDashboardScreen: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    val playerMetadata = playerMetadataProvider()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
    ) {
        item {
            QuickOpBtn(
                modifier = Modifier.focusOnLaunched(),
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Tv)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("节目单") 
                    }
                },
                onSelect = onShowEpg,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Satellite)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("播放源") 
                    }
                },
                onSelect = onShowChannelLine,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayArrow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("播放控制")
                    }
                },
                onSelect = onShowVideoPlayerController,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AspectRatio)
                        Spacer(modifier = Modifier.width(4.dp)) 
                        Text("显示模式") 
                    }
                },
                onSelect = onShowVideoPlayerDisplayMode,
            )
        }

        if (playerMetadata.videoTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.VideoLibrary)
                            Spacer(modifier = Modifier.width(4.dp)) 
                            Text("视轨") 
                        }
                    },
                    onSelect = onShowVideoTracks,
                )
            }
        }

        if (playerMetadata.audioTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.MusicNote)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("音轨")
                        }
                    },
                    onSelect = onShowAudioTracks,
                )
            }
        }

        if (playerMetadata.subtitleTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Subtitles)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("字幕") 
                        }
                },
                    onSelect = onShowSubtitleTracks,
                )
            }
        }

        item {
            val settingsViewModel = settingsVM

            QuickOpBtn(
                title = { Text(settingsVM.videoPlayerCore.label) },
                onSelect = {
                    settingsViewModel.videoPlayerCore = when (settingsViewModel.videoPlayerCore) {
                        Configs.VideoPlayerCore.MEDIA3 -> Configs.VideoPlayerCore.IJK
                        Configs.VideoPlayerCore.IJK -> Configs.VideoPlayerCore.MEDIA3
                    }
                },
            )
        }

        item {
            QuickOpBtn(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) { 
                        Icon(Icons.Filled.ClearAll)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("清除缓存")
                    }
                },
                onSelect = onClearCache,
            )
        }

        item{
            QuickOpBtn(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Home)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("主页") 
                    }
                },
                onSelect = onShowDashboardScreen,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Settings)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("更多设置") 
                    }
                },
                onSelect = onShowMoreSettings,
            )
        }
    }
}

@Preview
@Composable
private fun QuickOpBtnListPreview() {
    MyTvTheme {
        QuickOpBtnList()
    }
}