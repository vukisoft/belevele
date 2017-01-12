package hu.vuk.belevele.game.stone;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Stone {
  private final Color color;
  private final Shape shape;

  public Stone(Color color, Shape shape) {
    this.color = checkNotNull(color);
    this.shape = checkNotNull(shape);
  }

  public Color getColor() {
    return color;
  }

  public Shape getShape() {
    return shape;
  }

  public boolean matchesColor(Stone other) {
    return getColor().equals(other.getColor());
  }

  public boolean matchesShape(Stone other) {
    return getShape().equals(other.getShape());
  }

  public boolean matchesShapeOrColor(Stone other) {
    return matchesShape(other) || matchesColor(other);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(color, shape);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Stone other = (Stone) obj;
    return Objects.equal(color, other.color)
        && Objects.equal(shape, other.shape);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
