package hu.vuk.belevele.ui;

@FunctionalInterface
public interface Consumer<T> {
  void accept(T object);
}
