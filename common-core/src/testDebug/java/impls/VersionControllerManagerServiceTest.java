package impls;

import android.net.Uri;

import org.junit.Test;

import java.io.File;

/**
 * Created by lovely3x on 16/12/12.
 */
public class VersionControllerManagerServiceTest {

    @Test
    public void testAndroidUri() {
        File file = new File("/sdcard/storage/0/DCIM/test.png");
        Uri uri = Uri.fromFile(file);
        System.out.println(uri);
        System.out.println(uri.getFragment());
        System.out.println(uri.getScheme());
    }
}