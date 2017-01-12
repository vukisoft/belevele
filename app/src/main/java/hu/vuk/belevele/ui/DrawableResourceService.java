package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.text.MessageFormat;

import hu.vuk.belevele.game.stone.Stone;

public class DrawableResourceService {

  private static final String FILE_NAME_FORMAT = "{0}_{1}";

  private final LoadingCache<Integer, Drawable> drawableCache;
  private final LoadingCache<String, Integer> idCache;

  public DrawableResourceService(Context context) {
    drawableCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(context::getDrawable));
    idCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(
             name ->
                context.getResources().getIdentifier(name, "drawable", context.getPackageName())));
  }

  private String getStoneName(Stone stone) {
    Preconditions.checkArgument(stone != null);

    return MessageFormat.format(
        FILE_NAME_FORMAT,
        stone.getShape().name().toLowerCase(),
        stone.getColor().name().toLowerCase());
  }

  private int getResourceId(String name) {
    return idCache.getUnchecked(name);
  }

  private Drawable getResource(String name) {
    return getResource(getResourceId(name));
  }

  public Drawable getResource(int resourceid) {
    return drawableCache.getUnchecked(resourceid);
  }

  public Drawable getStoneResource(Stone stone) {
    return getResource(getStoneName(stone));
  }
}
