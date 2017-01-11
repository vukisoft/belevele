package hu.vuk.belevele.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class DrawHelpers {
  static  void drawBitmap(Bitmap bitmap, int alpha, Canvas canvas, Rect rect) {
    if (bitmap != null) {
      Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      paint.setAlpha(alpha);
      canvas.drawBitmap(bitmap, src, rect, paint);
    }
  }
}
