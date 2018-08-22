/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovely3x.common.image.picker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.emptytip.ExactEmptyContentTipActivity;
import com.lovely3x.common.adapter.ListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 图片列表
 */
public class ImagePickerListActivity extends ExactEmptyContentTipActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final int IMAGE_PICKER_DETAIL_REQUEST_CODE = 200;

    public static final String KEY_BUNDLE_ALBUM_PATH = "KEY_BUNDLE_ALBUM_PATH";

    public static final String KEY_BUNDLE_ALBUM_NAME = "KEY_BUNDLE_ALBUM_NAME";

    /**
     * 传递选择模式的key
     */
    public static final String KEY_CHOICE_MODEL = "key.choice.model";

    /**
     * 已经选择的列表
     */
    public static final String KEY_ALREADY_CHOICE_LIST = "key.already.choice.list";

    /**
     * 最大的图片选择数量，不限制(9999)最多9999张
     */
    public static final int IMAGE_CHOICE_NUMBER_LIMIT_UNSPECIFIED = -1;

    /**
     * 获取数据的 key
     */
    public static final String KEY_DATA = "data";
    /**
     * 子界面需要直接完成的key
     */
    public static final String KEY_DONE = "done";
    /**
     * 单选模式
     */
    public static final String SINGLE_MODEL = "choice.model.single";
    /**
     * 多选模式
     */
    public static final String MULTIPLE_MODEL = "choice.model.multiple";
    /**
     * 传递最大的图片选择数量限制，int
     * 只对多选模式有效
     */
    public static final String KEY_MAX_IMG_CHOICE_LIMIT = "max.img.choice.limit";
    /**
     * 申请存储器的操作
     */
    private static final int REQUEST_STORAGE_OPERATION = 1002;
    /**
     * 选择模式 默认单选模式
     */
    public String choiceMode = MULTIPLE_MODEL;
    /**
     * 最大的图片选择数量
     * 默认为，用户随便选
     */
    private int maxChoiceNumberLimit = IMAGE_CHOICE_NUMBER_LIMIT_UNSPECIFIED;
    private ListAdapter<ImageBucket> mListViewAdapter = null;

    private AsyncTask<Void, Void, List<ImageBucket>> mAlbumLoadTask = null;

    private ListView mImagePickerListView;
    /**
     * 用户选择的图片集合
     */
    private ArrayList<String> mChoicedImgPath = new ArrayList<>();

    private TextView tvDone;

    @Override
    protected void onInitExtras(@NonNull Bundle bundle) {
        //获取选择模式
        choiceMode = bundle.getString(KEY_CHOICE_MODEL, SINGLE_MODEL);
        maxChoiceNumberLimit = bundle.getInt(KEY_MAX_IMG_CHOICE_LIMIT, 999);
    }

    /**
     * 加载图片
     */
    private void loadImage() {
        mAlbumLoadTask = new AsyncTask<Void, Void, List<ImageBucket>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                onContentStatusChanged(LOADING_CONTENT);
                ImagePickerHelper.getHelper().init(mActivity);
            }

            @Override
            protected List<ImageBucket> doInBackground(Void... params) {
                return ImagePickerHelper.getHelper().getImagesBucketList();
            }

            @Override
            protected void onPostExecute(List<ImageBucket> list) {
                onContentStatusChanged(LOADING_SUCCESSFUL);
                mListViewAdapter.setData(list);
            }
        };

        mAlbumLoadTask.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        if (null != mAlbumLoadTask && !mAlbumLoadTask.isCancelled()) {
            mAlbumLoadTask.cancel(true);
            mAlbumLoadTask = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == IMAGE_PICKER_DETAIL_REQUEST_CODE) {
            //单选模式下，用户选择一张就可以退出了
            if (SINGLE_MODEL.equals(choiceMode)) {
                setResult(RESULT_OK, data);
                finish();
            } else {//多选模式下，用户可以来回继续选择
                this.mChoicedImgPath.clear();
                this.mChoicedImgPath.addAll(data.getStringArrayListExtra(ImagePickerGridDetailActivity.EXTRA_DATA));
                if (tvDone != null) {
                    tvDone.setText(String.format(getString(R.string.format_done), mChoicedImgPath.size()));
                }
                //如果子界面要求完成，那么就需要直接把数据传给调用者
                if (data.getBooleanExtra(KEY_DONE, false)) {
                    onClick(tvDone);
                }
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_common_image_picker_list;
    }

    @Override
    protected void onViewInitialized() {
        setTitle(getResources().getString(R.string.title_image_picker));
        mListViewAdapter = new PickerAdapter(null, this, ImageLoader.getInstance());
        mImagePickerListView.setAdapter(mListViewAdapter);
        mImagePickerListView.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                loadImage();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_OPERATION);
            }
        } else {
            loadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_STORAGE_OPERATION == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                loadImage();
            else finish();
        }
    }

    @Override
    protected void initViews() {
        //多选模式下，给标题右边添加选择数量提示按钮
        if (MULTIPLE_MODEL.equals(choiceMode)) {
            tvDone = addRightBtn(R.string.default_select_img_count, R.id.tv_activity_image_picker_list_done);
            tvDone.setTextSize(16);
            tvDone.setOnClickListener(this);
        }
        mImagePickerListView = (ListView) findViewById(R.id.common_image_picker_list_view);

    }

    @Override
    public void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance) {

    }

    @Override
    public void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageBucket item = mListViewAdapter.getItem(position);
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(KEY_BUNDLE_ALBUM_PATH, item.bucketList);
        extras.putString(KEY_BUNDLE_ALBUM_NAME, item.bucketName);
        extras.putBoolean(KEY_CHOICE_MODEL, choiceMode.equals(MULTIPLE_MODEL));
        extras.putInt(KEY_MAX_IMG_CHOICE_LIMIT, maxChoiceNumberLimit);
        extras.putStringArrayList(KEY_ALREADY_CHOICE_LIST, mChoicedImgPath);
        launchDetailsListActivity(extras);
    }

    /**
     * 启动详情展示界面
     *
     * @param extras 附加值
     */
    protected void launchDetailsListActivity(Bundle extras) {
        launchActivityForResult(ImagePickerGridDetailActivity.class, IMAGE_PICKER_DETAIL_REQUEST_CODE, extras);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_activity_image_picker_list_done) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(KEY_DATA, mChoicedImgPath);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected ViewGroup getEmptyContainerView() {
        return ButterKnife.findById(this, R.id.fl_activity_common_image_picker_list_empty_tip_container);
    }
}
