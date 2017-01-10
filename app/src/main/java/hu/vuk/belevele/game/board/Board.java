package hu.vuk.belevele.game.board;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Set;

import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Matrix;
import hu.vuk.belevele.game.struct.Point;

public class Board {

  private final Matrix<Stone> matrix;

  private int score = 0;
  private int multiplier = 1;

  public Board(int width, int height) {
    matrix = new Matrix<>(width, height, Stone.class);
  }

  public final void newGame() {
    matrix.clear();
    score = 0;
    multiplier = 1;
  }

  /**
   * Places the stone to the location.
   *
   * @return if the stone was actually placed (place was valid)
   */
  public boolean place(int x, int y, Stone stone) {

    Collection<Stone> neighbours = getPlacedNeighbours(x, y, stone);
    if (neighbours == null) {
      // not valid place
      return false;
    }

    score += multiplier * neighbours.size();

    if (neighbours.size() == matrix.getMaxNeigbours()) {
      // we placed among 4 others
      multiplier++;
    }

    matrix.set(x, y, stone);
    return true;
  }

  /**
   * Returns neighbour stones of the place if the given stone can be placed. Null, if the stone can't be placed here.
   */
  private Collection<Stone> getPlacedNeighbours(int x, int y, Stone stone) {
    if (matrix.isOutOfBounds(x, y)) {
      // out of bounds
      return null;
    }

    if (matrix.get(x, y) != null) {
      // place already contains a stone
      return null;
    }

    Collection<Stone> neighbours = matrix.getNeighbours(x, y);
    if (neighbours.size() > 0) {
      // if there are neighbours, must match to one of them
      for (Stone neighbour : neighbours) {
        if (!neighbour.matchesShapeOrColor(stone)) {
          return null;
        }
      }
    } else {
      // no neighbours, so must be no other stones with matching shape or colour
      if (hasMatching(stone)) {
        return null;
      }
    }

    return neighbours;
  }

  private boolean isValidPlace(int x, int y, Stone stone) {
    return getPlacedNeighbours(x, y, stone) != null;
  }

  public Set<Point> getAvailablePlaces(final Stone stone) {
    return matrix.getAllPlaces((o, x, y) ->  isValidPlace(x, y, stone));
  }

  public int getWidth() {
    return matrix.getWidth();
  }

  public int getHeight() {
    return matrix.getHeight();
  }

  public Stone get(int x, int y) {
    return matrix.get(x, y);
  }

  public int getScore() {
    return score;
  }

  public int getMultiplier() {
    return multiplier;
  }

  public boolean hasMatching(Stone stone) {
    return matrix.getAll(new ShapeOrColorMatcher(stone)).size() > 0;
  }

  private static class ShapeOrColorMatcher implements Predicate<Stone> {
    private final Stone match;

    public ShapeOrColorMatcher(Stone match) {
      this.match = match;
    }

    @Override
    public boolean apply(Stone o) {
      return o.matchesShapeOrColor(match);
    }
  }
}
