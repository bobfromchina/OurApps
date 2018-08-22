package com.lovely3x.jsonparser.conversation.rule;

/**
 * Created by lovely3x on 15-7-2.
 * 下划线java对象 field 转换为json key 的规则
 */
public class UnderlineJSONGenerateKeyRule extends JSONKeyGenerateRule {

    public static final char UNDERLINE = '_';

    @Override
    public String keyRule(String fieldName) {
        StringBuilder sb = new StringBuilder();
        final int count = fieldName.length();
        for (int i = 0; i < count; i++) {
            char currentChar = fieldName.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                sb.append(UNDERLINE).append((char) (currentChar + 32));
            } else {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }
}
