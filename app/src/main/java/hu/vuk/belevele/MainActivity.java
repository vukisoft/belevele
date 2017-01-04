package hu.vuk.belevele;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.ui.BoardListener;
import hu.vuk.belevele.ui.BoardView;
import hu.vuk.belevele.ui.NextStoneListener;
import hu.vuk.belevele.ui.NextStoneView;
import hu.vuk.belevele.ui.RandomStoneFactory;
import hu.vuk.belevele.ui.StoneResourceService;

public class MainActivity extends Activity {

  private static final int NEXT_COUNT = 3;

  private final StoneResourceService stoneResourceService = new StoneResourceService();
  private NextStoneView nextView;
  private BoardView board;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    initialize();
  }

  private void initialize() {
    nextView = (NextStoneView) findViewById(R.id.nextView);
    nextView.setStonesCount(NEXT_COUNT);
    nextView.setStoneResourceService(stoneResourceService);
    nextView.setStoneFactory(new RandomStoneFactory());
    nextView.initialize();

    board = (BoardView) findViewById(R.id.boardView);
    board.setStoneResourceService(stoneResourceService);
    board.initialize();

    final TextView scoreText = (TextView) findViewById(R.id.scoreTextView);
    board.setBoardListener(new BoardListener() {

      @Override
      public void onScoreChanged(int score, int multiplier) {
        scoreText.setText(getResources().getString(R.string.scoreText, score, multiplier));
        nextStone();
      }
    });
    nextView.setNextStoneListener(stone -> board.setNext(stone));

    Button button = (Button) findViewById(R.id.newGameButton);
    button.setOnClickListener(v -> {
        nextView.reset();
        board.newGame();
      });

    updateNextStones();
  }

  private void nextStone() {
    nextView.nextStone();
    updateNextStones();
  }

  void updateNextStones() {
    Set<Stone> usableStones = board.setPossibilities(nextView.getStones());
    nextView.setAvailabe(usableStones);

    if (usableStones.isEmpty()) {
      // it's over, mate
      board.setOver();
    } else {
      board.setNext(nextView.getSelected());
    }
  }
}
