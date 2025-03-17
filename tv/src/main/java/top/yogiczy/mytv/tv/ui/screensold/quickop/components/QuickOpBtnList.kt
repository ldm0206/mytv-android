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
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.ControlCamera
import androidx.compose.material.icons.outlined.SmartDisplay

import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ClearAll
import androidx.tv.material3.Icon
import androidx.compose.ui.unit.dp

@Composable
fun QuickOpBtnList(
    modifier: Modifier = Modifier,
    playerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
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
    val settingsViewModel = settingsVM
    var currentVideoTrack = ""
    var currentAudioTrack = ""
    var currentSubtitleTrack = ""
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }
    if (playerMetadata.videoTracks.isNotEmpty()) {
        for (videoTrack in playerMetadata.videoTracks) {
            if (videoTrack.isSelected == true) {
                currentVideoTrack = videoTrack.width.toString() + "x" + videoTrack.height.toString() + "," + audioTrack.mimeType.toString() + "," + videoTrack.decoder.toString()
                break
            }
        }
    }
    if (playerMetadata.audioTracks.isNotEmpty()) {
        for (audioTrack in playerMetadata.audioTracks) {
            if (audioTrack.isSelected == true) {
                currentAudioTrack = audioTrack.mimeType.toString() + "," + audioTrack.decoder.toString()
                break
            }
        }
    }
    if (playerMetadata.subtitleTracks.isNotEmpty()) {
        for (subtitleTrack in playerMetadata.subtitleTracks) {
            if (subtitleTrack.isSelected == true) {
                currentSubtitleTrack = subtitleTrack.mimeType.toString() + "," + subtitleTrack.language.toString()
                break
            }
        }
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
                        Icon(Icons.AutoMirrored.Outlined.LibraryBooks, contentDescription = "图标")
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
                        Icon(Icons.Outlined.LiveTv, contentDescription = "图标")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("线路" + (currentChannelLineIdxProvider() + 1).toString()) 
                    }
                },
                onSelect = onShowChannelLine,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ControlCamera, contentDescription = "图标")
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
                        Icon(Icons.Filled.AspectRatio, contentDescription = "图标")
                        Spacer(modifier = Modifier.width(4.dp)) 
                        Text(settingsViewModel.videoPlayerDisplayMode.label) 
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
                            Icon(Icons.Filled.VideoLibrary, contentDescription = "图标")
                            Spacer(modifier = Modifier.width(4.dp)) 
                            Text(currentVideoTrack) 
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
                            Icon(Icons.Filled.MusicNote, contentDescription = "图标")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(currentAudioTrack)
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
                            Icon(Icons.Filled.Subtitles, contentDescription = "图标")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(currentSubtitleTrack) 
                        }
                },
                    onSelect = onShowSubtitleTracks,
                )
            }
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.SmartDisplay, contentDescription = "图标")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text( "播放器："+settingsViewModel.videoPlayerCore.label) 
                    }
                },
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
                        Icon(Icons.Filled.ClearAll, contentDescription = "图标")
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
                        Icon(Icons.Filled.Home, contentDescription = "图标")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("主页面") 
                    }
                },
                onSelect = onShowDashboardScreen,
            )
        }

        item {
            QuickOpBtn(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Settings, contentDescription = "图标")
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