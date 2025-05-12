package com.kirill.mg.Bullets;

public class Bullet {
    public float x;
    public float y;
    float direction;
    float speed;

    public Bullet(float x, float y, float direction, float speed) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
    }

    public void update(float delta) {
        x += Math.cos(direction) * speed * delta;
        y += Math.sin(direction) * speed * delta;
    }

    public boolean isOutOfBounds() {
        return x < 0 || x > 1600 || y < 0 || y > 960;
}
}
