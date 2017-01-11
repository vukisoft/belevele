package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hu.vuk.belevele.game.struct.Point;

public abstract class GridView extends View {

  private int width;
  private int height;

  private int gap = 0;

  public GridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected boolean setDimensions(int width, int height) {
    if (this.width == width && this.height == height) {
      return false;
    }
    this.width = width;
    this.height = height;
    return true;
  }

  public void setGap(int gap) {
    this.gap = gap;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (width == 0 || height == 0) {
      return;
    }
    int visibleWidth = (canvas.getWidth() / width) - gap;
    int visibleHeight = (canvas.getHeight() / height) - gap;

    int posx = 0;
    for (int cellx = 0; cellx < width; cellx++) {
      int posy = 0;
      for (int celly = 0; celly < height; celly++) {
        Rect dst = new Rect(posx, posy, posx + visibleWidth, posy + visibleHeight);
        drawCell(cellx, celly, canvas, dst);
        posy += visibleHeight + gap;
      }
      posx += visibleWidth + gap;
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

  protected abstract void drawCell(int x, int y, Canvas canvas, Rect rect);

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (width == 0 || height == 0) {
      return;
    }

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
