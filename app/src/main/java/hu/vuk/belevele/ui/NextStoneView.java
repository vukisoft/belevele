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
import hu.vuk.belevele.game.board.StoneFactory;
import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Point;

public class NextStoneView extends BitmapGridView {

  private StoneFactory stoneFactory;
  private StoneResourceService stoneResourceService;

  private NextStoneListener nextStoneListener = new NextStoneListener() {
    @Override
    public void onSelection(Stone stone) {
    }
  };

  private List<Stone> stones = Collections.emptyList();
  private int count;

  private int selected = -1;
  private Set<Stone> availables = Collections.emptySet();

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
        if (!availables.contains(stones.get(x))) {
          canvas.drawRect(rect, paintDisabled);
        } else if (count > 1 && selected == x) {
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
        return stoneResourceService.getStoneResource(stones.get(x));
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

  public void setStonesCount(int count) {
    this.count = count;
    setDimensions(count, 1);
  }

  public void initialize() {
    reset();
  }

  public void reset() {
    stones = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      stones.add(stoneFactory.create());
    }

    availables = new HashSet<Stone>(stones);
    invalidate();
  }

  private void fireSelectionEvent() {
    nextStoneListener.onSelection(getSelected());
  }

  public Set<Stone> getStones() {
    return new HashSet<>(stones);
  }

  public Stone getSelected() {
    return selected >= 0 ? stones.get(selected) : null;
  }

  public void setAvailabe(Set<Stone> availables) {
    this.availables = availables;
    if (selected == -1 || !availables.contains(getSelected())) {
      int index = 0;
      for (Stone stone : stones) {
        if (availables.contains(stone)) {
          setSelected(index);
          break;
        }
        index++;
      }
      if (index == count) {
        // there is no possible value
        selected = -1;
      }
    }
    invalidate();
  }

  private void setSelected(int selected) {
    if (!availables.contains(stones.get(selected))) {
      return;
    }

    this.selected = selected;
    fireSelectionEvent();
  }

  public void nextStone() {
    if (selected == -1) {
      return;
    }

    stones.set(selected, stoneFactory.create());
  }

  public void setStoneFactory(StoneFactory stoneFactory) {
    this.stoneFactory = stoneFactory;
  }

  public void setStoneResourceService(StoneResourceService stoneResourceService) {
    this.stoneResourceService = stoneResourceService;
  }

  public void setNextStoneListener(NextStoneListener nextStoneListener) {
    this.nextStoneListener = nextStoneListener;
  }
}
