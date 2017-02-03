package hu.vuk.belevele.game.struct;

import com.google.common.collect.ImmutableList;

public class Matrix6h<T> extends Matrix6<T> {

  public Matrix6h(int width, int height, Class<T> type) {
    super(width, height, type);
  }

  @Override
  protected Iterable<Point> getAllNeighbourPoints(int x, int y) {
    int mod = y % 2;
    return ImmutableList.of(
        new Point(x, y - 2),
        new Point(x, y + 2),
        new Point(x - (1 - mod), y - 1),
        new Point(x - (1 - mod), y + 1),
        new Point(x + mod, y + 1),
        new Point(x + mod, y + 1));
  }
}
