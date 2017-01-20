package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.common.collect.ImmutableList;

import java.util.Random;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.Board;
import hu.vuk.belevele.game.board.Game;
import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

import static com.google.common.base.Preconditions.checkNotNull;
import static hu.vuk.belevele.ui.UiUtils.shrinkByRatio;

public class BoardView extends GridView {

  private static final BoardListener NOTHING = (score, multiplier) -> {};
  private static final float MARK_INSET_RATIO = 0.72f;
  private static final float SMALL_MARK_INSET_RATIO = 0.86f;

  private Game game;

  private DrawableService drawableService;
  private BackgroundTiles backgroundTiles;

  private BoardListener boardListener = NOTHING;

  public BoardView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void drawCell(int x, int y, Canvas canvas, Rect rect) {
    Stone stone = game.getBoard().get(x, y);
    if (stone == null) {
      backgroundTiles.draw(x, y, canvas, rect);
    } else {
      drawableService.drawStone(stone, canvas, rect);
    }

    Point point = new Point(x, y);
    Drawable markDrawable = drawableService.newDrawableLoader(R.drawable.ic_mark)
        .withColor(Color.CYAN)
        .load();
    if (game.isHighlightedPlaceForSelected(point)) {
      shrinkByRatio(rect, MARK_INSET_RATIO);
      drawableService.drawDrawable(markDrawable, canvas, rect);
    } else if (game.isHighlightedPlace(point)) {
      shrinkByRatio(rect, SMALL_MARK_INSET_RATIO);
      drawableService.drawDrawable(markDrawable, canvas, rect);
    }
  }

  private void fireScoreEvent() {
    // TODO make game keep the score
    boardListener.onScoreChanged(game.getBoard().getScore(), game.getBoard().getMultiplier());
  }

  @Override
  protected void onDraw(Canvas canvas) {
    drawBackground(canvas);
    super.onDraw(canvas);
    drawDropShadowIfNeeded(canvas);
  }

  private void drawDropShadowIfNeeded(Canvas canvas) {
    if (game.isOver()) {
      Paint paintDarkOverlay = new Paint();
      paintDarkOverlay.setColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintDarkOverlay);
    }
  }

  private void drawBackground(Canvas canvas) {
    drawableService.drawDrawable(
        drawableService.newDrawableLoader(
            R.drawable.board_background).withAlpha(100).load(),
        canvas,
        new Rect(0, 0, canvas.getWidth(), canvas.getHeight()));
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_DOWN) {
      // we don't care if not down
      return false;
    }

    Point point = getTouchedCell(event);
    if (game.placeNext(point.getX(), point.getY())) {
      fireScoreEvent();
      invalidate();
    }

    return true;
  }

  public void setBoardListener(BoardListener boardListener) {
    this.boardListener = boardListener;
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

  private static final Random RND = new Random();

  private static final ImmutableList<Integer> COLORS = ImmutableList.of(
      Color.argb(255, 255, 0, 0),
      Color.argb(255, 0, 255, 0),
      Color.argb(255, 0, 0, 255),
      Color.argb(255, 255, 255, 0),
      Color.argb(255, 0, 255, 255),
      Color.argb(255, 255, 0, 255));
  private static class BackgroundTiles {
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

    void draw(int x, int y, Canvas canvas, Rect rect) {
      Paint paint = new Paint();
      paint.setColor(bgstones[x][y]);
      canvas.drawRect(rect, paint);
    }
  }
}
