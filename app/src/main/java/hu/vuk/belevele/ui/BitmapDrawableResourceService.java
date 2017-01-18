package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.stone.Stone;

public class BitmapDrawableResourceService implements DrawableResourceService {

  private final LoadingCache<Integer, Bitmap> bitmapCache;
  private final LoadingCache<String, Integer> resourceIdCache;

  public BitmapDrawableResourceService(Context context) {
    bitmapCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(
            (Integer resourceId) ->
                BitmapFactory.decodeResource(context.getResources(), resourceId)));
    resourceIdCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from(
            name ->
                context.getResources().getIdentifier(name, "drawable", context.getPackageName())));
  }

  private int getResourceId(String name) {
    return resourceIdCache.getUnchecked(name);
  }

  public void drawStone(Stone stone, Canvas canvas, Rect rect) {
    drawDrawable(R.drawable.stone, canvas, rect);
    drawDrawable(
        getResourceId(
            stone.getShape().name().toLowerCase() + "_" + stone.getColor().name().toLowerCase()),
        canvas, rect);
  }

  @Override
  public void drawDrawable(int resourceId, int alpha, Canvas canvas, Rect rect) {
    Paint paint = new Paint();
    paint.setAlpha(alpha);
    drawDrawable(resourceId, canvas, rect, paint);
  }

  @Override
  public void drawDrawable(int resourceId, Canvas canvas, Rect rect) {
    drawDrawable(resourceId, 255, canvas, rect);
  }

  private void drawDrawable(int resourceId, Canvas canvas, Rect rect, Paint paint) {
    Bitmap bitmap = getBitmap(resourceId);
    if (bitmap != null) {
      Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
      canvas.drawBitmap(bitmap, src, rect, paint);
    }
  }

  private Bitmap getBitmap(int resourceId) {
    return bitmapCache.getUnchecked(resourceId);
  }
}
