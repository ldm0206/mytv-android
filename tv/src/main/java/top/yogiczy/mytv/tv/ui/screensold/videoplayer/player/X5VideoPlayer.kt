package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.data.utils.Loggable

import com.tencent.smtt.sdk.TbsVideo

class X5VideoPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : VideoPlayer(coroutineScope){
    private val logger = Logger.create("X5VideoPlayer")
    private fun prepare(contentType: Int? = null) {
        if(TbsVideo.canUseTbsPlayer(context)){
            logger.i("X5VideoPlayer: TbsVideo can be used")
            triggerPrepared()
        } else {
            logger.i("X5VideoPlayer: TbsVideo cannot be used")
        }
    }
    
    override fun prepare(line: ChannelLine) {
        TbsVideo.openVideo(context, line.playableUrl)
    }
}
