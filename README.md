# OurApps

 compile fileTree(include: ['*.jar'], dir: 'libs')

    //网页解析
    compile 'org.jsoup:jsoup:1.10.2'

    // 架包支持
    compile "com.android.support:palette-v7:$SUPPORT_LIBRARY_VERSION"

    compile 'com.android.support.constraint:constraint-layout:1.0.2'

    //注解框架
    compile 'com.jakewharton:butterknife:8.8.1'
//    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    kapt "com.jakewharton:butterknife-compiler:8.8.1"


    compile 'com.aliyun.dpa:oss-android-sdk:2.3.0'

    compile project(':jframelibray')

    compile project(':refresh-header')

    compile project(':refresh-layout')

    //仿照IOS的弹窗效果
    compile project(':action-sheet-library')

    compile('com.alibaba.android:vlayout:1.0.4@aar') {
        transitive = true
    }
    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
