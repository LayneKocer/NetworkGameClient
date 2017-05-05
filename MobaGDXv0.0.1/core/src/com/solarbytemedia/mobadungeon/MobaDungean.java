package com.solarbytemedia.mobadungeon;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cnnranderson.tutorial.utils.b2d.BodyBuilder;
import com.cnnranderson.tutorial.utils.CameraStyles;
import com.cnnranderson.tutorial.utils.Constants;
import com.cnnranderson.tutorial.utils.TiledObjectUtil;

import box2dLight.PointLight;
import box2dLight.RayHandler;


public class MobaDungean extends ApplicationAdapter {

    private WebSocketClient mWebSocketClient;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public OrthographicCamera cam;
	public SpriteBatch batch;
	public Texture img;
	
	public static Player player;
	public List<Player> players;
	
	public OrthogonalTiledMapRenderer tmr;
	public TiledMap map;
	
	public World world;
	public Body body;
	public BodyDef bodyDef;
	public Box2DDebugRenderer debugRender;
	public ListenerClass listenerClass;
	
	
	public RayHandler rayHandler;

	public PointLight light;
	
	public float speed = 3500;
	public boolean submit = false;
	
	
	public MobaDungean(){
		System.out.println("Constructor");
	}
	
	@Override
	public void create () {
		players = new ArrayList<Player>();
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		img = new Texture("resources/graphic/badlogic.jpg");
		
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 50);
		cam.zoom = 0.2f;
		cam.update();

		world = new World(new Vector2(0, 0), true);
		//listenerClass = new ListenerClass();
		//world.setContactListener(listenerClass);
		
		debugRender = new Box2DDebugRenderer();
		
		body = BodyBuilder.createBox(world, 100*Constants.PPM, 100*Constants.PPM, 450, 800, false, true);
		body.setLinearDamping(20f);

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(Color.WHITE);
		rayHandler.setAmbientLight(0.1f);
		
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		
		light = new PointLight(rayHandler, 100, Color.WHITE, 5*Constants.PPM, 0, 0);
		light.setColor(1f,0.8f,0.6f,0.8f);
		light.attachToBody(body);
		map = new TmxMapLoader().load("resources/graphic/TestLevel0.tmx");
		
		tmr = new OrthogonalTiledMapRenderer(map);
		
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("Object Layer 1").getObjects());
		
		player = new Player(batch, img, body, body.getPosition().x-(450/Constants.PPM/2), body.getPosition().y-(800/Constants.PPM/2), 450/Constants.PPM, 800/Constants.PPM);
		
		players.add(player);
		connectWebSocket();
		
		submit = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				SendUpdate();
			}
		}, 50, 1000);
		
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				SendPing();
//			}
//		}, 0, 1000);
		
	}


	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined);
		tmr.setView(cam);
		
		player.updateBox();
		for(Player pl : players){
			pl.update();
		}

		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		CameraStyles.lerpToTarget(cam, body.getPosition());

		world.step(1/60f, 6, 2);
//		rayHandler.update();
		
		handleInput();
		cam.update();
//		rayHandler.setCombinedMatrix(cam);

		tmr.render();

		for(Player pl : players){
			pl.render();
		}

//		rayHandler.render();
//		debugRender.render(world, cam.combined);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
	}
	
	@Override
	public void dispose () {
		rayHandler.dispose();
        debugRender.dispose();
        world.dispose();
        batch.dispose();
        map.dispose();
        tmr.dispose();
        CloseConnection();
	}
	
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.I)) {
			cam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.applyLinearImpulse(new Vector2(-speed,0) , body.getPosition(), true);
			submit = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.applyLinearImpulse(new Vector2(speed,0) , body.getPosition(), true);
			submit = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			body.applyLinearImpulse(new Vector2(0,-speed) , body.getPosition(), true);
			submit = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			body.applyLinearImpulse(new Vector2(0,speed) , body.getPosition(), true);
			submit = true;
		}

		cam.zoom = MathUtils.clamp(cam.zoom, 0.05f, 1000/cam.viewportWidth);

	}
	
    public void OpenConnection(){
    	if(mWebSocketClient != null){
    		if(mWebSocketClient.getConnection().isClosed()){
    			mWebSocketClient = null;
    			connectWebSocket();
    		}
    	}
    }
    
    public void CloseConnection(){
    	if(mWebSocketClient != null){
    		if(!mWebSocketClient.getConnection().isClosed()){
    			mWebSocketClient.close();
    		}
    	}
    }
    
    public void SendMessage(String messageText) {
    	System.out.println("Sending message :: " + "Start: " + messageText);

        if(mWebSocketClient.getConnection().isOpen()/* && submit*/){
            mWebSocketClient.send(messageText);
            System.out.println("Sending message :: " + "Sending: " + messageText);
        }
        System.out.println("Sending message :: " + "Finish");
    }
    
    public void SendUpdate(){
    	if(mWebSocketClient.getConnection().isOpen()){
    		String message = PacketUtil.CreateUpdate(player);
    		if(message != ""){
    			System.out.println("Sending:: " + message);
                mWebSocketClient.send(message);
                submit = false;
    		} else {
    			System.out.println("Empty String");
    		}
        }
    	OpenConnection();
    }

    private void connectWebSocket() {
        URI uri;
        try {
        	// Url Of Game Server
            uri = new URI("ws://ab506392.ngrok.io/GameServer/GameServer");
//            uri = new URI("ws://192.168.1.9:8080/GameServer/GameServer");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        // Map<String, String> headers = new HashMap<>();
        // mWebSocketClient = new WebSocketClient(uri, new Draft_17(), headers, 0) {

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("Websocket Open");
                // String message = PacketUtil.CreateUpdate(player);
                // mWebSocketClient.send(message);
            }

            @Override
            public void onMessage(String packet) {
                System.out.println("Recieved Packet: "+packet);

                List<String> ids = PacketUtil.GetID(packet);
                List<Vector2> pos = PacketUtil.GetPos(packet);
                Instant time = PacketUtil.GetTime(packet);
                int i = 0;
                for(String id : ids){
                    boolean idInPlayers = false;
                    for(Player pl : players){
                    	if(pl.id.equals(id)){
                    		System.out.println("Player "+id+" Already here");
                    		idInPlayers = true;
                    	}
                    }
                    if(!idInPlayers){
                		System.out.println("Player "+id+" not here creating player");
                    	players.add(new Player(batch, img, 
                    			BodyBuilder.createBox(world, pos.get(i).x, pos.get(i).y, 450, 800, false, true), 
                    			id, pos.get(i).x, pos.get(i).y, player.width, player.height));
                    }
                    for(Player pl : players){
                    	if(pl.id.equals(id) && !pl.id.equals(player.id)){
                    		System.out.println("Updating Player " + id + " posx " + pos.get(i).x + " posy " + pos.get(i).y);
                    		pl.update(pos.get(i));
                    		pl.lastUpdate = time.toString();
                    	} else {
                    		player.lastUpdate = time.toString();
                    	}
                    }
                    i++;
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("Websocket Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Websocket Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
	
}