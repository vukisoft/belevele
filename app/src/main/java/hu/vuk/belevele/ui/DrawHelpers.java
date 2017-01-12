package hu.vuk.belevele.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class DrawHelpers {
  static  void drawBitmap(Drawable bitmap, int alpha, Canvas canvas, Rect rect) {
    if (bitmap != null) {
//      Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//      paint.setAlpha(alpha);
      bitmap.setBounds(rect);
      bitmap.setAlpha(alpha);
      bitmap.draw(canvas);
    }
  }
}
