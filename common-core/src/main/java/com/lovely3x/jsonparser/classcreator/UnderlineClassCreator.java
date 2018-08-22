package com.lovely3x.jsonparser.classcreator;

import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.JavaType;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.JSONValue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 下划线类创建器
 * Created by lovely3x on 15-7-2.
 */
public class UnderlineClassCreator implements ClassCreator {

    private static final String PRIVATE_ACCESS_SYMBOL = "private";

    private static final String PUBLIC_ACCESS_SYMBOL = "public";
    /**
     * 下划线
     */
    private static final char UNDERLINE = '_';
    /**
     * 存放生成的字段 key = 字段名 value = 字段类型
     */
    private HashMap<String, String> fields = new HashMap<>();
    /**
     * 使用的字符集
     */
    private String charSet = "UTF-8";

    /**
     * 生成字段表
     *
     * @param jsonObject
     */
    protected StringBuilder createFields(JSONObject jsonObject) throws IOException {
        StringBuilder sb = new StringBuilder();

        ArrayList<JSONKey> keys = new ArrayList<>(jsonObject.keySet());
        final int count = keys.size();
        for (int i = 0; i < count; i++) {
            JSONKey key = keys.get(i);
            JSONValue value = jsonObject.getValue(key);

            //字段类型
            String fieldType = null;
            //字段名
            String fieldName = null;

            switch (value.guessType()) {
                case JSONType.JSON_TYPE_OBJECT:
                    //TODO
                    break;
                case JSONType.JSON_TYPE_ARRAY:
                    //TODO
                    break;
                case JSONType.JSON_TYPE_BOOLEAN:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_BOOLEAN;
                    break;
                case JSONType.JSON_TYPE_INT:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_INT;
                    break;
                case JSONType.JSON_TYPE_LONG:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_LONG;
                    break;
                case JSONType.JSON_TYPE_FLOAT:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_FLOAT;
                    break;
                case JSONType.JSON_TYPE_DOUBLE:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_DOUBLE;
                    break;
                case JSONType.JSON_TYPE_STRING:
                    fieldName = fromJSONFieldNameToJavaFieldName(key.getKey());
                    fieldType = JavaType.JAVA_TYPE_STRING;
                    break;
            }

            if (fieldName != null && fieldName.length() > 0 && fieldType.length() > 0) {
                sb.append(addNewLine(1)).append(addBlock(1)).append(PRIVATE_ACCESS_SYMBOL)
                        .append(' ').append(containFieldTypeQualifiedName(fieldType) ? fieldType : getSimpleName(fieldType)).append(' ').append(fieldName).append(';');
                sb.append(addNewLine(1));
                fields.put(fieldName, fieldType);
            }
        }
        return sb;
    }

    /**
     * 获取简易名字
     *
     * @param name 原始名
     * @return 简单名
     */
    private String getSimpleName(String name) {
        return name.indexOf('.') != -1 ? name.substring(name.lastIndexOf('.') + 1, name.length()) : name;
    }


    /**
     * 通过字段集合创建getter和setter
     *
     * @param fieldsTable
     */
    protected StringBuilder createGetterAndSetter(Map<String, String> fieldsTable) throws IOException {
        ArrayList<String> fields = new ArrayList<>(fieldsTable.keySet());
        StringBuilder sb = new StringBuilder();
        final int count = fields.size();
        for (int i = 0; i < count; i++) {

            String fieldName = fields.get(i);
            String fieldType = fieldsTable.get(fieldName);

            //创建getter
            sb.append(addNewLine(2)).append(addBlock(1)).append(PUBLIC_ACCESS_SYMBOL).append(' ')
                    .append(containFieldTypeQualifiedName(fieldType) ? fieldType : getSimpleName(fieldType)).append(' ');
            sb.append(JavaType.JAVA_TYPE_BOOLEAN.equals(fieldName) ? "is" : "get").append(firstSymbolToUpperCase(fieldName));
            sb.append("()").append('{').append(addNewLine(1));
            sb.append(addBlock(2));
            sb.append("return this.").append(fieldName).append(';').append(addNewLine(1)).append(addBlock(1)).append('}');

            //创建setter
            sb.append(addNewLine(2)).append(addBlock(1)).append(PUBLIC_ACCESS_SYMBOL).append(' ').append("void").append(' ');
            sb.append("set").append(firstSymbolToUpperCase(fieldName));
            sb.append("(").append(containFieldTypeQualifiedName(fieldType) ? fieldType : getSimpleName(fieldType)).append(' ').append(fieldName).append(")").append('{').append(addNewLine(1));
            sb.append(addBlock(2));
            sb.append("this.").append(fieldName).append(" = ").append(fieldName).append(';').append(addNewLine(1)).append(addBlock(1)).append('}');
        }
        return sb;
    }


