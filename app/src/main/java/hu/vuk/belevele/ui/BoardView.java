package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.Board;
import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

public class BoardView extends BitmapGridView {

  private static final int BOARD_SIZE = 8;

  private static final BoardListener NOTHING = (score, multiplier) -> {};

  private Board board = new Board(BOARD_SIZE, BOARD_SIZE);
  private Stone next;

  private StoneResourceService stoneResourceService;

  private Paint paintAlpha = new Paint();

  private BoardListener boardListener = NOTHING;

  private boolean over = false;

  private volatile Set<Point> highlightedPossiblePlaces;
  private volatile Set<Point> possiblePlaces;

  public BoardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    paintAlpha.setColor(getResources().getColor(R.color.black_overlay));

    setDimensions(BOARD_SIZE, BOARD_SIZE);
    setBitmapProvider(new DrawBitmapStrategy() {

      @Override
      public Bitmap getBitmap(int x, int y) {
        Bitmap bitmap;
        Point point = new Point(x, y);
        if (highlightedPossiblePlaces.contains(point)) {
          bitmap = stoneResourceService.getStoneResource(StoneResourceService.STONE_POSSIBLE_MOVE);
        } else if (possiblePlaces.contains(point)) {
          bitmap = stoneResourceService.getStoneResource(StoneResourceService.STONE_POSSIBLE_MOVE_ALTERNATE);
        } else {
          bitmap = stoneResourceService.getStoneResource(board.get(x, y));
        }

        return bitmap;
      }
    });
  }

  public void initialize() {
    resetPlaces();
    fireScoreEvent();
  }

  private void resetPlaces() {
    highlightedPossiblePlaces = Collections.emptySet();
    possiblePlaces = Collections.emptySet();
  }

  private void fireScoreEvent() {
    boardListener.onScoreChanged(board.getScore(), board.getMultiplier());
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (over) {
      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintAlpha);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN) {
      // we don't care if not down
      return false;
    }

    Point point = getTouchedCell(event);
    if (board.place(point.getX(), point.getY(), next)) {
      resetPlaces();
      fireScoreEvent();
    }

    invalidate();

    return true;
  }

  public void newGame() {
    board.newGame();
    over = false;
    resetPlaces();
    fireScoreEvent();
    invalidate();
  }

  public void setNext(Stone next) {
    this.next = next;
    this.highlightedPossiblePlaces = getShownAvaliablePlaces(next);
    invalidate();
  }

  private Set<Point> getShownAvaliablePlaces(Stone stone) {
    if (next != null && board.hasMatching(next)) {
      return board.getAvailablePlaces(stone);
    }

    return Collections.emptySet();
  }

  /**
   * Sets all possible stones available as next (to show on screen).
   * <p>
   * Returns the ones which are actually placeable.
   */
  public Set<Stone> setPossibilities(Set<Stone> nexts) {
    next = null;

    Set<Stone> ret = new HashSet<>(nexts.size());
    possiblePlaces = new HashSet<>();
    for (Stone stone : nexts) {
      boolean placeable = true;
      if (board.hasMatching(stone)) {
        Set<Point> places = board.getAvailablePlaces(stone);
        if (places.isEmpty()) {
          placeable = false;
        } else {
          possiblePlaces.addAll(places);
        }
      }
      if (placeable) {
        ret.add(stone);
      }
    }
    invalidate();

    return ret;
  }

  public void setOver() {
    this.over = true;
    invalidate();
  }

  public void setBoardListener(BoardListener boardListener) {
    this.boardListener = boardListener;
  }

  public void setStoneResourceService(StoneResourceService stoneResourceService) {
    this.stoneResourceService = stoneResourceService;
  }
}
