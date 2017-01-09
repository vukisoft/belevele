package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.Game;
import hu.vuk.belevele.game.struct.Point;

public class BoardView extends BitmapGridView {

  private static final BoardListener NOTHING = (score, multiplier) -> {};

  private Game game;

  private StoneResourceService stoneResourceService;

  private Paint paintAlpha = new Paint();

  private BoardListener boardListener = NOTHING;

  public BoardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    paintAlpha.setColor(getResources().getColor(R.color.black_overlay));
  }

  private void fireScoreEvent() {
    // TODO make game keep the score
    boardListener.onScoreChanged(game.getBoard().getScore(), game.getBoard().getMultiplier());
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (game.isOver()) {
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
    this.game = game;

    if (game != null) {
      setDimensions(game.getBoard().getWidth(), game.getBoard().getHeight());
      setBitmapProvider(new DrawBitmapStrategy() {
        @Override
        public Bitmap getBitmap(int x, int y) {
          Bitmap bitmap;
          Point point = new Point(x, y);
          if (game.isHighlightedPlaceForSelected(point)) {
            bitmap = stoneResourceService.getStoneResource(StoneResourceService.STONE_POSSIBLE_MOVE);
          } else if (game.isHighlightedPlace(point)) {
            bitmap = stoneResourceService.getStoneResource(StoneResourceService.STONE_POSSIBLE_MOVE_ALTERNATE);
          } else {
            bitmap = stoneResourceService.getStoneResource(game.getBoard().get(x, y));
          }

          return bitmap;
        }
      });
    } else {
      setBitmapProvider(null);
    }

    invalidate();
    fireScoreEvent();
  }
}
