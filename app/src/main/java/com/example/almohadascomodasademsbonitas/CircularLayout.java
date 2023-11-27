package com.example.almohadascomodasademsbonitas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircularLayout extends View {

    private static final int NUM_ITEMS = 6;
    private Paint paint = new Paint();
    private float[] cx = new float[NUM_ITEMS];
    private float[] cy = new float[NUM_ITEMS];
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
        // Inicializa el Paint para los círculos
        paint.setColor(Color.BLACK);
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
            cx[i] = centerX + radius * (float) Math.cos(newAngle);
            cy[i] = centerY + radius * (float) Math.sin(newAngle);
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

        // Dibuja los círculos
        for (int i = 0; i < NUM_ITEMS; i++) {
            canvas.drawCircle(cx[i], cy[i], 20, paint);
        }
    }
}


