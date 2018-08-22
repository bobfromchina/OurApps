package com.lovely3x.jsonparser.source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lovely3x on 15-6-29.
 * json 数据源 字符串提供者默认实现类
 */
public class JSONSourceImpl implements JSONSource {

    /**
     * 默认的编码方式
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 输入的数据源的字符串形式
     */
    private String input;

    /**
     * 使用一个字符串作为json输入源
     *
     * @param string
     */
    public JSONSourceImpl(String string) {
        this(string, true);
    }


    /**
     * 使用一个字符串作为json输入源
     *
     * @param string
     * @param unicodeDecode 是否使用unicode解码器解码
     */
    public JSONSourceImpl(String string, boolean unicodeDecode) {
        this.input = unicodeDecode ? unicodeDecode(new StringBuilder(string)) : string;
    }


    /**
     * 使用一个byte数组 作为json输入源 编码方式使用默认的UTF-8
     *
     * @param buf
     */
    public JSONSourceImpl(byte[] buf) {
        try {
            this.input = new String(buf, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param buf     使用一个byte数组 作为json输入源
     * @param charset 指定byte[]转换为String的编码方式
     */
    public JSONSourceImpl(byte[] buf, String charset) throws UnsupportedEncodingException {
        this.input = new String(buf, charset);
    }

    /**
     * @param stream 使用一个InputStream数组 作为json输入源
     *               使用默认的编码方式 UTF-8
     */
    public JSONSourceImpl(InputStream stream) throws IOException {
        this(stream, true);
    }

    /**
     * @param stream 使用一个InputStream数组 作为json输入源
     *               使用默认的编码方式 UTF-8
     */
    public JSONSourceImpl(InputStream stream, boolean unicodeDecode) throws IOException {
        this.input = streamToString(stream, DEFAULT_CHARSET, unicodeDecode);
    }

    /**
     * @param stream  使用一个InputStream数组 作为json输入源
     * @param charset 使用指定的编码方式
     */
    public JSONSourceImpl(InputStream stream, String charset, boolean unicodeDecode) throws IOException {
        this.input = streamToString(stream, charset, unicodeDecode);
    }

    /**
     * @param stream  使用一个InputStream数组 作为json输入源
     * @param charset 使用指定的编码方式
     */
    public JSONSourceImpl(InputStream stream, String charset) throws IOException {
        this.input = streamToString(stream, charset, true);
    }

    public static String unicodeDecode(StringBuilder sb) {
        Pattern pattern = Pattern.compile("\\\\u.{4}");
        Matcher matcher = pattern.matcher(sb.toString());
        int len = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start != 0) {
                if (sb.charAt(start - len - 1) != '\\') {
                    CharSequence sub = sb.subSequence(start - len, end - len);
                    sb.replace(start - len, end - len, String.valueOf(hexToChar(sub.toString())));
                    len += 5;
                }
            } else {
                CharSequence sub = sb.subSequence(start - len, end - len);
                sb.replace(start - len, end - len, String.valueOf(hexToChar(sub.toString())));
                len += 5;
            }
        }


        String string = sb.toString();
        char preChar = '\0';
        int dividedCount = 0;

        for (int i = 0; i < string.length(); i++) {
            char currentChar = sb.charAt(i - dividedCount);
            if (preChar == '\\') {
                switch (currentChar) {
                    case 'n':// \n
                        sb.replace(i - dividedCount - 1, i - dividedCount + 1, "\n");
                        dividedCount += 1;
                        break;
                    case 'r':// \r
                        sb.replace(i - dividedCount - 1, i - dividedCount + 1, "\r");
                        dividedCount += 1;
                        break;
                    case 't':// \t
                        sb.replace(i - dividedCount - 1, i - dividedCount + 1, "\t");
                        dividedCount += 1;
                        break;
                    case '\\':// \
                        sb.replace(i - dividedCount - 1, i - dividedCount + 1, "\\");
                        dividedCount += 1;
                        break;
                }
            }
            preChar = (preChar == '\\' && currentChar == '\\') ? '\0' : currentChar;
        }

        return sb.toString();
    }

    private static final char hexToChar(String hex) {
        return (char) Integer.parseInt(hex.substring(2), 16);
    }

    /**
     * 就爱那个输入流转换为string
     *
     * @param stream  需要转换的流
     * @param charset 制定的字符编码集
     * @return 转换好的string
     */
    private String streamToString(InputStream stream, String charset, boolean unicodeDecode) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 8];
        int len = -1;
        while ((len = stream.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        baos.flush();
        String str = new String(baos.toByteArray(), charset);
        baos.close();
        return unicodeDecode ? unicodeDecode(new StringBuilder(str)) : str;
    }

    @Override
    public String input() {
        return input;
    }
}