    /**
     * 增加缩进
     *
     * @param blockLevel
     * @return
     */
    private StringBuilder addBlock(int blockLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blockLevel; i++) {
            sb.append('\t');
        }
        return sb;
    }

    /**
     * 字段表中是否已经包含了这个全限定名的类
     *
     * @param qualifiedName 需要判断的全限定类名
     * @return 是否包含
     */
    private boolean containFieldTypeQualifiedName(String qualifiedName) {
        //先检索 simpleName
        boolean name = fields.containsValue(getSimpleName(qualifiedName));
        if (!name) return false;
        else {
            //如果包含全限定名则认为,可以添加
            return !fields.containsValue(qualifiedName);
        }
    }


    /**
     * 增加换行符
     *
     * @param level 需要增加几个
     * @return 新行组
     */
    private StringBuilder addNewLine(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append('\n');
        }
        return sb;
    }

    /**
     * 创建类头
     *
     * @param className
     * @return 生成结果
     */
    protected StringBuilder createClassHeader(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append(PUBLIC_ACCESS_SYMBOL).append(' ').append("class").append(' ').append(className).append('{');
        return sb;
    }

    /**
     * 创建构造器
     *
     * @param className 类名
     * @param fields    字段名
     * @return 生成结果
     */
    protected StringBuilder createConstructor(String className, Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append(addNewLine(2)).append(addBlock(1))
                .append(PUBLIC_ACCESS_SYMBOL).append(' ').append(className).append("()").append(' ').append('{')
                .append(addNewLine(1)).append(addBlock(1)).append('}');

        sb.append(addNewLine(2)).append(addBlock(1))
                .append(PUBLIC_ACCESS_SYMBOL).append(' ').append(className).append('(');
        ArrayList<String> keys = new ArrayList(fields.keySet());
        final int count = fields.size();
        for (int i = 0; i < count; i++) {
            String fieldName = keys.get(i);
            String fieldType = fields.get(fieldName);
            sb.append(containFieldTypeQualifiedName(fieldType) ? fieldType : getSimpleName(fieldType)).append(' ').append(fieldName);
            if (i + 1 != count) {
                sb.append(",");
            }
        }
        sb.append(')').append(' ').append('{');
        for (int i = 0; i < count; i++) {
            String fieldName = keys.get(i);
            sb.append(addNewLine(1)).append(addBlock(2)).append("this.").append(fieldName).append(" = ").append(fieldName).append(';');
        }

        sb.append(addNewLine(1)).append(addBlock(1)).append('}');


        return sb;
    }

    /**
     * 创建HashCode 方法
     *
     * @param className 类名
     * @param fields    字段表
     */
    protected StringBuilder createEqualsAndHashCodeMethod(String className, Map<String, String> fields) {
        return new StringBuilder();
    }

    /**
     * 创建toString方法
     *
     * @param className 类名
     * @param fields    字段表
     * @return toString 方法
     */
    protected StringBuilder createToStringMethod(String className, Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append(addNewLine(2)).append(addBlock(1));
        sb.append(PUBLIC_ACCESS_SYMBOL).append(" String ").append("toString(").append(')');
        sb.append('{').append(addNewLine(1));

        sb.append(addBlock(2));
        sb.append("return new StringBuilder()").append(addNewLine(1)).append(addBlock(3)).append(".append").append('(').append('"').append(className).append(" = { ").append('"').append(')');
        ArrayList<String> keys = new ArrayList<>(fields.keySet());
        final int count = keys.size();
        for (int i = 0; i < count; i++) {
            String field = keys.get(i);
            sb.append(addNewLine(1)).append(addBlock(3)).append(i != 0 ? ".append(',')" : "").append('.').append("append(").append('"').append(field).append('"').append(')');
            sb.append(".append(").append('"').append(" = ").append('"').append(')').append(".append(").append(field).append(')');

        }
        sb.append(addNewLine(1)).append(addBlock(3));
        sb.append(".append('}') ");
        sb.append(".toString()");
        sb.append(';');
        sb.append(addNewLine(1));
        sb.append(addBlock(1)).append('}');
        return sb;
    }


    /**
     * 创建类尾
     *
     * @return 类结束符号
     */
    protected StringBuilder createClassFooter() {
        return new StringBuilder().append(addNewLine(1)).append('}');
    }


    /**
     * 导包
     *
     * @param fields 字段表
     */
    protected StringBuilder importPackages(Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        //由于无法做到 根据json获取其他的对象所以全是String和基本类型,就不用导包了
     /*   ArrayList<String> values = new ArrayList<>(fields.values());
        final int count = values.size();

        for (int i = 0; i < count; i++) {
            String value = values.get(i);
            if (value.indexOf('.') != -1) {
                sb.append("import ").append(value).append(';');
                sb.append(addNewLine(1));
            }
        }
*/
        return sb;
    }

    /**
     * 将那个字符串的首字母大写
     *
     * @param str 需要转换的字符串
     * @return 转换后的结果
     */
    private String firstSymbolToUpperCase(String str) {
        if (str != null && str.trim().length() > 0) {
            final int count = str.length();
            char firstChar = str.charAt(0);
            if (firstChar > 'Z') firstChar -= 32;
            return firstChar + str.substring(1, count);
        }
        return null;
    }

    /**
     * 将 json 的字段名转换为java 的字段名
     *
     * @param jsonFieldName
     * @return
     */
    protected String fromJSONFieldNameToJavaFieldName(String jsonFieldName) {
        StringBuilder sb = new StringBuilder();
        final int count = jsonFieldName.length();
        char preChar = '\0';

        for (int i = 0; i < count; i++) {
            char currentChar = jsonFieldName.charAt(i);
            switch (currentChar) {
                case UNDERLINE:
                    break;
                default:
                    switch (preChar) {
                        case UNDERLINE:
                            if (currentChar >= 97) {
                                sb.append((char) (currentChar - 32));//is lower case,to uppercase
                            } else {
                                sb.append(currentChar);//upper case
                            }
                            break;
                        default:
                            sb.append(currentChar);
                            break;
                    }
                    break;
            }
            preChar = currentChar;
        }
        return sb.toString();
    }


    @Override
    public OutputStream createObject(JSONObject object, String className, OutputStream outputStream) {
        try {
            StringBuilder sbClassHeader = createClassHeader(className);
            StringBuilder sbFields = createFields(object);
            StringBuilder sbConstructor = createConstructor(className, fields);
            StringBuilder sbGetterAndSetter = createGetterAndSetter(fields);
            StringBuilder sbEqualsAndHashCode = createEqualsAndHashCodeMethod(className, fields);
            StringBuilder sbToString = createToStringMethod(className, fields);
            StringBuilder sbImportPackage = importPackages(fields);
            StringBuilder sbClassFooter = createClassFooter();
            sbImportPackage.append(sbClassHeader).append(sbFields).append(sbConstructor).append(sbGetterAndSetter);
            sbImportPackage.append(sbEqualsAndHashCode).append(sbToString).append(sbClassFooter);
            outputStream.write(sbImportPackage.toString().getBytes(charSet));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}
