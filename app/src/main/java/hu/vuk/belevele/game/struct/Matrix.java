package hu.vuk.belevele.game.struct;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Predicates.notNull;

public abstract class Matrix<T> {

  private T[][] data;
  private int width;
  private int height;

  @SuppressWarnings("unchecked")
  public Matrix(int width, int height, Class<T> type) {
    this.width = width;
    this.height = height;
    this.data = (T[][]) Array.newInstance(type, width, height);
  }

  public abstract boolean isOutOfBounds(int x, int y);
  public abstract int getMaxNeighbours();
  protected abstract Iterable<Point> getAllNeighbourPoints(int x, int y);

  public T get(int x, int y) {
    if (isOutOfBounds(x, y)) {
      return null;
    }

    return data[x][y];
  }

  public void set(int x, int y, T o) {
    if (isOutOfBounds(x, y)) {
      return;
    }

    data[x][y] = o;
  }

  public Collection<T> getAll(Predicate<T> predicate) {
    List<T> ret = new LinkedList<>();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        T o = get(x, y);
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
        if (isOutOfBounds(x, y)) {
          continue;
        }
        T o = get(x, y);
        if (criteria == null || criteria.apply(o, x, y)) {
          ret.add(new Point(x, y));
        }
      }
    }

    return ret;
  }

  public Collection<T> getNeighbours(int x, int y) {
    return FluentIterable.from(getNeighbourPoints(x, y))
        .transform(point -> get(point.getX(), point.getY()))
        .filter(notNull())
        .toList();
  }

  public Collection<Point> getNeighbourPoints(int x, int y) {
    return FluentIterable.from(getAllNeighbourPoints(x, y))
        .filter(point -> !isOutOfBounds(point.getX(), point.getY()))
        .toList();
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
        data[x][y] = null;
      }
    }
  }
}
