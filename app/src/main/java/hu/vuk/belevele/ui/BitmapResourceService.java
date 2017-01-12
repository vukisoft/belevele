package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.text.MessageFormat;

import hu.vuk.belevele.game.stone.Stone;

public class BitmapResourceService {

  private static final String RESOURCE_NAME_FORMAT = "{0}_{1}";

  private final LoadingCache<Integer, Bitmap> bitmapCache;
  private final LoadingCache<String, Integer> resourceIdCache;

  public BitmapResourceService(Context context) {
    bitmapCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(
            (Integer resourceId) ->
                BitmapFactory.decodeResource(context.getResources(), resourceId)));
    resourceIdCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(
             name ->
                context.getResources().getIdentifier(name, "drawable", context.getPackageName())));
  }

  private String getStoneName(Stone stone) {
    Preconditions.checkArgument(stone != null);

    return MessageFormat.format(
        RESOURCE_NAME_FORMAT,
        stone.getShape().name().toLowerCase(),
        stone.getColor().name().toLowerCase());
  }

  private int getResourceId(String name) {
    return resourceIdCache.getUnchecked(name);
  }

  private Bitmap getBitmap(String name) {
    return getBitmap(getResourceId(name));
  }

  public Bitmap getBitmap(int resourceid) {
    return bitmapCache.getUnchecked(resourceid);
  }

  public Bitmap getStoneBitmap(Stone stone) {
    return getBitmap(getStoneName(stone));
  }
}
