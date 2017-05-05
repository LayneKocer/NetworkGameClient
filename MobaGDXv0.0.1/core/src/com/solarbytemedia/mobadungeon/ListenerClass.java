package com.solarbytemedia.mobadungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ListenerClass implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		final Fixture fa = contact.getFixtureA();
        final Fixture fb = contact.getFixtureB();
        
		System.out.println("contact begin");

		Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run () {
            	MobaDungean.player.target = fa.getBody().getPosition().sub(fb.getBody().getPosition());
//            	fa.getBody().applyForce(fa.getBody().getPosition().sub(fb.getBody().getPosition()).scl(200), fb.getBody().getPosition(), true);
            }
		});
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
//		System.out.println("contact end");		
////		contact.getFixtureA().getBody().applyLinearImpulse(contact.getFixtureA().getBody().getPosition().sub(contact.getFixtureB().getBody().getPosition()).scl(100, 100) , contact.getFixtureB().getBody().getPosition(), true);
//		final Fixture fa = contact.getFixtureA();
//        final Fixture fb = contact.getFixtureB();
//        
////		System.out.println("contact begin");
//
//		Gdx.app.postRunnable(new Runnable() {
//            @Override
//            public void run () {
//            	fa.getBody().applyForce(fa.getBody().getPosition().sub(fb.getBody().getPosition()).scl(200), fb.getBody().getPosition(), true);
//            }
//		});
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	
}
