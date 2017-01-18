package hu.vuk.belevele.ui;

import android.graphics.Canvas;
import android.graphics.Rect;

import hu.vuk.belevele.game.stone.Stone;

public interface DrawableResourceService {
  void drawStone(Stone stone, Canvas canvas, Rect rect);
  void drawDrawable(int resourceId, Canvas canvas, Rect rect);
  void drawDrawable(int resourceId, int alpha, Canvas canvas, Rect rect);
}
