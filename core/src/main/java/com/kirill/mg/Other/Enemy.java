package com.kirill.mg.Other;

import com.badlogic.gdx.graphics.Texture;

public class Enemy {
    public float x, y;
    private int health;
    private float speed;
    private int type;
    private Texture texture;

    public Enemy(Texture texture, float x, float y, int health, float speed, int type) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
        this.type = type;
        this.texture = texture;
    }

    public void update(float playerX, float playerY, float delta) {
        float dx = playerX - x;
        float dy = playerY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            x += (dx / distance) * speed * delta;
            y += (dy / distance) * speed * delta;
        }
    }

    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            if (health <= 0) {
                boolean alive = false;
            }
        }
    }

    public Texture getTexture() {return  this.texture;}

    public boolean isAlive() {
        return health > 0;
    }

    public int getType() {
        return type;
    }
}
