package top.yogiczy.mytv.tv.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.ViewCozy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.tv.ui.material.LazyRow
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screen.Screens
import top.yogiczy.mytv.tv.ui.screen.about.AboutScreen
import top.yogiczy.mytv.tv.ui.screen.agreement.AgreementScreen
import top.yogiczy.mytv.tv.ui.screen.channels.ChannelsScreen
import top.yogiczy.mytv.tv.ui.screen.dashboard.DashboardScreen
import top.yogiczy.mytv.tv.ui.screen.favorites.FavoritesScreen
import top.yogiczy.mytv.tv.ui.screen.loading.LoadingScreen
import top.yogiczy.mytv.tv.ui.screen.multiview.MultiViewScreen
import top.yogiczy.mytv.tv.ui.screen.push.PushScreen
import top.yogiczy.mytv.tv.ui.screen.search.SearchScreen
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsScreen
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsSubCategories
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM

@Composable
fun DashboardModuleList(
    modifier: Modifier = Modifier,
    toLiveScreen: () -> Unit = {},
    toChannelsScreen: () -> Unit = {},
    toFavoritesScreen: () -> Unit = {},
    toSearchScreen: () -> Unit = {},
    toMultiViewScreen: () -> Unit = {},
    toPushScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    toAboutScreen: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
    ) { runtime ->
        item {
            DashboardModuleItem(
                modifier = Modifier
                    .focusRequester(runtime.firstItemFocusRequester)
                    .handleKeyEvents(onLeft = { runtime.scrollToLast() }),
                imageVector = Icons.Outlined.Tv,
                title = "直播",
                onSelected = toLiveScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.GridView,
                title = "全部频道",
                onSelected = toChannelsScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.FavoriteBorder,
                title = "收藏",
                onSelected = toFavoritesScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.Search,
                title = "搜索",
                onSelected = toSearchScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.ViewCozy,
                title = "多屏同播",
                onSelected = toMultiViewScreen,
                //tag = "BETA",
            )
        }

        /*item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.InsertChart,
                title = "观看统计",
                tag = "UNDO",
            )
        }*/

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.CloudUpload,
                title = "推送",
                onSelected = toPushScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.Settings,
                title = "设置",
                onSelected = toSettingsScreen,
            )
        }

        item {
            DashboardModuleItem(
                modifier = Modifier
                    .focusRequester(runtime.lastItemFocusRequester)
                    .handleKeyEvents(onRight = { runtime.scrollToFirst() }),
                imageVector = Icons.Outlined.Info,
                title = "关于",
                onSelected = toAboutScreen,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun DashboardModuleListPreview() {
    MyTvTheme {
        AppScreen {
            DashboardModuleList(
                modifier = Modifier.padding(vertical = 20.dp),
            )
        }
        // PreviewWithLayoutGrids { }
    }
}