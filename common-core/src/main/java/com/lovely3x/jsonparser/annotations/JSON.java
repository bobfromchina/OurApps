package com.lovely3x.jsonparser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解用于你和json数据格式交换
 * 用法,假设你现在你的类的字段名叫做 'person' 但是你现在想要这个字段 能够和服务器的 'ren' 匹配,那么你可以
 * 在你的 'person' 这个字段上加这个个注解 如:
 * <p>@JSON("ren") <br>String person; </p>
 * Created by lovely3x on 15-7-11.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSON {
    /**
     * json字段的名字
     *
     * @return json字段的名字
     */
    String value();

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
