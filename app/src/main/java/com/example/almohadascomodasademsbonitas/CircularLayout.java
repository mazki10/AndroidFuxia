package com.example.almohadascomodasademsbonitas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircularLayout extends View {

    private static final int NUM_ITEMS = 5;
    private Paint paint = new Paint();
    private float[] cx = new float[NUM_ITEMS];
    private float[] cy = new float[NUM_ITEMS];
    private float[] currentCx = new float[NUM_ITEMS];
    private float[] currentCy = new float[NUM_ITEMS];
    private float radius;
    private float centerX;
    private float centerY;

    private float prevX;
    private float prevY;
    private boolean isTouching = false;

    public CircularLayout(Context context) {
        super(context);
        init();
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // Configura eventos táctiles
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        prevX = x;
                        prevY = y;
                        isTouching = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isTouching) {
                            float dx = x - centerX;
                            float dy = y - centerY;

                            float angle = (float) Math.atan2(dy, dx);

                            rotateCircles(angle);

                            invalidate(); // Vuelve a dibujar la vista para mostrar los cambios
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isTouching = false;
                        break;
                }
                return true;
            }
        });
    }

    private void rotateCircles(float angle) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            double newAngle = angle + (i * (2 * Math.PI) / NUM_ITEMS);
            currentCx[i] = centerX + radius * (float) Math.cos(newAngle);
            currentCy[i] = centerY + radius * (float) Math.sin(newAngle);

            // Actualiza las posiciones de las imágenes para su dibujo
            cx[i] = currentCx[i];
            cy[i] = currentCy[i];
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Calcula el centro y el radio del círculo
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 3f;

        // Calcula las posiciones iniciales de los círculos
        for (int i = 0; i < NUM_ITEMS; i++) {
            double angle = (i * (2 * Math.PI) / NUM_ITEMS);
            cx[i] = centerX + radius * (float) Math.cos(angle);
            cy[i] = centerY + radius * (float) Math.sin(angle);
        }
    }

     @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       Drawable[] images = new Drawable[NUM_ITEMS];
        images[0] = getResources().getDrawable(R.drawable.agenda);
        images[1] = getResources().getDrawable(R.drawable.enviar);
        images[2] = getResources().getDrawable(R.drawable.pedidos);
        images[3] = getResources().getDrawable(R.drawable.informacion);
        images[4] = getResources().getDrawable(R.drawable.partners);

        for (int i = 0; i < NUM_ITEMS; i++) {
            if (images[i] != null) {
                Bitmap bitmap = ((BitmapDrawable) images[i]).getBitmap();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                // Escalar la imagen al tamaño deseado (puedes ajustar estos valores)
                int scaledWidth = 250; // Ancho escalado deseado
                int scaledHeight = 250; // Altura escalada deseada

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
                images[i] = new BitmapDrawable(getResources(), scaledBitmap);

                // Dibujar la imagen escalada en el lienzo
                images[i].setBounds((int) (cx[i] - scaledWidth / 2), (int) (cy[i] - scaledHeight / 2),
                        (int) (cx[i] + scaledWidth / 2), (int) (cy[i] + scaledHeight / 2));
                images[i].draw(canvas);
            }
        }
    }
}


