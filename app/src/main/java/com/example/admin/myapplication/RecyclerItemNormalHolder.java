package com.example.admin.myapplication;

import android.content.Context;
import android.view.View;

import com.example.admin.myapplication.video.CustomVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context = null;

    CustomVideo gsyVideoPlayer;

    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        gsyVideoPlayer = v.findViewById(R.id.video_item_player);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();

    }

    public void onBind(final int position, VideoModel videoModel) {
        gsyVideoPlayer.loadCoverImage(videoModel.getVideoUrl(), 0);
        //防止错位，离开释放
        gsyVideoPlayer.initUIState();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setUrl(videoModel.getVideoUrl())
                .setSetUpLazy(false)//lazy可以防止滑动卡顿
                .setVideoTitle(videoModel.getTitle())
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setShowPauseCover(true)
                .setLockLand(true)
                .setPlayTag(videoModel.getVideoUrl())
                .setShowFullAnimation(true)
                .setNeedShowWifiTip(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(false);
                        }

                    }
                }).build(gsyVideoPlayer);
        if (position == 0) {
            gsyVideoPlayer.startPlayLogic();
        }

    }



}
