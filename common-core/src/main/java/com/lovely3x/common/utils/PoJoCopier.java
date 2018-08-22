package com.lovely3x.common.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * 对象复制器
 * Created by lovely3x on 16-4-1.
 */
public class PoJoCopier {


    /**
     * 注解了这个字段的说明在复制时不会重新设置值
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ignore {

    }

    public static final FieldComparator DEFAULT_COMPARATOR = new FieldComparator() {
        public static final String TAG = "PoJoCopier";

        @Override
        public boolean isEqual(Field field, Field anotherField) {
            return field != null && anotherField != null && field.getAnnotation(Ignore.class) == null && (field.getName().equals(anotherField.getName()) || com.lovely3x.jsonparser.utils.CommonUtils.isEqual(field.getName(), anotherField.getName()));
        }
    };

    /*复制*/
    public static <T> T copy(Object original, Class originalClazz, T target, Class targetClazz, FieldComparator comparator) {
        try {

            Field[] fields = original.getClass().getDeclaredFields();
            Field[] targetFields = targetClazz.getDeclaredFields();

            outer:
            for (Field field : fields) {
                field.setAccessible(true);
                if (CommonUtils.fieldIsInvalid(field)) continue;

                for (Field aField : targetFields) {
                    aField.setAccessible(true);
                    if (CommonUtils.fieldIsInvalid(aField)) continue;

                    try {
                        if (comparator.isEqual(field, aField)) {
                            aField.set(target, field.get(original));
                            continue outer;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //递归复制
                if (targetClazz.getSuperclass() != Object.class) {
                    copy(original, originalClazz, target, targetClazz.getSuperclass(), comparator);
                }
            }

            //递归复制
            if (originalClazz.getSuperclass() != Object.class) {
                copy(original, originalClazz.getSuperclass(), target, targetClazz, comparator);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    /**
     * 复制
     */
    public static <T> T copy(Object original, Class<T> another) {
        try {
            return copy(original, original.getClass(), another.newInstance(), another.getClass(), DEFAULT_COMPARATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制
     */
    public static <T> T copy(Object original, T target) {
        return copy(original, original.getClass(), target, target.getClass(), DEFAULT_COMPARATOR);
    }

    /**
     * 字段比较器
     */
    public interface FieldComparator {
        boolean isEqual(Field field, Field anotherField);
    }
}
