package top.yogiczy.mytv.tv.ui.screen.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardFavoriteList
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardModuleList
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardTime
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardChannels
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewCozy
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.tv.material3.TabRowDefaults
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Surface

import androidx.compose.runtime.key
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    currentIptvSourceProvider: () -> IptvSource = { IptvSource() },
    channelFavoriteListProvider: () -> ChannelFavoriteList = { ChannelFavoriteList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    filteredChannelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    toLiveScreen: () -> Unit = {},
    toChannelsScreen: () -> Unit = {},
    toFavoritesScreen: () -> Unit = {},
    toSearchScreen: () -> Unit = {},
    toMultiViewScreen: () -> Unit = {},
    toPushScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    toAboutScreen: () -> Unit = {},
    toSettingsIptvSourceScreen: () -> Unit = {},
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val coroutineScope = rememberCoroutineScope()

    AppScreen(
        modifier = modifier,
        header = {
            DashboardScreeIptvSource(
                currentIptvSourceProvider = currentIptvSourceProvider,
                toSettingsIptvSourceScreen = toSettingsIptvSourceScreen,
                clearCurrentIptvSourceCache = {
                    coroutineScope.launch {
                        IptvRepository(Configs.iptvSourceCurrent).clearCache()
                        onReload()
                    }
                },
            )
        },
        headerExtra = { DashboardTime() },
        onBackPressed = onBackPressed,
    ) {
        val tabs = listOf("直播", "频道", "搜索", "多屏同播", "设置")
        val pagerState = rememberPagerState(pageCount = {tabs.size}, initialPage = 0)
        val icons = listOf(Icons.Outlined.Tv, Icons.Outlined.GridView, Icons.Outlined.Search, Icons.Outlined.ViewCozy, Icons.Outlined.Settings)
        var selectedTabIndex by remember { mutableStateOf(0) }
        var activeTabIndex by remember { mutableStateOf(0) }
        var focusedTabIndex by remember { mutableStateOf(0) }
        Box(modifier = modifier.fillMaxSize()) {
            val scope = rememberCoroutineScope()
            TabRow(
                selectedTabIndex = focusedTabIndex,
                indicator = { tabPositions, doesTabRowHaveFocus ->
                    TabRowDefaults.PillIndicator(
                        currentTabPosition = tabPositions[focusedTabIndex],
                        activeColor = Color.Blue.copy(alpha = 0.4f),
                        inactiveColor = Color.Transparent,
                        doesTabRowHaveFocus = doesTabRowHaveFocus,
                    )

                    TabRowDefaults.PillIndicator(
                        currentTabPosition = tabPositions[activeTabIndex],
                        doesTabRowHaveFocus = doesTabRowHaveFocus,
                    )
                },
                modifier = modifier.focusRestorer()
            ) { tabs.forEachIndexed { index, tab ->
                key(index) {
                        Tab(
                            selected = activeTabIndex == index,
                            onFocus = { focusedTabIndex = index },
                            onClick = {
                                focusedTabIndex = index
                                activeTabIndex = index
                                scope.launch {
                                    pagerState.animateScrollToPage(index, 0f)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
            VerticalPager(state = pagerState, modifier.fillMaxHeight(),beyondBoundsPageCount = 2,verticalAlignment=Alignment.Top) {page->
                if (page==0){//直播
                    LazyColumn(
                        contentPadding = PaddingValues(top = 20.dp, bottom = childPadding.bottom),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        item {
                            DashboardFavoriteList(
                                channelFavoriteListProvider = channelFavoriteListProvider,
                                onChannelSelected = onChannelSelected,
                                onChannelUnFavorite = onChannelFavoriteToggle,
                                epgListProvider = epgListProvider,
                            )
                        }
                    }
                }
                else if (page==1){//频道
                    LazyColumn(
                        contentPadding = PaddingValues(top = 20.dp, bottom = childPadding.bottom),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        item {
                            DashboardChannels(
                                channelGroupListProvider = filteredChannelGroupListProvider,
                                onChannelSelected = onChannelSelected,
                                onChannelFavoriteToggle = onChannelFavoriteToggle,
                                epgListProvider = epgListProvider,
                            )
                        }
                }
                }
                else if (page==2){//搜索
                    
                }
                else if (page==3){//多屏同播
                }
                else if (page==4){//设置
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(top = 20.dp, bottom = childPadding.bottom),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                DashboardModuleList(
                    modifier = Modifier.focusOnLaunched(),
                    toLiveScreen = toLiveScreen,
                    toChannelsScreen = toChannelsScreen,
                    toFavoritesScreen = toFavoritesScreen,
                    toSearchScreen = toSearchScreen,
                    toMultiViewScreen = toMultiViewScreen,
                    toPushScreen = toPushScreen,
                    toSettingsScreen = toSettingsScreen,
                    toAboutScreen = toAboutScreen,
                )
            }
        }
    }
}

@Composable
fun DashboardScreeIptvSource(
    modifier: Modifier = Modifier,
    currentIptvSourceProvider: () -> IptvSource = { IptvSource() },
    toSettingsIptvSourceScreen: () -> Unit = {},
    clearCurrentIptvSourceCache: () -> Unit = {},
) {
    val currentIptvSource = currentIptvSourceProvider()

    var isFocused by remember { mutableStateOf(false) }

    val alpha = remember { Animatable(1f) }
    LaunchedEffect(isFocused) {
        if (isFocused) {
            while (true) {
                alpha.animateTo(0.2f, tween(durationMillis = 1000))
                alpha.animateTo(1f, tween(durationMillis = 1000))
            }
        } else {
            alpha.animateTo(1f)
        }
    }

    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(
                onSelect = toSettingsIptvSourceScreen,
                onLongSelect = clearCurrentIptvSourceCache,
            )
            .alpha(alpha.value),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
        shape = ClickableSurfaceDefaults.shape(RectangleShape),
        onClick = {},
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(currentIptvSource.name)
            if (isFocused) Icon(Icons.Default.SyncAlt, contentDescription = null)
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun DashboardScreenScreen() {
    MyTvTheme {
        DashboardScreen(
            currentIptvSourceProvider = { IptvSource(name = "默认播放源1") },
            channelFavoriteListProvider = { ChannelFavoriteList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
        )
        // PreviewWithLayoutGrids { }
    }
}