package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import com.tencent.smtt.sdk.TbsVideo

class X5VideoPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : VideoPlayer(coroutineScope){
    private val logger = Logger.create("X5VideoPlayer")
    if(TbsVideo.canUseTbsPlayer(context)){
        logger.i("X5VideoPlayer: TbsVideo can be used")
        TbsVideo.openVideo(context, url);
    } else {
        logger.i("X5VideoPlayer: TbsVideo cannot be used")
    }
    override fun prepare(line: ChannelLine) {
        TbsVideo.openVideo(context, line.playableUrl)
    }
}
