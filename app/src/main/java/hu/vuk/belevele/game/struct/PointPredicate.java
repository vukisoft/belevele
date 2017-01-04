package hu.vuk.belevele.game.struct;

public interface PointPredicate<T> {
  boolean apply(T o, int x, int y);
}
