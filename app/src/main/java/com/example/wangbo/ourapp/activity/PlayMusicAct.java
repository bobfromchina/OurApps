package com.example.wangbo.ourapp.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.bean.CommentSoongsBean;
import com.example.wangbo.ourapp.bean.MusicListDetails;
import com.example.wangbo.ourapp.utils.RoundedImageView;
import com.jackmar.jframelibray.base.JBaseAct;
import com.jackmar.jframelibray.utils.GlideImageLoadUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangbo on 2018/8/7.
 * <p>
 * <p>
 * 音乐播放界面
 */
public class PlayMusicAct extends JBaseAct {

    public static final String EXTRA_BASE_DATA = "extra_base_data";

    public static final String EXTRA_BASE_DATA_MORE = "extra_base_data_more";

    String file_path = "";

    MediaPlayer mediaPlayer = new MediaPlayer();

    @BindView(R.id.song_img)
    RoundedImageView songImg;

    @BindView(R.id.song_name)
    TextView songName;

    @BindView(R.id.song_author)
    TextView songAuthor;

    private String musicId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLyContent(R.layout.activity_play_music, true, "播放音乐");
    }

    @Override
    public void initView() {

        MusicListDetails bean = getIntent().getParcelableExtra(EXTRA_BASE_DATA);

        CommentSoongsBean beans = getIntent().getParcelableExtra(EXTRA_BASE_DATA_MORE);

        if (bean != null) {

            GlideImageLoadUtil.loadImage(context, bean.getAl().get(0).getPicUrl(), songImg);

            songName.setText(bean.getName());

            songAuthor.setText(bean.getAr().get(0).getName());

            musicId = bean.getId();
        } else {
            GlideImageLoadUtil.loadImage(context, beans.getAlbumPic(), songImg);

            songName.setText(beans.getMusicName());

            songAuthor.setText(beans.getAlbumName());

            musicId = beans.getMusicId();
        }
    }

    @Override
    public void initData() {

        file_path = "http://music.163.com/song/media/outer/url?id=" + musicId + ".mp3";

        try {
            mediaPlayer.setDataSource(file_path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);  // 设置循环播放
        } catch (Exception e) {
            e.printStackTrace();
        }
//        HttpHelper.getInstance(context).getSongUrlDetails(bean.getId(), new ProgressSubscriber<String>(context, new IOnNextListener<String>() {
//            @Override
//            public void onNext(String o) {
//                file_path = o;
//                try {
//                    mediaPlayer.setDataSource(file_path);
//                    mediaPlayer.prepare();
//                    mediaPlayer.setLooping(true);  // 设置循环播放
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }));
    }


    @OnClick({R.id.play, R.id.stop, R.id.pause})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (!file_path.equals(""))
                    mediaPlayer.start();
                else
                    showToast("未获取到数据，请稍后再试！");
                break;

            case R.id.stop:
                if (!file_path.equals(""))
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        mediaPlayer.stop();
                        try {
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                break;

            case R.id.pause:
                if (!file_path.equals(""))
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
        mediaPlayer.stop();
    }
}
