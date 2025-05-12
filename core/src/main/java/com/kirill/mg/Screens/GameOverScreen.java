package com.kirill.mg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements com.badlogic.gdx.Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private int score;

    public GameOverScreen(int score) {
        this.score = score;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Очищаем экран
        batch.begin();

        // Текст "Game Over"

        font.draw(batch, "GAME OVER", 500, 500);

        font.draw(batch, "Press ENTER to Restart", 300, 250);  // Кнопка перезапуска
        font.draw(batch, "Press ESC to Exit", 300, 200);   // Кнопка выхода
        batch.end();

        // Обработка нажатий
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Перезапуск игры
            ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // Выход из игры
            Gdx.app.exit();
        }

    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
