# OurApps

开发环境 ：AS 3.0

开发语言：kotlin + java (基础类为java原生，View层为kotlin)

框架：butterknife Retrofit +RxAndroid v-layout gilde

主要功能：
 1 仿网易新闻版块
 2 仿网易云音乐版块
 3 记事本
 4 天气预报
 
![这里写什么](https://github.com/bobfromchina/OurApps/blob/master/app/src/main/res/drawable-xxhdpi/1.jpeg)

 以学习为兴趣来写一些自己想要玩的东西。

![这里写什么](https://github.com/bobfromchina/OurApps/blob/master/app/src/main/res/drawable-xxhdpi/2.jpeg)

用到的jar包：
 compile fileTree(include: ['*.jar'], dir: 'libs')

    //网页解析
    compile 'org.jsoup:jsoup:1.10.2'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'

    //注解框架
    compile 'com.jakewharton:butterknife:8.8.1'
    kapt "com.jakewharton:butterknife-compiler:8.8.1"

    compile 'com.aliyun.dpa:oss-android-sdk:2.3.0'

    compile project(':jframelibray')

    compile project(':refresh-header')

    compile project(':refresh-layout')

    compile project(':action-sheet-library')

    compile('com.alibaba.android:vlayout:1.0.4@aar') {
        transitive = true
    }
    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
