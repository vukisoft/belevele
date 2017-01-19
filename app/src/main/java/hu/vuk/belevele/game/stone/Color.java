package hu.vuk.belevele.game.stone;

import android.support.annotation.ColorInt;

public enum Color {
  RED(android.graphics.Color.RED),
  BLUE(android.graphics.Color.BLUE),
  GREEN(android.graphics.Color.GREEN),
  MAGENTA(android.graphics.Color.MAGENTA),
  GRAY(android.graphics.Color.GRAY),
  YELLOW(android.graphics.Color.YELLOW);

  public final int color;

  Color(@ColorInt int color) {
    this.color = color;
  }
}
