package pasaud.voip.integrationapi;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pasaud.voip.player.*;


public class GetPlayerInfo {
	
	private String targetURL;
	
	public GetPlayerInfo(String targetURL) {
		this.targetURL = targetURL;
	}
	
	public void serach () {
	
		try {
		URL url = new URL(targetURL);
		String json = IOUtils.toString(url, Charset.forName("UTF-8"));
		//JSONObject jsn = new JSONObject(json);
		JSONArray jsonArray = new JSONArray(json);
		
		if(! jsonArray.isEmpty()) {
			try {
	            for (Object obj : jsonArray) {
	                JSONObject jsonObject = (JSONObject) obj;
	                
	                String nome = jsonObject.getString("name");
	                int id = jsonObject.getInt("id");
	                int secret_id = jsonObject.getInt("s_id");
	                String key = jsonObject.getString("key");
	                Boolean dc = jsonObject.getBoolean("dc");
	                Boolean type = jsonObject.getBoolean("player_type");
	                Player player = new PlayerNormal();
	                player.setID(id);
	                player.setScretID(secret_id);
	                player.setPublicId(nome);
	                if(!key.equals("")) {
	                	player.initCrypto(key.getBytes());
	                }
	                if(!dc) {
	                	PlayersManager.addPreConnect(player);
	                } else {
	                	if(PlayersManager.containsPlayerConnected(id)) {
	                		PlayersManager.disconnect(id);
	                	}
	                }
	                
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
		}
		
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		}
	
}
