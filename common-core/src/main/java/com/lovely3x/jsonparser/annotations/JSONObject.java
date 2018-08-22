package com.lovely3x.jsonparser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lovely3x on 15-7-11.
 * 使用这个注解表示这个字段需要一个jsonArray
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONObject {
    /**
     * @return json字段key
     */
    String value();

    /**
     * JSONObject对应的对象
     *
     * @return
     */
    Class object();


    /**
     * 注意：这个字段只在生成json时有效
     * 在使用注解作为key时，是否使用key规则
     * 在生成JSON串的时候，所有的字段都会通过指定的Rule变换，默认使用{@link com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule}
     * 在指定为false后，这个字段就不会使用
     *
     * @return 默认不使用
     */
    boolean useKeyRuleOnUseAnnotation() default false;

    /**
     * 注意：这个字段只在生成json时有效
     * 在生成json对象时如果识别到这个对象，是否使用这个注解的值作为字段
     *
     * @return 默认使用
     */
    boolean useAnnotation() default true;

}
