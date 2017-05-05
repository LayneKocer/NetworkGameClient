package com.solarbytemedia.mobadungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cnnranderson.tutorial.utils.Constants;

import java.security.SecureRandom;
import java.time.Instant;
import java.math.BigInteger;

public class Player {

	public SpriteBatch batch;
	public Texture img;
	public Body body;

	private SecureRandom random = new SecureRandom();
	public String id;
	
	public Vector2 mPos;
	public Vector2 target;
	public float x;
	public float y;
	public float width;
	public float height;

	public String lastUpdate = "-1000000000-01-01T00:00:00Z";
	
	public Player(SpriteBatch batch, Texture img, Body body ,float x, float y, float width, float height) {
		// TODO Auto-generated constructor stub
		this.batch = batch;
		this.img = img;
		this.body = body;
		this.body.setLinearDamping(20f);
		this.mPos = new Vector2(x, y);
		this.target = new Vector2(x, y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.id = nextSessionId();
		System.out.println(id);
	}
	
	public Player(SpriteBatch batch, Texture img,  Body body, String id,float x, float y, float width, float height){
		this.batch = batch;
		this.img = img;
		this.body = body;
		this.body.setLinearDamping(20f);
		this.mPos = new Vector2(x, y);
		this.target = new Vector2(x, y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.id = id;
		System.out.println(id);
	}
	

	public String nextSessionId() {
		return new BigInteger(64, random).toString(32);
	}
	
	public void update(Vector2 pos){
		this.target = pos;
//		this.body.setTransform(target.x+(450/Constants.PPM/2), target.y+(800/Constants.PPM/2), 0);
		this.mPos = mPos.lerp(target, 0.2f);
		this.x = mPos.x;
		this.y = mPos.y;
	}

	public void update(){
		this.mPos = mPos.lerp(target, 0.2f);
		this.body.setTransform(target.x+(450/Constants.PPM/2), target.y+(800/Constants.PPM/2), 0);
		this.x = mPos.x;
		this.y = mPos.y;
//		body.setTransform(mPos.x+(450/Constants.PPM/2), mPos.y+(800/Constants.PPM/2), 0);
	}

	public void updateBox(){
		target.x = body.getPosition().x-(450/Constants.PPM/2);
		target.y = body.getPosition().y-(800/Constants.PPM/2);
	}
	
	public void render(){
		batch.begin();
		batch.draw(img, x, y, width, height);
		batch.end();
	}
	
}
