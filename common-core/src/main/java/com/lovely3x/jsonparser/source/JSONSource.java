package com.lovely3x.jsonparser.source;

/**
 * Created by lovely3x on 15-6-29.
 * 作为json解析器的数据源
 * 你可以自己扩展数据源提供者
 */
public interface JSONSource {

    /**
     * 获取输入数据
     *
     * @return 需要解析的的数据
     */
    String input();

}
