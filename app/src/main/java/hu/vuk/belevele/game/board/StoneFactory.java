package hu.vuk.belevele.game.board;

import hu.vuk.belevele.game.stone.Stone;

public interface StoneFactory {
  /**
   * Creates a stone. Surprising, huh?
   */
  Stone create();
}