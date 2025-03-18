package top.yogiczy.mytv.core.data.repositories.epg

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlin.collections.MutableList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.network.HttpException
import top.yogiczy.mytv.core.data.network.request
import top.yogiczy.mytv.core.data.repositories.FileCacheRepository
import top.yogiczy.mytv.core.data.repositories.epg.fetcher.EpgFetcher
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.Logger
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.measureTimedValue

/**
 * 节目单获取
 */
class EpgRepository(private val source: EpgSource) :
    FileCacheRepository(source.cacheFileName("json")) {

    private val log = Logger.create("EpgRepository")
    private val epgXmlRepository = EpgXmlRepository(source)

    private fun isExpired(lastModified: Long): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(System.currentTimeMillis()) != dateFormat.format(lastModified)
    }

    private suspend fun refresh(): String {
        val xml = epgXmlRepository.getXml()
        val epgList = EpgParser.fromXml(xml)
        if (epgList.isEmpty()) throw Exception("获取节目单为空")

        return Globals.json.encodeToString(epgList)
    }

    /**
     * 获取节目单列表
     */
    suspend fun getEpgList(): EpgList = withContext(Dispatchers.Default) {
        try {
            val xmlJson = getOrRefresh({ lastModified, _ -> isExpired(lastModified) }) { refresh() }

            return@withContext Globals.json.decodeFromString<EpgList>(xmlJson).also { epgList ->
                log.i("加载节目单（${source.name}）：${epgList.size}个频道，${epgList.sumOf { it.programmeList.size }}个节目")
            }
        } catch (ex: Exception) {
            log.e("加载节目单（${source.name}）失败", ex)
            throw ex
        }
    }

    override suspend fun clearCache() {
        epgXmlRepository.clearCache()
        super.clearCache()
    }

    companion object {
        suspend fun clearAllCache() = withContext(Dispatchers.IO) {
            EpgSource.cacheDir.deleteRecursively()
        }
    }
}

/**
 * 节目单xml获取
 */
private class EpgXmlRepository(private val source: EpgSource) :
    FileCacheRepository(source.cacheFileName("xml")) {

    private val log = Logger.create("EpgXmlRepository")

    suspend fun getXml(): List<InputStream> {
        log.i("开始获取节目单（${source.name}）")
        val urls = source.urls.split(",")
        val xmlStreams : MutableList<InputStream> = mutableListOf()
        for (url_raw in urls) {
            val url = url_raw.trim()
            try {
                val xmlStream = getOrRefreshInputStream(0) {
                    log.i("开始获取节目单（${source.name}）xml: $url")
                    val t = measureTimedValue {
                        url.request { response, _ ->
                            response.body!!.byteStream()
                        }
                    }
                    log.i("获取节目单（${source.name}）xml : $url 成功", null, t.duration)
                    t.value
                }as InputStream
                if (xmlStream != null) {
                    xmlStreams.add(xmlStream)
                }
            } catch (ex: Exception) {
                log.e("获取节目单（${source.name}）xml : $url 失败", ex)
            }
        }
        return xmlStreams.toList()
    }
}

