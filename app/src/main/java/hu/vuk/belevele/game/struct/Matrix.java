package hu.vuk.belevele.game.struct;

import com.google.common.base.Predicate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Matrix<T> {

  private static final int MAX_NEIGBOURS = 4;

  private T[][] data;
  private int width;
  private int height;

  @SuppressWarnings("unchecked")
  public Matrix(int width, int height, Class<T> type) {
    this.width = width;
    this.height = height;
    this.data = (T[][]) Array.newInstance(type, width, height);
  }

  public T get(int x, int y) {
    if (isOutOfBounds(x, y)) {
      return null;
    }

    return data[x][y];
  }

  public boolean isOutOfBounds(int x, int y) {
    return x < 0 || y < 0 || x >= width || y >= height;
  }

  public void set(int x, int y, T o) {
    if (isOutOfBounds(x, y)) {
      return;
    }

    data[x][y] = o;
  }

  public Collection<T> getAll(Predicate<T> predicate) {

    List<T> ret = new LinkedList<>();
    for (T[] a : data) {
      for (T o : a) {
        if (o != null && predicate.apply(o)) {
          ret.add(o);
        }
      }
    }

    return ret;
  }

  public Set<Point> getAllPlaces(PointPredicate<T> criteria) {
    Set<Point> ret = new HashSet<>();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        T o = get(x, y);
        if (criteria == null || criteria.apply(o, x, y)) {
          ret.add(new Point(x, y));
        }
      }
    }

    return ret;
  }

  public Collection<T> getNeighbours(int x, int y) {
    List<T> ret = new ArrayList<>(4);
    addIfNotNull(ret, x - 1, y);
    addIfNotNull(ret, x + 1, y);
    addIfNotNull(ret, x, y - 1);
    addIfNotNull(ret, x, y + 1);
    return ret;
  }

  public Collection<Point> getNeighbourPoints(int x, int y) {
    List<Point> ret = new ArrayList<>(4);
    addIfIn(ret, x - 1, y);
    addIfIn(ret, x + 1, y);
    addIfIn(ret, x, y - 1);
    addIfIn(ret, x, y + 1);
    return ret;
  }

  private void addIfIn(List<Point> ret, int x, int y) {
    if (!isOutOfBounds(x, y)) {
      ret.add(new Point(x, y));
    }
  }

  private void addIfNotNull(Collection<T> ret, int x, int y) {
    T o = get(x, y);
    if (o != null) {
      ret.add(o);
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void clear() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < width; y++) {
        set(x, y, null);
      }
    }
  }

  public int getMaxNeigbours() {
    return MAX_NEIGBOURS;
  }
}
