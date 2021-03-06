package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.NextStones;

import static hu.vuk.belevele.ui.UiUtils.shrinkByRatio;

public class NextStoneView extends GridView {

  private static final float SELECTION_INSET_RATIO = 0.2f;
  private NextStones nextStones;

  private NextStoneListener nextStoneListener = (stone) -> {};

  private DrawableService drawableService;

  private final Paint paintSelection;
  private final Paint paintDisabled;

  public NextStoneView(Context context, AttributeSet attrs) {
    super(context, attrs);
    paintSelection = new Paint();
    paintSelection.setColor(ContextCompat.getColor(context, R.color.next_stone_selection));

    paintDisabled = new Paint();
    paintDisabled.setColor(ContextCompat.getColor(context, R.color.black_overlay));
  }

  @Override
  public void drawCell(int x, int y, Canvas canvas, Rect rect) {
    if (nextStones == null) {
      return;
    }

    boolean hasSelectingFeature = nextStones.getCount() > 1;

    if (hasSelectingFeature && nextStones.getSelectedIndex() == x) {
      canvas.drawRect(rect, paintSelection);
    }

    if (hasSelectingFeature) {
      shrinkByRatio(rect, SELECTION_INSET_RATIO);
    }

    drawableService.drawStone(nextStones.get(x), canvas, rect);
    if (!nextStones.isAvailable(x)) {
      canvas.drawRect(rect, paintDisabled);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    setSelected(getTouchedX(event.getX()));
    invalidate();
    return true;
  }

  public void setNextStones(NextStones nextStones) {
    this.nextStones = nextStones;
    setDimensions(nextStones.getCount(), 1);
    invalidate();
  }

  private void fireSelectionEvent() {
    nextStoneListener.onSelection(nextStones.getSelected());
  }

  private void setSelected(int selected) {
    if (nextStones.setSelected(selected)) {
      fireSelectionEvent();
    }
  }

  public void setNextStoneListener(NextStoneListener nextStoneListener) {
    this.nextStoneListener = nextStoneListener;
  }

  public void setDrawableService(DrawableService drawableService) {
    this.drawableService = drawableService;
  }
}
