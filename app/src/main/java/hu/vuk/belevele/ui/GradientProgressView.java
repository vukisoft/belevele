package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkArgument;

public class GradientProgressView extends GridView {
  private int min = 0;
  private int max = 0;
  private int value = 0;
  private ImmutableList<Integer> colors;

  public GradientProgressView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setColorRange(Integer... colors) {
    checkArgument(colors.length > 1);
    this.colors = FluentIterable.from(colors)
        .transform(colorId -> ContextCompat.getColor(getContext(), colorId))
        .toList();
    setGap(3);
  }

  public void setRange(int min, int max) {
    checkArgument(min < max);
    this.min = min;
    this.max = max;
    setDimensions(max - min, 1);
  }

  public void setValue(int value) {
    this.value = value;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  @Override
  protected void drawCell(int x, int y, Canvas canvas, Rect rect) {
    if (colors == null) {
      return;
    }

    if (x + min + 1 > value) {
      return;
    }

    double colorGradient = (double) ((colors.size() - 1) * x) / (max - min);
    int color = gradient(
        (int) colorGradient,
        (int) colorGradient + 1,
        colorGradient - ((int) colorGradient));

    Paint paint = new Paint();
    paint.setColor(color);
    canvas.drawRect(rect, paint);
  }

  private int gradient(int colorIndex1, int colorIndex2, double gradient) {
    int color1 = colors.get(colorIndex1);
    int color2 = colors.get(colorIndex2);
    return Color.argb(255,
        (int) (Color.red(color1) * (1 - gradient) + Color.red(color2) * gradient),
        (int) (Color.green(color1) * (1 - gradient) + Color.green(color2) * gradient),
        (int) (Color.blue(color1) * (1 - gradient) + Color.blue(color2) * gradient));
  }
}
