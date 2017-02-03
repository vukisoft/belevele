package hu.vuk.belevele.game.struct;

public abstract class Matrix6<T> extends Matrix<T> {

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
  public int getMaxNeighbours() {
    return 6;
  }
}
