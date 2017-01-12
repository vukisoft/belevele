package hu.vuk.belevele.game.struct;

import com.google.common.collect.ImmutableList;

public class Matrix4<T> extends Matrix<T> {

  private static final ImmutableList<Point> NEIGBOUR_DELTA_POINTS = ImmutableList.of(
  );

  public Matrix4(int width, int height, Class<T> type) {
    super(width, height, type);
  }

  public boolean isOutOfBounds(int x, int y) {
    return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
  }

  @Override
  protected Iterable<Point> getAllNeighbourPoints(int x, int y) {
    return ImmutableList.of(
        new Point(x + 1, y),
        new Point(x - 1, y),
        new Point(x, y + 1),
        new Point(x, y - 1)
    );
  }

  @Override
  public int getMaxNeighbours() {
    return 4;
  }
}
