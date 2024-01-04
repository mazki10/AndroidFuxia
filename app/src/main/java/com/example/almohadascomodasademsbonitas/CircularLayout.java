package com.example.almohadascomodasademsbonitas;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CircularLayout extends View {

    private static final int NUM_ITEMS = 5;
    private float[] cx = new float[NUM_ITEMS];
    private float[] cy = new float[NUM_ITEMS];
    private float[] currentCx = new float[NUM_ITEMS];
    private float[] currentCy = new float[NUM_ITEMS];
    private float radius;
    private float centerX;
    private float centerY;
    private List<ImageButton> imageButtonList = new ArrayList<>();

    private boolean isTouching = false;

    private int[] imageResources = new int[]{
            R.drawable.agenda,
            R.drawable.enviar,
            R.drawable.pedidos,
            R.drawable.partners,
            R.drawable.informacion
    };

    public CircularLayout(Context context) {
        super(context);
        init();
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Configura eventos táctiles
        setOnTouchListener((v, event) -> {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
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
        });
    }

    private void rotateCircles(float angle) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            double newAngle = angle + (i * (2 * Math.PI) / NUM_ITEMS);
            currentCx[i] = centerX + radius * (float) Math.cos(newAngle);
            currentCy[i] = centerY + radius * (float) Math.sin(newAngle);

            cx[i] = currentCx[i];
            cy[i] = currentCy[i];
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 3f;

        for (int i = 0; i < NUM_ITEMS; i++) {
            double angle = (i * (2 * Math.PI) / NUM_ITEMS);
            cx[i] = centerX + radius * (float) Math.cos(angle);
            cy[i] = centerY + radius * (float) Math.sin(angle);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (imageButtonList.isEmpty()) {
            createImageButtons();
        } else {
            updateImageButtonsPositions();
        }
    }

    private void createImageButtons() {
        RelativeLayout.LayoutParams layoutParams;
        int buttonSize = 250; // Nuevo tamaño deseado para los botones de imagen

        for (int i = 0; i < NUM_ITEMS; i++) {
            ImageButton imageButton = new ImageButton(getContext());
            // Establece las imágenes para cada botón de imagen
            imageButton.setImageResource(imageResources[i]);

            imageButton.setBackgroundResource(R.drawable.button_shape);

            // Escala la imagen dentro del botón
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

            layoutParams = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
            layoutParams.leftMargin = (int) (cx[i] - buttonSize / 2);
            layoutParams.topMargin = (int) (cy[i] - buttonSize / 2);
            imageButton.setLayoutParams(layoutParams);

            // Asegúrate de tener un ID único para cada botón si es necesario
            imageButton.setId(i + 1);

            imageButtonList.add(imageButton); // Agregar a la lista
            ((RelativeLayout) getParent()).addView(imageButton);
        }
    }

    private void updateImageButtonsPositions() {
        RelativeLayout.LayoutParams layoutParams;
        int buttonSize = 250; // Nuevo tamaño deseado para los botones de imagen

        for (int i = 0; i < NUM_ITEMS; i++) {
            ImageButton imageButton = imageButtonList.get(i);

            layoutParams = (RelativeLayout.LayoutParams) imageButton.getLayoutParams();
            layoutParams.leftMargin = (int) (cx[i] - buttonSize / 2);
            layoutParams.topMargin = (int) (cy[i] - buttonSize / 2);

            imageButton.setLayoutParams(layoutParams);
        }
    }

}
