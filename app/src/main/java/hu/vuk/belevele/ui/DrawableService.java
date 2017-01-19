package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.stone.Stone;

public class DrawableService {

  public static final int NO_VALUE = 0;
  public static final double STONE_INSET_RATIO = 0.2  ;
  public static final float SHADOW_OFFSET = 1.5f;

  private final LoadingCache<DrawableKey, Drawable> drawableCache;
  private final LoadingCache<String, Integer> resourceIdCache;
  private final float dpToPx;

  public DrawableService(Context context) {
    dpToPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    drawableCache = CacheBuilder.newBuilder()
        .build(CacheLoader.from((DrawableKey key) -> {
          Drawable drawable = ContextCompat.getDrawable(context, key.resourceId);
          if (key.color != NO_VALUE) {
            drawable = drawable.mutate();
            drawable.setColorFilter(key.color, PorterDuff.Mode.MULTIPLY);
          }
          if (key.alpha != NO_VALUE) {
            drawable = drawable.mutate();
            drawable.setAlpha(key.alpha);
          }
          return drawable;
        }));
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

    rect.inset((int) (STONE_INSET_RATIO * rect.width()), (int) (STONE_INSET_RATIO * rect.height()));

    int shadowOffset = toPx(SHADOW_OFFSET);
    rect.offset(shadowOffset, shadowOffset);
    Drawable shadow = getDrawable(getResourceId("ic_" + stone.getShape().name().toLowerCase()))
        .withColor(Color.BLACK)
        .withAlpha(180)
        .load();
    drawDrawable(shadow, canvas, rect);
    rect.offset(-shadowOffset, -shadowOffset);
    Drawable drawable = getDrawable(getResourceId("ic_" + stone.getShape().name().toLowerCase()))
        .withColor(stone.getColor().color)
        .load();
    drawDrawable(drawable, canvas, rect);
  }

  public void drawDrawable(int resourceId, Canvas canvas, Rect rect) {
    drawDrawable(getDrawable(resourceId).load(), canvas, rect);
  }

  public void drawDrawable(Drawable drawable, Canvas canvas, Rect rect) {
    drawable.setBounds(rect);
    drawable.draw(canvas);
  }

  public DrawableLoader newDrawableLoader(int resourceId) {
    return new DrawableLoader(resourceId);
  }

  private DrawableLoader getDrawable(int resourceId) {
    return new DrawableLoader(resourceId);
  }

  private int toPx(float dp) {
    return (int) (dpToPx * dp);
  }

  public class DrawableLoader {
    private final int resourceId;
    private int color = NO_VALUE;
    private int alpha = NO_VALUE;

    DrawableLoader(int resourceId) {
      this.resourceId = resourceId;
    }

    public DrawableLoader withColor(int color) {
      this.color = color;
      return this;
    }

    public DrawableLoader withAlpha(int alpha) {
      this.alpha = alpha;
      return this;
    }

    public Drawable load() {
      return drawableCache.getUnchecked(new DrawableKey(resourceId, color, alpha));
    }
  }

  static class DrawableKey {
    final int resourceId;
    final int color;
    final int alpha;

    DrawableKey(int resourceId, int color, int alpha) {
      this.resourceId = resourceId;
      this.color = color;
      this.alpha = alpha;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      DrawableKey that = (DrawableKey) o;

      return Objects.equal(resourceId, that.resourceId)
          && Objects.equal(color, that.color)
          && Objects.equal(alpha, that.alpha);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(resourceId, color, alpha);
    }
  }
}
