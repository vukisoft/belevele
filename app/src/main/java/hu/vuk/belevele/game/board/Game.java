package hu.vuk.belevele.game.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

public class Game {
  private final Board board;
  private final NextStones nextStones;

  private Map<Stone, Set<Point>> highlightedPlaces = Collections.emptyMap();

  public Game(Board board, NextStones nextStones) {
    this.board = board;
    this.nextStones = nextStones;
    updatePossiblePlaces();
  }

  /**
   * Return if stone was in fact placed.
   */
  public boolean placeNext(int x, int y) {
    if (nextStones.hasNext()
        && board.place(x, y, nextStones.getSelected())) {
      nextStones.rollSelected();
      updatePossiblePlaces();
      return true;
    }
    return false;
  }

  private void updatePossiblePlaces() {
    Set<Stone> availableStones = new HashSet<>(nextStones.getCount());
    highlightedPlaces = new HashMap<>();
    for (Stone stone : nextStones.getAll()) {
      boolean placeable = true;
      Set<Point> places = Collections.emptySet();
      if (board.hasMatching(stone)) {
        places = board.getAvailablePlaces(stone);
        if (places.isEmpty()) {
          placeable = false;
        }
      }
      highlightedPlaces.put(stone, places);
      if (placeable) {
        availableStones.add(stone);
      }
    }

    nextStones.setAvailabes(availableStones);
  }

  public boolean isOver() {
    return !nextStones.hasAvailable();
  }

  public NextStones getNextStones() {
    return nextStones;
  }

  public Board getBoard() {
    return board;
  }

  public boolean isHighlightedPlaceForSelected(Point point) {
    Set<Point> points = highlightedPlaces.get(nextStones.getSelected());
    return points != null && points.contains(point);
  }

  public boolean isHighlightedPlace(Point point) {
    for (Set<Point> points : highlightedPlaces.values()) {
      if (points.contains(point)) {
        return true;
      }
    }
    return false;
  }
}
