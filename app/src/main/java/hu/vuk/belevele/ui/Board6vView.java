package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
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

    Consumer<Rect> hexagonRectTransformation =
        r -> r.inset(1, (int) ((rect.height() - (rect.width() / RATIO)) / 2) + 1);

    Stone stone = game.getBoard().get(x, y);
    if (stone == null) {
      drawableService.draw(R.drawable.bg_hexagon)
          .withRectTransformation(hexagonRectTransformation)
          .withColor(backgroundTiles.getColor(x, y))
          .to(canvas, rect);
    } else {
      drawableService.draw(R.drawable.stone6v)
          .withRectTransformation(hexagonRectTransformation)
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
