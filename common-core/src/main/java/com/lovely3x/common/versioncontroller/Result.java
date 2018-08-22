package com.lovely3x.common.versioncontroller;


/**
 * Class Result
 */
public class Result {

    //
    // Fields
    //

    /**
     * 在设计上的原意是用这个值来保存下载完成的文件的地址的,当然你可以作他用
     */
    private String url;

    //
    // Constructors
    //
    public Result() {
    }

    /**
     * Set the value of url
     * 在设计上的原意是用这个值来保存下载完成的文件的地址的,当然你可以作他用
     *
     * @param newVar the new value of url
     */
    public void setURL(String newVar) {
        url = newVar;
    }

    /**
     * Get the value of url
     * 在设计上的原意是用这个值来保存下载完成的文件的地址的,当然你可以作他用
     *
     * @return the value of url
     */
    public String getURL() {
        return url;
    }

    public Result(String url) {
        this.url = url;
    }
}
