package edu.gvsu.cis.campbjos.youtubemvp;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @author josiah
 * @version 12/29/15
 */
public class YouTubeMvpApplication extends Application {

  private static Context context;

  public void onCreate() {
    super.onCreate();
    YouTubeMvpApplication.context = getApplicationContext();
  }

  public static Context getAppContext() {
    return YouTubeMvpApplication.context;
  }

  /*
   * Public static method to retrieve manifest keys. Throws NameNotFoundException
   *
   * @param key    key name
   * @param parent calling activity
   * @return value from key
   */
  public static String getManifestValue(String key)
      throws PackageManager.NameNotFoundException {

    String value;

    ApplicationInfo ai = context.getPackageManager()
        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
    value = (String) ai.metaData.get(key);

    return value;
  }
}
