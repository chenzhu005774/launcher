package com.amtzhmt.launcher;

import android.app.Application;

import com.amtzhmt.launcher.util.utils.ProperTies;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Properties;

/**
 * Created by Administrator on 2019/4/3.
 */
public class App extends Application {
    public static Properties proper ;
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) //缓存 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);

        proper = ProperTies.getProperties(getApplicationContext());
        String magin = App. proper.getProperty("magin");
    }
}
