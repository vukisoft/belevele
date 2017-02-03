package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.google.common.collect.ImmutableList;

import java.util.Random;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.Board;
import hu.vuk.belevele.game.board.Game;
import hu.vuk.belevele.game.struct.Point;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BoardView extends GridView {
  protected static final float MARK_INSET_RATIO = 0.72f;
  protected static final float SMALL_MARK_INSET_RATIO = 0.86f;
  private static final BoardListener NOTHING = (score, multiplier) -> {};
  private static final Random RND = new Random();
  private static final ImmutableList<Integer> COLORS = ImmutableList.of(
      Color.argb(255, 255, 0, 0),
      Color.argb(255, 0, 255, 0),
      Color.argb(255, 0, 0, 255),
      Color.argb(255, 255, 255, 0),
      Color.argb(255, 0, 255, 255),
      Color.argb(255, 255, 0, 255));
  protected Game game;
  protected DrawableService drawableService;
  protected BackgroundTiles backgroundTiles;
  protected BoardListener boardListener = NOTHING;

  public BoardView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected void fireScoreEvent() {
    // TODO make game keep the score
    boardListener.onScoreChanged(game.getBoard().getScore(), game.getBoard().getMultiplier());
  }

  protected void drawDropShadowIfNeeded(Canvas canvas) {
    if (game.isOver()) {
      Paint paintDarkOverlay = new Paint();
      paintDarkOverlay.setColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintDarkOverlay);
    }
  }

  protected void drawBackground(Canvas canvas) {
    drawableService.draw(R.drawable.board_background)
        .withAlpha(100)
        .to(canvas, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()));
  }

  protected void drawMark(int x, int y, Canvas canvas, Rect rect) {
    Point point = new Point(x, y);
    if (game.isHighlightedPlace(point)) {
      drawableService.draw(R.drawable.ic_mark)
          .withRectShrinkingByRatio(
              game.isHighlightedPlaceForSelected(point)
                  ? MARK_INSET_RATIO : SMALL_MARK_INSET_RATIO)
          .to(canvas, rect);
    }
  }

  public void setDrawableService(DrawableService drawableService) {
    this.drawableService = drawableService;
  }

  // TODO set board only
  public void setGame(Game game) {
    this.game = checkNotNull(game);

    if (setDimensions(game.getBoard().getWidth(), game.getBoard().getHeight())) {
      backgroundTiles = new BackgroundTiles(game.getBoard());
    }

    invalidate();
    fireScoreEvent();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    drawBackground(canvas);
    super.onDraw(canvas);
    drawDropShadowIfNeeded(canvas);
  }

  public void setBoardListener(BoardListener boardListener) {
    this.boardListener = boardListener;
  }

  protected static class BackgroundTiles {
    private int[][] bgstones;
    BackgroundTiles(Board board) {
      bgstones = new int[board.getWidth()][];
      for (int i = 0; i < bgstones.length; i++) {
        bgstones[i] = new int[board.getHeight()];
        for (int j = 0; j < bgstones[i].length; j++) {
          bgstones[i][j] = createRandomColor();
        }
      }
    }

    private int createRandomColor() {
      int color = COLORS.get(RND.nextInt(COLORS.size()));
      return Color.argb(
          10 + RND.nextInt(20),
          Color.red(color),
          Color.green(color),
          Color.blue(color));
    }

    protected int getColor(int x, int y) {
      return bgstones[x][y];
    }

    protected Paint getPaint(int x, int y) {
      Paint paint = new Paint();
      paint.setColor(getColor(x, y));
      return paint;
    }
  }
}
