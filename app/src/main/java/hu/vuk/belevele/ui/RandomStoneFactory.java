package hu.vuk.belevele.ui;

import java.util.Random;

import hu.vuk.belevele.game.board.StoneFactory;
import hu.vuk.belevele.game.stone.Color;
import hu.vuk.belevele.game.stone.Shape;
import hu.vuk.belevele.game.stone.Stone;

/**
 * Creates a random stone.
 *
 * @author Marton Dinnyes
 */
public class RandomStoneFactory implements StoneFactory {
  private static final Random RND = new Random();

  @Override
  public Stone create() {
    return new Stone(createRandomEnum(Color.class), createRandomEnum(Shape.class));
  }

  private <T extends Enum<T>> T createRandomEnum(Class<T> type) {
    T[] values = type.getEnumConstants();
    return values[RND.nextInt(values.length)];
  }
}
