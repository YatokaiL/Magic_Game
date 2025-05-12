package com.kirill.mg.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private Texture buttonStartTexture;
    private Texture buttonExitTexture;
    private Rectangle buttonStartBounds;
    private Rectangle buttonExitBounds;

    private Texture backgroundTexture;
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fontq.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        backgroundTexture = new Texture("game_back.png");
        buttonStartTexture = new Texture("button_start.png"); // Замените на путь к вашей текстуре
        buttonExitTexture = new Texture("button_exit.png");   // Замените на путь к вашей текстуре

        buttonStartBounds = new Rectangle(300, 200, 200, 100); // Положение и размер кнопки
        buttonExitBounds = new Rectangle(300, 30, 200, 100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Welcome to the Game!", 200, 400);

        batch.draw(buttonStartTexture, buttonStartBounds.x, buttonStartBounds.y, buttonStartBounds.width, buttonStartBounds.height);
        batch.draw(buttonExitTexture, buttonExitBounds.x, buttonExitBounds.y, buttonExitBounds.width, buttonExitBounds.height);
        batch.end();

        if (Gdx.input.justTouched()) {

            int x = Gdx.input.getX();
            int y = Gdx.input.getY();

            Vector3 touchPos = new Vector3(x, y, 0);
            camera.unproject(touchPos);

            if (touchPos.x >= 300 && touchPos.x <= 500 && touchPos.y >= 200 && touchPos.y <= 260) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }

            if (touchPos.x >= 300 && touchPos.x <= 500 && touchPos.y >= 30 && touchPos.y <= 90) {
                Gdx.app.exit();
            }
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
        buttonStartTexture.dispose();
        buttonExitTexture.dispose();
    }
}
