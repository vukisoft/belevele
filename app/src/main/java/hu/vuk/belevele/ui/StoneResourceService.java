package hu.vuk.belevele.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

  public static final String STONE_EMPTY = "empty.jpg";
  public static final String STONE_POSSIBLE_MOVE = "help.jpg";
  public static final String STONE_POSSIBLE_MOVE_ALTERNATE = "help2.jpg";

  private static final String BASE_PACKAGE = "stones/";
  private static final String FILE_NAME_FORMAT = "st{0}{1}.jpg";

  private static final Map<Color, Object> COLOR_NAMES = new HashMap<>();
  private static final Map<Shape, Object> SHAPE_NAMES = new HashMap<>();

  static {
    COLOR_NAMES.put(RED, 1);
    COLOR_NAMES.put(BLUE, 2);
    COLOR_NAMES.put(GREEN, 3);
    COLOR_NAMES.put(MAGENTA, 4);
    COLOR_NAMES.put(GRAY, 5);
    COLOR_NAMES.put(YELLOW, 6);

    SHAPE_NAMES.put(SQUARE, 1);
    SHAPE_NAMES.put(DROP, 2);
    SHAPE_NAMES.put(CROSS, 3);
    SHAPE_NAMES.put(STAR, 4);
  }

  private ConcurrentMap<String, FutureTask<Bitmap>> cache = new ConcurrentHashMap<>();

  private String getStoneName(Stone stone) {
    if (stone == null) {
      return STONE_EMPTY;
    }

    return MessageFormat.format(FILE_NAME_FORMAT, COLOR_NAMES.get(stone.getColor()), SHAPE_NAMES.get(stone.getShape()));
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
