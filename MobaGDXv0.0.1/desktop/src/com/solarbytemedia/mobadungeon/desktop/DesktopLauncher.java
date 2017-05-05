package com.solarbytemedia.mobadungeon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.solarbytemedia.mobadungeon.MobaDungean;

public class DesktopLauncher {
	public static int WIDTH = 680;
	public static int HEIGHT = 480;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;
		config.title = "Scene Test";
		new LwjglApplication(new MobaDungean(), config);
	}
}
