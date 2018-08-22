package com.lovely3x.common.image.picker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ImagePickerHelper {

    private static final String TAG = "ImagePickerHelper";
    private static final String GIF_MIME_TYPE = "image/gif";
    private static ImagePickerHelper instance;
    boolean hasBuildImagesBucketList = false;
    private WeakReference<Context> context;
    private ContentResolver contentResolver;
    private HashMap<String, String> mThumbnailList = new HashMap<String, String>();
    private HashMap<String, ImageBucket> mBucketList = new HashMap<String, ImageBucket>();

    private ImagePickerHelper() {
    }

    public static ImagePickerHelper getHelper() {
        if (instance == null) {
            instance = new ImagePickerHelper();
        }
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = new WeakReference<>(context.getApplicationContext() == null ? context : context.getApplicationContext());
            this.contentResolver = this.context.get().getContentResolver();
        }
    }

    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getThumbnailColumnData(cursor);
    }

    private void getThumbnailColumnData(Cursor cur) {
        mThumbnailList.clear();
        if (cur.moveToFirst()) {
            int image_id;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            String image_path;
            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                mThumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    private void buildImagesBucketList() {
        getThumbnail();
        mBucketList.clear();

        String columns[] = new String[]{Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME, Media.MIME_TYPE, Media.DATE_ADDED};
        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
            int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
            int mimeTypeIndex = cursor.getColumnIndex(Media.MIME_TYPE);
            int dateIndex = cursor.getColumnIndex(Media.DATE_ADDED);

            do {
                String _id = cursor.getString(photoIDIndex);
                String path = cursor.getString(photoPathIndex);
                String bucketName = cursor.getString(bucketDisplayNameIndex);
                String bucketId = cursor.getString(bucketIdIndex);
                String mimeType = cursor.getString(mimeTypeIndex);
                long date = cursor.getLong(dateIndex);
                ImageBucket bucket = mBucketList.get(bucketId);
     /*           //过滤掉gif
                if (GIF_MIME_TYPE.equals(mimeType)) {
                    continue;
                }
*/
                if (bucket == null) {
                    bucket = new ImageBucket();
                    mBucketList.put(bucketId, bucket);
                    bucket.bucketList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.setImageId(_id);
                imageItem.setImagePath(path);
                imageItem.setThumbnailPath(mThumbnailList.get(_id));
                imageItem.date = date;
                bucket.bucketList.add(imageItem);

            } while (cursor.moveToNext());
        }
        hasBuildImagesBucketList = true;

        //排序
        Set<String> keys = mBucketList.keySet();
        for (String next : keys) {
            ImageBucket value = mBucketList.get(next);
            if (value != null) {
                Collections.sort(value.bucketList);
            }
        }
    }

    public List<ImageBucket> getImagesBucketList() {
        buildImagesBucketList();
        List<ImageBucket> tmpList = new ArrayList<>(mBucketList.values());
        Collections.sort(tmpList);
        return tmpList;
    }

}
