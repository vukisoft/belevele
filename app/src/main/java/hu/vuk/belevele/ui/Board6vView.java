package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.stone.Stone;

public class Board6vView extends BoardView {

  public static final float RATIO = (float) (Math.sqrt(3) / 2);

  public Board6vView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setRatio(RATIO);
  }

  @Override
  public void drawCell(int x, int y, Canvas canvas, Rect rect) {
    if (game.getBoard().isOutOfBounds(x, y)) {
      return;
    }
    rect.offset((y % 2) * rect.width() / 2, 0);

    float height = (float) (rect.width() / RATIO);

    Stone stone = game.getBoard().get(x, y);
    if (stone == null) {
      canvas.drawPath(
          createHexagonalPath(rect.exactCenterX(), rect.exactCenterY(), height / 2),
          backgroundTiles.getPaint(x, y));
    } else {
      drawableService.draw(R.drawable.stone6v)
          .withRectTransformation(r -> r.inset(1, (int) ((rect.height() - height) / 2) + 1))
          .to(canvas, rect);
      drawableService.draw(stone.getShape().resourceId)
          .withColor(stone.getColor().color)
          .withShadow()
          .withRectTransformation(r -> r.inset(0, (rect.height() - rect.width()) / 2))
          .withRectShrinkingByRatio(0.5f)
          .to(canvas, rect);
    }

    drawMark(x, y, canvas, rect);
  }

  private Path createHexagonalPath(float cx, float cy, float r) {
    Path path = new Path();
    path.moveTo(angleX(cx, 30, r), angleY(cy, 30, r));
    for (int i = 90; i <= 390; i+= 60) {
      path.lineTo(angleX(cx, i, r), angleY(cy, i, r));
    }
    return path;
  }

  private float angleX(float cx, int angle, float r) {
    return (float) (cx + Math.cos(Math.toRadians(angle)) * r);
  }

  private float angleY(float cy, int angle, float r) {
    return (float) (cy + Math.sin(Math.toRadians(angle)) * r);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN) {
      // we don't care if not down
      return false;
    }

    int y = getTouchedY(event.getY());
    int x = getTouchedX(event.getX() - y % 2 * getWidth() / getGridWidth() / 2);
    if (game.placeNext(x, y)) {
      fireScoreEvent();
      invalidate();
    }

    return true;
  }
}
