package com.qainfotech.tap.training.snl.api;
import com.qainfotech.tap.training.snl.api.Board;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BoardTest {

    
   Board boardreader;
   BoardModel board;
    
    @BeforeTest
    public void loadDB() throws PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
    	
    	boardreader = new Board();
    	board=new BoardModel();
    	boardreader.registerPlayer("Purvi");
    	boardreader.registerPlayer("Priya");
    	

    	
    }
    
     @Test
	public void register_player_should_return_players_register_forgame() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException
	{
		
		assertThat(((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position")).isEqualTo(0);
		assertThat( boardreader.registerPlayer("Avni").length()).isEqualTo(2);
		assertThat( ((JSONObject) boardreader.registerPlayer("Aparna").get(2)).get("name")).isEqualTo("Aparna");
		}
     
     @Test(expectedExceptions=MaxPlayersReachedExeption.class)
     public void register_player_should_throw_Max_Players_Reached_Exeption_for_max_players() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
     {
    	 boardreader.registerPlayer("Srishti");
    	 boardreader.registerPlayer("Anu");
     }
     @Test
     public void delete_player_should_delete_the_player_from_the_board() throws FileNotFoundException, UnsupportedEncodingException, JSONException, NoUserWithSuchUUIDException
     {
    	
    	UUID uuid=(UUID)boardreader.data.getJSONArray("players").getJSONObject(0).get("uuid");
    	assertThat(boardreader.deletePlayer(uuid).length()).isEqualTo(1);
    	
    	
     }
     @Test(expectedExceptions=NoUserWithSuchUUIDException.class)
     public void delete_player_should_throw_No_User_With_Such_UUID_Exception_for_invalid_players() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException
     {
    	boardreader.deletePlayer(this.boardreader.getUUID());
     }

}
