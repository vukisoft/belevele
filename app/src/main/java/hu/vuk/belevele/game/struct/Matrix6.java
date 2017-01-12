package hu.vuk.belevele.game.struct;

import com.google.common.collect.ImmutableList;

public class Matrix6<T> extends Matrix<T> {

  public Matrix6(int width, int height, Class<T> type) {
    super(width, height, type);
  }

  public boolean isOutOfBounds(int x, int y) {
    return x < 0 || y < 0 || x >= widthOf(y) || y >= getHeight();
  }

  private int widthOf(int y) {
    return getWidth() - y % 2;
  }

  @Override
  protected Iterable<Point> getAllNeighbourPoints(int x, int y) {
    int mod = y % 2;
    return ImmutableList.of(
        new Point(x - 1, y),
        new Point(x + 1, y),
        new Point(x - 1 + mod, y - 1),
        new Point(x + mod, y - 1),
        new Point(x - 1 + mod, y + 1),
        new Point(x + mod, y + 1));
  }

  @Override
  public int getMaxNeighbours() {
    return 6;
  }
}
