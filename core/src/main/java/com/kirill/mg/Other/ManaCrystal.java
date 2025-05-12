package com.kirill.mg.Other;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ManaCrystal {

    private float x, y;
    private float radius = 10;

    public ManaCrystal(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0.5f, 1, 1);

        shapeRenderer.triangle(
            x, y + radius,
            x - radius, y,
            x + radius, y
        );

        shapeRenderer.triangle(
            x, y - radius,
            x - radius, y,
            x + radius, y
        );
        shapeRenderer.end();
    }
}
