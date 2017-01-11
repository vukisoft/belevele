package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
import static hu.vuk.belevele.ui.DrawHelpers.drawBitmap;

public class BoardView extends GridView {

  private static final BoardListener NOTHING = (score, multiplier) -> {};

  private Game game;

  private StoneResourceService stoneResourceService;
  private BackgroundTiles backgroundTiles;

  private Paint paintDarkOverlay;

  private BoardListener boardListener = NOTHING;

  @Override
  public void drawCell(int x, int y, Canvas canvas, Rect rect) {
    Stone stone = game.getBoard().get(x, y);
    if (stone == null) {
      backgroundTiles.draw(x, y, canvas, rect);
    } else {
      drawBitmap(
          stoneResourceService.getStoneResource(StoneResourceService.STONE),
          255,
          canvas, rect);
      drawBitmap(
          stoneResourceService.getStoneResource(stone),
          ViewSettings.STONE_TOP_ALPHA,
          canvas, rect);
    }

    Point point = new Point(x, y);
    if (game.isHighlightedPlaceForSelected(point)) {
      drawBitmap(
          stoneResourceService.getStoneResource(StoneResourceService.POSSIBLE_MOVE),
          200,
          canvas, rect);
    } else if (game.isHighlightedPlace(point)) {
      drawBitmap(
          stoneResourceService.getStoneResource(
              StoneResourceService.POSSIBLE_MOVE_ALTERNATE),
          200,
          canvas, rect);
    }
  }

  public BoardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    paintDarkOverlay = new Paint();
    paintDarkOverlay.setColor(ContextCompat.getColor(context, R.color.black_overlay));
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
      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintDarkOverlay);
    }
  }

  private void drawBackground(Canvas canvas) {
    Bitmap boardBackground =
        BitmapFactory.decodeResource(getResources(), R.drawable.board_background);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setAlpha(100);
    canvas.drawBitmap(
        boardBackground,
        new Rect(0, 0, boardBackground.getWidth(), boardBackground.getHeight()),
        new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
        paint);
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

  public void setStoneResourceService(StoneResourceService stoneResourceService) {
    this.stoneResourceService = stoneResourceService;
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

    public void draw(int x, int y, Canvas canvas, Rect rect) {
      Paint paint = new Paint();
      paint.setColor(bgstones[x][y]);
      canvas.drawRect(rect, paint);
    }
  }
}
