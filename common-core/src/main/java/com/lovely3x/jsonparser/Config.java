package com.lovely3x.jsonparser;

import com.lovely3x.jsonparser.classcreator.ClassCreator;
import com.lovely3x.jsonparser.classcreator.UnderlineClassCreator;
import com.lovely3x.jsonparser.formatter.JSONFormatter;
import com.lovely3x.jsonparser.formatter.JSONFormatterImpl;
import com.lovely3x.jsonparser.matcher.JSONMatcher;
import com.lovely3x.jsonparser.matcher.MixMatcher;
import com.lovely3x.jsonparser.model.JSONPairFactory;
import com.lovely3x.jsonparser.model.JSONPairFactoryImpl;
import com.lovely3x.jsonparser.objectcreator.ObjectCreator;
import com.lovely3x.jsonparser.objectcreator.SuperObjectCreator;
import org.json2.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON 解析器的配置文件
 * Created by lovely3x on 16-3-10.
 */
public class Config {

    /**
     * 解析器
     */
    public JSONParser parser;

    /**
     * 匹配器
     */
    public JSONMatcher matcher;

    /**
     * 对象创建器
     */
    public ObjectCreator creator;
    /**
     * json对工厂
     */
    public JSONPairFactory pairFactory;

    /**
     * 类创建器
     */
    public ClassCreator classCreator;

    /**
     * 格式化器
     */
    public JSONFormatter mFormatter;

    /**
     * 默认的json数组容器
     */
    public Class<? extends List> defaultJSONArrayContainer = ArrayList.class;


    public Config(JSONParser parser, JSONMatcher matcher, SuperObjectCreator creator, JSONPairFactory pairFactory, ClassCreator classCreator, JSONFormatter mFormatter) {
        this.parser = parser;
        this.matcher = matcher;
        this.creator = creator;
        this.pairFactory = pairFactory;
        this.classCreator = classCreator;
        this.mFormatter = mFormatter;
    }

    public Config() {
    }

    public static Config createDefault() {
        Config config = new Config();
        config.parser = new JSONParser(config);
        config.pairFactory = new JSONPairFactoryImpl(config);
        config.creator = new SuperObjectCreator(config, true);
        config.classCreator = new UnderlineClassCreator();
        config.mFormatter = new JSONFormatterImpl(config);
        config.matcher = new MixMatcher(config);
        return config;
    }

}
