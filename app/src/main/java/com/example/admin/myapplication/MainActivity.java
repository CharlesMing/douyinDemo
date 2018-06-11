package com.example.admin.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.myapplication.dialog.CommentListFragment;
import com.example.admin.myapplication.status.StatusBarCompat;
import com.example.admin.myapplication.video.CustomVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private VideoAdapter mVideoAdapter;
    private ImageView mIvAvatar;
    private TextView mTvMusicName, mTvComment;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_main);

        LinearLayout li = findViewById(R.id.line3);

        mRecyclerView = findViewById(R.id.recyclerView);
        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvMusicName = findViewById(R.id.tv_music_name);
        mTvComment = findViewById(R.id.tv_comment);
        mTvComment.setOnClickListener(this);
        mTvMusicName.setSelected(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper linearSnapHelper = new PagerSnapHelper();
        linearSnapHelper.attachToRecyclerView(mRecyclerView);
        mVideoAdapter = new VideoAdapter();
        mRecyclerView.swapAdapter(mVideoAdapter, false);
        initData();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem, visibleCount;
            int mRecordItem = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:

                        if (mRecordItem != firstVisibleItem) {
                            CustomVideo player = recyclerView.getChildAt(0).findViewById(R.id.video_item_player);
                            if (player != null) {
                                player.startPlayLogic();
                                mRecordItem = firstVisibleItem;
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;

            }
        });

        GlideApp.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526618513135&di=9392989a216a15d7418437ff954516eb&imgtype=0&src=http%3A%2F%2Fwww.fansimg.com%2Fuploads2011%2F04%2Fuserid140098time20110417160511.jpg")
                .circleCrop()
                .into(mIvAvatar);
    }

    List<VideoModel> data = new ArrayList<>();

    private void initData() {
        data.add(new VideoModel("1",
                "http://v11-tt.ixigua.com/43d30c7db9c299fc0c28440ff3dfa429/5b04eb93/video/m/22017780edaed27417584a9f40f87740f0311570fae00001d4b5bea0af2/",
                "http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834171827b357b5c9ea14cebfcf.jpg"));
        data.add(new VideoModel("1",
                "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200fbd0000bc0l2ijrm1nf66kgg480&line=0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526618513135&di=9392989a216a15d7418437ff954516eb&imgtype=0&src=http%3A%2F%2Fwww.fansimg.com%2Fuploads2011%2F04%2Fuserid140098time20110417160511.jpg"));
        data.add(new VideoModel("1",
                "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200f490000bc142cvl54d3gcmhmvh0&line=0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526618513135&di=9392989a216a15d7418437ff954516eb&imgtype=0&src=http%3A%2F%2Fwww.fansimg.com%2Fuploads2011%2F04%2Fuserid140098time20110417160511.jpg"));
        data.add(new VideoModel("1",
                "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200f180000bbss1l41n3eabpg1o5m0&line=0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526618513135&di=9392989a216a15d7418437ff954516eb&imgtype=0&src=http%3A%2F%2Fwww.fansimg.com%2Fuploads2011%2F04%2Fuserid140098time20110417160511.jpg"));
        data.add(new VideoModel("1",
                "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200f180000bc1n8mqepr17h5nerqhg&line=0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526618513135&di=9392989a216a15d7418437ff954516eb&imgtype=0&src=http%3A%2F%2Fwww.fansimg.com%2Fuploads2011%2F04%2Fuserid140098time20110417160511.jpg"));
        data.add(new VideoModel("1",
                "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200fbc0000bc16lbr82vu4lfm1ga4g&line=04",
                "http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834171827b357b5c9ea14cebfcf.jpg"));


        mVideoAdapter.setData(data);
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    private CommentListFragment mCommentListFragment;

    @Override
    public void onClick(View v) {
        int resId = v.getId();

        switch (resId) {
            case R.id.tv_comment:
                if (mCommentListFragment == null) {
                    mCommentListFragment = new CommentListFragment();
                }
                mCommentListFragment.show(getSupportFragmentManager(), "dialog");
                break;
        }
    }
}
