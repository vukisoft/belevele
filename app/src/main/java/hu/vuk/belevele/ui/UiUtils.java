package hu.vuk.belevele.ui;

import android.graphics.Rect;

public class UiUtils {
  /**
   * Shrinks the rect area by given ratio. 0 means no change, 1 (100%) means rect area shrinks to 0.
   */
  public static void shrinkByRatio(Rect rect, float ratio) {
    rect.inset(
        (int) (ratio / 2 * rect.width()),
        (int) (ratio / 2 * rect.height()));
  }
}
