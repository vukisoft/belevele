package hu.vuk.belevele.game.struct;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Point {
  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(x, y);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Point other = (Point) obj;
    return Objects.equal(x, other.x)
        && Objects.equal(y, other.y);
  }
}
