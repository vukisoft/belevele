package hu.vuk.belevele.game.stone;

import hu.vuk.belevele.R;

public enum Shape {
  SQUARE(R.drawable.ic_square),
  DROP(R.drawable.ic_drop),
  CROSS(R.drawable.ic_cross),
  STAR(R.drawable.ic_star);

  public final int resourceId;

  Shape(int resourceId) {
    this.resourceId = resourceId;
  }
}
