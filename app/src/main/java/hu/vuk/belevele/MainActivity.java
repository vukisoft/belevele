package hu.vuk.belevele;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import hu.vuk.belevele.game.board.Board;
import hu.vuk.belevele.game.board.Game;
import hu.vuk.belevele.game.board.NextStones;
import hu.vuk.belevele.ui.BoardListener;
import hu.vuk.belevele.ui.BoardView;
import hu.vuk.belevele.ui.NextStoneView;
import hu.vuk.belevele.ui.RandomStoneFactory;
import hu.vuk.belevele.ui.StoneResourceService;

public class MainActivity extends Activity {

  private static final int NEXT_COUNT = 3;
  private static final int BOARD_SIZE = 8;

  private Game game;

  private final StoneResourceService stoneResourceService = new StoneResourceService();
  private NextStoneView nextView;
  private BoardView boardView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    initialize();
  }

  private void initialize() {
    game = getLastGame();

    nextView = (NextStoneView) findViewById(R.id.nextView);
    nextView.setStoneResourceService(stoneResourceService);

    boardView = (BoardView) findViewById(R.id.boardView);
    boardView.setStoneResourceService(stoneResourceService);

    final TextView scoreText = (TextView) findViewById(R.id.scoreTextView);
    boardView.setBoardListener(new BoardListener() {
      @Override
      public void onScoreChanged(int score, int multiplier) {
        scoreText.setText(getResources().getString(R.string.scoreText, score, multiplier));
        nextView.invalidate();
      }
    });

    nextView.setNextStoneListener(stone -> {
      boardView.invalidate();
      nextView.invalidate();
    });

    setGameToViews();

    Button button = (Button) findViewById(R.id.newGameButton);
    button.setOnClickListener(v -> {
      game = createNewGame();
      setGameToViews();
    });
  }

  private void setGameToViews() {
    nextView.setNextStones(game.getNextStones());
    boardView.setGame(game);
  }

  @Override
  public Object onRetainNonConfigurationInstance() {
    return game;
  }

  private Game getLastGame() {
    Game game = (Game) getLastNonConfigurationInstance();
    if (game == null) {
      game = createNewGame();
    }
    return game;
  }

  private Game createNewGame() {
    return new Game(
        new Board(BOARD_SIZE, BOARD_SIZE),
        new NextStones(NEXT_COUNT, new RandomStoneFactory()));
  }
}
