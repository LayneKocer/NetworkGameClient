package com.solarbytemedia.mobadungeon;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.math.Vector2;

public class PacketUtil {

	public static final String jsonTemplate = "{ \"user\": \"empty\",\"posx\": 100,\"posy\": 100}";
	
	// JSON fields
	public static final String msg = "msg";
	public static final String user = "user";
	public static final String posx = "posx";
	public static final String posy = "posy";
	public static final String updateTime = "updateTime";
	public static final String input = "input";
	
	public static String CreateUpdate(Player pl){
		String message = "{\"type\":\"connect\",\"msg\": {\"user\": \""+pl.id+"\",\"posx\":"+pl.target.x+",\"posy\":"+pl.target.y+",\"lastUpdate\": \""+pl.lastUpdate+"\"}}";
		return message;
	}

	public static List<String> GetID(String JPacket){
		JSONParser parser = new JSONParser();
		JSONObject json;
		JSONArray players;
		try {
			json = (JSONObject) parser.parse(JPacket);
			players = (JSONArray) json.get(msg);
			List<String> ids = new ArrayList<String>();
			Iterator<JSONObject> iterator = players.iterator();
			while(iterator.hasNext()){
				json = iterator.next();
				ids.add(json.get(user).toString());
			}
			return ids;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Get ID error");
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Vector2> GetPos(String JPacket){
		JSONParser parser = new JSONParser();
		JSONObject json;
		JSONArray players;
		try {
			json = (JSONObject) parser.parse(JPacket);
			players = (JSONArray) json.get(msg);
			List<Vector2> pos = new ArrayList<Vector2>();
			Iterator<JSONObject> iterator = players.iterator();
			while(iterator.hasNext()){
				json = iterator.next();
				pos.add(new Vector2(Float.parseFloat(json.get(posx).toString()),Float.parseFloat(json.get(posy).toString())));
			}
			return pos;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Get ID error");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Instant GetTime(String JPacket){
		JSONParser parser = new JSONParser();
		JSONObject json;
		JSONArray players;
		try {
			json = (JSONObject) parser.parse(JPacket);
			
			Instant time = Instant.parse(json.get(updateTime).toString());

			return time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Get Time error");
			e.printStackTrace();
		}
		return null;
	}
		
}
