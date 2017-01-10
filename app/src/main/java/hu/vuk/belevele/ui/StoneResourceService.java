package hu.vuk.belevele.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.FutureTask;

import hu.vuk.belevele.game.stone.Color;
import hu.vuk.belevele.game.stone.Shape;
import hu.vuk.belevele.game.stone.Stone;

import static hu.vuk.belevele.game.stone.Color.BLUE;
import static hu.vuk.belevele.game.stone.Color.GRAY;
import static hu.vuk.belevele.game.stone.Color.GREEN;
import static hu.vuk.belevele.game.stone.Color.MAGENTA;
import static hu.vuk.belevele.game.stone.Color.RED;
import static hu.vuk.belevele.game.stone.Color.YELLOW;
import static hu.vuk.belevele.game.stone.Shape.CROSS;
import static hu.vuk.belevele.game.stone.Shape.DROP;
import static hu.vuk.belevele.game.stone.Shape.SQUARE;
import static hu.vuk.belevele.game.stone.Shape.STAR;

public class StoneResourceService {

  public static final String POSSIBLE_MOVE = "help.png";
  public static final String POSSIBLE_MOVE_ALTERNATE = "alternate_help.png";
  public static final String STONE = "stone.png";

  private static final String BASE_PACKAGE = "stones/";
  private static final String FILE_NAME_FORMAT = "{0}_{1}.png";

  private ConcurrentMap<String, FutureTask<Bitmap>> cache = new ConcurrentHashMap<>();

  private String getStoneName(Stone stone) {
    Preconditions.checkArgument(stone != null);

    return MessageFormat.format(
        FILE_NAME_FORMAT,
        stone.getShape().name().toLowerCase(),
        stone.getColor().name().toLowerCase());
  }

  private String getStonePath(String name) {
    return BASE_PACKAGE + name;
  }

  public Bitmap getStoneResource(String name) {
    String path = getStonePath(name);
    FutureTask<Bitmap> actual = new FutureTask<>(new BitmapCallable(path));
    FutureTask<Bitmap> stored = cache.putIfAbsent(path, actual);
    if (stored == null) {
      stored = actual;
      stored.run();
    }

    try {
      return stored.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Bitmap getStoneResource(Stone stone) {
    return getStoneResource(getStoneName(stone));
  }

  private static class BitmapCallable implements Callable<Bitmap> {

    private String path;

    public BitmapCallable(String path) {
      this.path = path;
    }

    @Override
    public Bitmap call() throws Exception {
      InputStream in = getClass().getResourceAsStream(path);
      if (in == null) {
        System.out.println("Not found: " + path);
        return null;
      }

      try {
        return BitmapFactory.decodeStream(in);
      } finally {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
