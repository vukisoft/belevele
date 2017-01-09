package hu.vuk.belevele.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hu.vuk.belevele.R;
import hu.vuk.belevele.game.board.NextStones;
import hu.vuk.belevele.game.board.StoneFactory;
import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

public class NextStoneView extends BitmapGridView {

  private NextStones nextStones;

  private NextStoneListener nextStoneListener = new NextStoneListener() {
    @Override
    public void onSelection(Stone stone) {
    }
  };
  private StoneResourceService stoneResourceService;

  public NextStoneView(Context context, AttributeSet attrs) {
    super(context, attrs);

    setBitmapProvider(new DrawBitmapStrategy() {
      Paint paintSelection;
      Paint paintDisabled;

      {
        paintSelection = new Paint();
        paintSelection.setColor(getResources().getColor(R.color.blue_overlay));

        paintDisabled = new Paint();
        paintDisabled.setColor(getResources().getColor(R.color.black_overlay));
      }

      @Override
      public void draw(int x, int y, Canvas canvas, Rect rect) {
        super.draw(x, y, canvas, rect);
        if (nextStones.isAvailable(x)) {
          canvas.drawRect(rect, paintDisabled);
        } else if (nextStones.getCount() > 1 && nextStones.getSelectedIndex() == x) {
          Rect clipArea = new Rect(rect);
          clipArea.top += 10;
          clipArea.left += 10;
          clipArea.bottom -= 10;
          clipArea.right -= 10;
          canvas.clipRect(clipArea, Op.XOR);
          canvas.drawRect(rect, paintSelection);
        }
      }

      @Override
      public Bitmap getBitmap(int x, int y) {
        return stoneResourceService.getStoneResource(nextStones.get(x));
      }
    });
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    Point point = getTouchedCell(event);
    setSelected(point.getX());
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

  public void setStoneResourceService(StoneResourceService stoneResourceService) {
    this.stoneResourceService = stoneResourceService;
  }
}
