package hu.vuk.belevele;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import hu.vuk.belevele.game.board.Board;
import hu.vuk.belevele.game.board.Game;
import hu.vuk.belevele.game.board.NextStones;
import hu.vuk.belevele.game.stone.Stone;
import hu.vuk.belevele.game.struct.Matrix6v;
import hu.vuk.belevele.ui.Board6vView;
import hu.vuk.belevele.ui.BoardView;
import hu.vuk.belevele.ui.DrawableService;
import hu.vuk.belevele.ui.GradientProgressView;
import hu.vuk.belevele.ui.NextStoneView;
import hu.vuk.belevele.ui.RandomStoneFactory;

public class MainActivity extends Activity {

  private static final int NEXT_COUNT = 3;
  private static final int BOARD_SIZE = 8;

  private Game game;

  private DrawableService drawableService;
  private NextStoneView nextView;
  private BoardView boardView;
  private GradientProgressView levelView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    initialize();
  }

  private void initialize() {
    drawableService = new DrawableService(getApplicationContext());
    game = getLastOrNewGame();

    nextView = (NextStoneView) findViewById(R.id.nextView);
    nextView.setDrawableService(drawableService);

    levelView = (GradientProgressView) findViewById(R.id.levelProgressView);
    levelView.setColorRange(
        R.color.level_progress_color1,
        R.color.level_progress_color2,
        R.color.level_progress_color3);

    boardView = (Board6vView) findViewById(R.id.boardView);
    boardView.setDrawableService(drawableService);

    final TextView scoreText = (TextView) findViewById(R.id.scoreTextView);
    boardView.setBoardListener(
        (score, multiplier) -> {
          scoreText.setText(getResources().getString(R.string.scoreText, score, multiplier));
          nextView.invalidate();
          levelView.setValue(game.getBoard().getMultiplier());
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
    levelView.setRange(0, 15); // TODO calculate value
    levelView.setValue(game.getBoard().getMultiplier());
  }

  @Override
  public Object onRetainNonConfigurationInstance() {
    return game;
  }

  private Game getLastOrNewGame() {
    Game game = (Game) getLastNonConfigurationInstance();
    if (game == null) {
      game = createNewGame();
    }
    return game;
  }

  private Game createNewGame() {
    return new Game(
        new Board(new Matrix6v<>(BOARD_SIZE, BOARD_SIZE + 1, Stone.class)),
        new NextStones(NEXT_COUNT, new RandomStoneFactory()));
  }
}
