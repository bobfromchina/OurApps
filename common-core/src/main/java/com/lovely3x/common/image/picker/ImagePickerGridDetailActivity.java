package com.lovely3x.common.image.picker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.emptytip.ExactEmptyContentTipActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ImagePickerGridDetailActivity extends ExactEmptyContentTipActivity implements View.OnClickListener, ImageDetailsPickerAdapter.SingleOnCheckedListener, ImageDetailsPickerAdapter.MultiOnCheckedListener {

    public static final String KEY_BUNDLE_RESULT_IMAGE_PATH = "KEY_BUNDLE_RESULT_IMAGE_PATH";
    /**
     * 获取用户选择的结果，就用这个key
     */
    public static final String EXTRA_DATA = "data";
    private static final String KEY_CHECKED_LIST = "key.checked.list";
    private ImageDetailsPickerAdapter mGridViewAdapter = null;

    private List<ImageItem> mGridListData = null;

    private GridView commonImagePickerDetailGridView;


    private boolean mMultiChoiceModel;

    private TextView mTvDone;
    /**
     * 用户最大选择的图片数量
     */
    private int maxLimit;
    /**
     * 已经选择的列表
     * 不包含在当前界面新选择的
     */
    private ArrayList<String> mParentAlreadyChoicedPath;


    @Override
    protected int getContentView() {
        return R.layout.activity_common_image_picker_detail;
    }

    /**
     * 获取图片展示适配器
     *
     * @return 适配器
     */
    protected ImageDetailsPickerAdapter getGridViewAdapter(List<ImageItem> datas) {
        return new ImageDetailsPickerAdapter(datas, mActivity, ImageLoader.getInstance());
    }

    @Override
    protected void onViewInitialized() {
        setTitle(R.string.title_image_picker);
        mGridViewAdapter = getGridViewAdapter(mGridListData);
        commonImagePickerDetailGridView.setAdapter(mGridViewAdapter);
        mGridViewAdapter.setCheckModel(mMultiChoiceModel);
        mGridViewAdapter.setSingleOnCheckedListener(this);
        mGridViewAdapter.setMultiOnCheckedListener(this);
        //如果在多选模式下，用户再次进入已经选择的界面
        //那么以前选择的也应该被选中
        if (mMultiChoiceModel && mParentAlreadyChoicedPath != null && mGridListData != null && !mGridListData.isEmpty()) {
            for (int i = 0; i < mParentAlreadyChoicedPath.size(); i++) {
                for (int j = 0; j < mGridListData.size(); j++) {
                    //用户已经选择的
                    String alreadySelected = mParentAlreadyChoicedPath.get(i);
                    ImageItem current = mGridListData.get(j);
                    if (alreadySelected.equals(current.getImagePath())) {//如果存在选中的
                        mGridViewAdapter.setItemChecked(j, true);
                    }
                }
            }
        }
    }

    /**
     * 在右边添加对勾按钮
     */
    protected void addRightYesBtn() {
        this.mTvDone = addRightBtn(String.format(getString(R.string.format_done), (mParentAlreadyChoicedPath.size())),
                R.id.tv_activity_image_picker_done);   //显示已经选择的数量
        this.mTvDone.setTextColor(getColor(R.color.white, true));
        mTvDone.setTextSize(16);
        mTvDone.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        //多选模式下添加完成按钮
        if (mMultiChoiceModel) {
            addRightYesBtn();
        }
        commonImagePickerDetailGridView = (GridView) findViewById(R.id.common_image_picker_detail_grid_view);
    }

    @Override
    protected void onInitExtras(@NonNull Bundle bundle) {
        mGridListData = bundle.getParcelableArrayList(ImagePickerListActivity.KEY_BUNDLE_ALBUM_PATH);
        String title = bundle.getString(ImagePickerListActivity.KEY_BUNDLE_ALBUM_NAME);
        this.mMultiChoiceModel = bundle.getBoolean(ImagePickerListActivity.KEY_CHOICE_MODEL);
        this.maxLimit = bundle.getInt(ImagePickerListActivity.KEY_MAX_IMG_CHOICE_LIMIT);
        this.mParentAlreadyChoicedPath = bundle.getStringArrayList(ImagePickerListActivity.KEY_ALREADY_CHOICE_LIST);
        setTitle(title);
    }

    @Override
    public void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance) {

    }

    @Override
    public void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance) {
        ArrayList<Integer> checkedList = savedInstance.getIntegerArrayList(KEY_CHECKED_LIST);
        if (mGridViewAdapter != null) {
            mGridViewAdapter.setItemChecked(checkedList);
        }
    }

    @Override
    public void onBackPressed() {
        //如果在多选模式下，并且用户有选择图片
        //那么被认为用户是正确的图片选择操作
        if (mMultiChoiceModel) {
            Intent intent = new Intent();
            SparseBooleanArray checkedArray = mGridViewAdapter.getCheckArray();
            ArrayList<String> arrayList = new ArrayList<>();
            final int len = checkedArray.size();
            for (int i = 0; i < len; i++) {
                int position = checkedArray.keyAt(i);
                if (checkedArray.get(position)) {
                    ImageItem data = mGridViewAdapter.getDatas().get(position);
                    arrayList.add(data.getImagePath());
                }
            }

            if (mParentAlreadyChoicedPath == null) mParentAlreadyChoicedPath = new ArrayList<>();
            mParentAlreadyChoicedPath.removeAll(arrayList);
            mParentAlreadyChoicedPath.addAll(arrayList);
            intent.putStringArrayListExtra(EXTRA_DATA, mParentAlreadyChoicedPath);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mGridViewAdapter != null) {
            SparseBooleanArray checkArray = mGridViewAdapter.getCheckArray();
            ArrayList<Integer> checkedArray = new ArrayList<>();
            final int len = checkArray.size();
            for (int i = 0; i < len; i++) {
                int position = checkArray.keyAt(i);
                if (checkArray.get(position)) {
                    checkedArray.add(position);
                }
            }
            outState.putIntegerArrayList(KEY_CHECKED_LIST, checkedArray);
        }
    }

    @Override
    public void handleLoadFailure(String errorMsg) {

    }

    @Override
    public void handleLoadFailure(String errorMsg, View.OnClickListener retryListener) {

    }

    @Override
    protected ViewGroup getEmptyContainerView() {
        return ButterKnife.findById(this, R.id.fl_activity_common_image_picker_detail_empty_tip_container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_activity_image_picker_done) {//完成
            //如果在多选模式下，并且用户有选择图片
            //那么被认为用户是正确的图片选择操作
            if (mMultiChoiceModel) {
                Intent intent = new Intent();
                SparseBooleanArray checkedArray = mGridViewAdapter.getCheckArray();
                ArrayList<String> arrayList = new ArrayList<>();
                final int len = checkedArray.size();
                for (int i = 0; i < len; i++) {
                    int position = checkedArray.keyAt(i);
                    if (checkedArray.get(position)) {
                        ImageItem data = mGridViewAdapter.getDatas().get(position);
                        arrayList.add(data.getImagePath());
                    }
                }

                if (mParentAlreadyChoicedPath == null)
                    mParentAlreadyChoicedPath = new ArrayList<>();
                mParentAlreadyChoicedPath.removeAll(arrayList);
                mParentAlreadyChoicedPath.addAll(arrayList);
                intent.putStringArrayListExtra(EXTRA_DATA, mParentAlreadyChoicedPath);
                intent.putExtra(ImagePickerListActivity.KEY_DONE, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onSingleChecked(int position, boolean isChecked) {
        Intent intent = new Intent();
        intent.setData(Uri.fromFile(new File(mGridViewAdapter.getDatas().get(position).getImagePath())));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMultiChecked(List<Integer> checkedArray, int position, boolean isChecked) {
        //在当前界面选择的图片数量
        int currentAlreadyCheckCount = checkedArray == null ? 0 : checkedArray.size();
        int parentCheckedCount = mParentAlreadyChoicedPath == null ? 0 : mParentAlreadyChoicedPath.size();
        if (isChecked) {//如果是选中
            if (currentAlreadyCheckCount + parentCheckedCount > maxLimit) {
                //如果已经超出选择的数量限制
                //那么就不能再选择更多了
                mGridViewAdapter.setItemChecked(position, false);
                if (checkedArray != null) checkedArray.remove(Integer.valueOf(position));
                showToast(R.string.choice_img_number_limit);
            }
        } else {//cancel select
            ImageItem data = mGridListData.get(position);
            mParentAlreadyChoicedPath.remove(data.getImagePath());
        }

        //显示已经选择的数量
        if (mTvDone != null)
            mTvDone.setText(String.format(getString(R.string.format_done), (checkedArray == null ? 0 : checkedArray.size() + mParentAlreadyChoicedPath.size())));
    }

}
