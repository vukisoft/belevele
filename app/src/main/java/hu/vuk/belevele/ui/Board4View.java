package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

public class Board4View extends BoardView {

  public Board4View(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void drawCell(int x, int y, Canvas canvas, Rect rect) {
    Stone stone = game.getBoard().get(x, y);
    if (stone == null) {
      canvas.drawRect(rect, backgroundTiles.getPaint(x, y));
    } else {
      drawableService.drawStone(stone, canvas, rect);
    }

    drawMark(x, y, canvas, rect);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN) {
      // we don't care if not down
      return false;
    }

    Point point = getTouchedCell(event.getX(), event.getY());
    if (game.placeNext(point.getX(), point.getY())) {
      fireScoreEvent();
      invalidate();
    }

    return true;
  }
}
