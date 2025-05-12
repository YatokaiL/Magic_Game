package com.kirill.mg.MapThings;

public class BackgroundChunk {
    public float x, y;
    public float width, height;

    public BackgroundChunk(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 800;
        this.height = 480;
    }

    public void update(float playerX, float playerY, float chunkWidth) {

        if (x + width < playerX - chunkWidth) {
            x += width;
        }
        if (x > playerX + chunkWidth) {
            x -= width;
        }
    }
}
