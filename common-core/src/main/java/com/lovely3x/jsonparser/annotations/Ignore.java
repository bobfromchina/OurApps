package com.lovely3x.jsonparser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 目前此字段暂无作用,等待更新中...
 * 如果字段上面注解了这个字段表示
 * 忽略字段
 * 忽略包含:
 * 1,生成JSON字符串时忽略
 * 2,通过JSON字符串生成对象时忽略
 * Created by lovely3x on 16-3-21.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}
