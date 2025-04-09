package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.data.utils.Loggable
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine

import com.tencent.smtt.sdk.TbsVideo

class X5VideoPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : VideoPlayer(coroutineScope){
    private val logger = Logger.create("X5VideoPlayer")

    
    override fun prepare(line: ChannelLine) {
        if(TbsVideo.canUseTbsPlayer(context)){
            logger.i("X5VideoPlayer: TbsVideo can be used")
            TbsVideo.openVideo(context, line.playableUrl)
            triggerPrepared()
        } else {
            logger.i("X5VideoPlayer: TbsVideo cannot be used")
        }
    }
}
