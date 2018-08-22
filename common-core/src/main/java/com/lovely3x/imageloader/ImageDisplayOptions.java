package com.lovely3x.imageloader;

/**
 * 图片选项
 */
public class ImageDisplayOptions {

    /**
     * 加载中的图片资源
     */
    private int loadingImgResource;

    /**
     * 加载失败的图片资源
     */
    private int loadFailImgResource;

    /**
     * 是否缓存在内存中
     */
    private boolean cacheInMemory;

    /**
     * 是否缓存在磁盘中
     */
    private boolean cacheInDisk;


    private ImageDisplayOptions() {

    }

    public int loadingImgResource() {
        return loadingImgResource;
    }

    public int loadFailImgResource() {
        return loadFailImgResource;
    }

    public boolean cacheInMemory() {
        return cacheInMemory;
    }

    public boolean cacheInDisk() {
        return cacheInDisk;
    }


    /**
     * 构建器
     */
    public static class Builder {

        private ImageDisplayOptions options;

        public Builder() {
            options = new ImageDisplayOptions();
        }

        public Builder cacheInMemory(boolean cacheInMemory) {
            this.options.cacheInMemory = cacheInMemory;
            return this;
        }

        public Builder cacheInDisk(boolean cacheInDisk) {
            this.options.cacheInDisk = cacheInDisk;
            return this;
        }

        public Builder loadFailImgResource(int res) {
            this.options.loadFailImgResource = res;
            return this;
        }

        public Builder loadingImgResource(int res) {
            this.options.loadingImgResource = res;
            return this;
        }

        public ImageDisplayOptions build() {
            return options;
        }

    }
}

