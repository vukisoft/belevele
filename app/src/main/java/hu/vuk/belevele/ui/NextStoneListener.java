package hu.vuk.belevele.ui;

import hu.vuk.belevele.game.stone.Stone;

@FunctionalInterface
public interface NextStoneListener {
  void onSelection(Stone stone);
}
