package com.kirill.mg;

import com.badlogic.gdx.Game;
import com.kirill.mg.Screens.MainMenuScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        this.setScreen(new MainMenuScreen());
    }
}
