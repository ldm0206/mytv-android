package top.yogiczy.mytv.core.data.repositories.git.parser

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.yogiczy.mytv.core.data.entities.git.GitRelease
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.tv.MainActivity

/**
 * github发行版解析
 */
class GithubGitReleaseParser : GitReleaseParser {
    override fun isSupport(url: String): Boolean {
        return url.contains("github.com")
    }

    override suspend fun parse(data: String): GitRelease {
        val json = Globals.json.parseToJsonElement(data).jsonObject
        val packageName = MainActivity.PACKAGE_NAME
        var downloadUrl = ""
        if (packageName.contains("com.chinablue.tv")){
            downloadUrl = Constants.GITHUB_PROXY + json.getValue("assets").jsonArray[0].jsonObject["browser_download_url"]!!.jsonPrimitive.content
        }else{
            downloadUrl = Constants.GITHUB_PROXY + json.getValue("assets").jsonArray[1].jsonObject["browser_download_url"]!!.jsonPrimitive.content
        }
        return GitRelease(
            version = json.getValue("tag_name").jsonPrimitive.content.substring(1),
            downloadUrl = downloadUrl,
            description = json.getValue("body").jsonPrimitive.content,
        )
    }
}