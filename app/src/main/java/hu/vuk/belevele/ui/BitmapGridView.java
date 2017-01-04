package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hu.vuk.belevele.game.struct.Point;

public class BitmapGridView extends View {

  private DrawStrategy bitmapProvider;

  private int width;
  private int height;

  public BitmapGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected void setDimensions(int width, int height) {
    this.width = width;
    this.height = height;
  }

  protected void setBitmapProvider(DrawStrategy bitmapProvider) {
    this.bitmapProvider = bitmapProvider;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int cwidth = canvas.getWidth();
    int cheight = canvas.getHeight();
    int visibleWidth = cwidth / width;
    int visibleHeight = cheight / height;

    int startx = 0;
    for (int x = 0; x < width; x++) {
      int starty = 0;
      for (int y = 0; y < height; y++) {
        Rect dst = new Rect(startx, starty, startx + visibleWidth - 1, starty + visibleHeight - 1);
        bitmapProvider.draw(x, y, canvas, dst);
        starty += visibleHeight;
      }
      startx += visibleWidth;
    }
  }

  protected Point getTouchedCell(MotionEvent event) {
    int cwidth = getWidth();
    int cheight = getHeight();
    int visibleWidth = cwidth / width;
    int visibleHeight = cheight / height;
    int x = (int) (event.getX() / visibleWidth);
    int y = (int) (event.getY() / visibleHeight);
    return new Point(x, y);
  }

  protected static abstract class DrawBitmapStrategy implements DrawStrategy {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void draw(int x, int y, Canvas canvas, Rect rect) {
      Bitmap bitmap = getBitmap(x, y);
      if (bitmap != null) {
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, src, rect, paint);
      }
    }

    protected abstract Bitmap getBitmap(int x, int y);
  }

  protected static interface DrawStrategy {
    void draw(int x, int y, Canvas canvas, Rect rect);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
    int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

    int calculatedHeight = originalWidth * height / width;

    int finalWidth, finalHeight;

    if (calculatedHeight > originalHeight) {
      finalWidth = originalHeight * width / height;
      finalHeight = originalHeight;
    } else {
      finalWidth = originalWidth;
      finalHeight = calculatedHeight;
    }

    super.onMeasure(
        MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
  }
}
