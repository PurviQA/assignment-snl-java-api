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
		board = new BoardModel();
		boardreader.registerPlayer("Purvi");
		boardreader.registerPlayer("Priya");
	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void a_register_player_should_throw_Player_Exists_Exception_for_already_existing_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		boardreader.registerPlayer("Priya");

	}

	@Test
	public void b_register_player_should_return_players_register_forgame()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {

		assertThat(((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position")).isEqualTo(0);
		assertThat(boardreader.registerPlayer("Avni").length()).isEqualTo(3);
		assertThat(((JSONObject) boardreader.registerPlayer("Aparna").get(3)).get("name")).isEqualTo("Aparna");
	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void c__register_player_should_throw_Max_Players_Reached_Exeption_for_max_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		boardreader.registerPlayer("Srishti");

	}

	@Test
	public void delete_player_should_delete_the_player_from_the_board()
			throws FileNotFoundException, UnsupportedEncodingException, JSONException, NoUserWithSuchUUIDException {

		UUID uuid = (UUID) boardreader.data.getJSONArray("players").getJSONObject(0).get("uuid");
		assertThat(boardreader.deletePlayer(uuid).length()).isEqualTo(3);

	}

	@Test(expectedExceptions = NoUserWithSuchUUIDException.class)
	public void delete_player_should_throw_No_User_With_Such_UUID_Exception_for_invalid_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {
		boardreader.deletePlayer(this.boardreader.getUUID());
	}

	@Test(expectedExceptions = GameInProgressException.class)
	public void qregister_player_should_throw_game_in_progress_exception_when_player_comes_in_between_the_game()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {

		boardreader.rollDice((UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid"));
		boardreader.registerPlayer("Anvit");
	}
	@Test(expectedExceptions = InvalidTurnException.class)
	public void roll_dice_should_throw_InvalidTurnException_when_new_position_reach_100()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {
		((JSONObject) boardreader.getData().getJSONArray("players").get(0)).put("position",100);
		boardreader.rollDice((UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid"));

		
	}
	@Test
	public void roll_dice_should_return_outcome_of_the_dice()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {
		UUID uuid=(UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(1)).get("uuid");
		Object dice= boardreader.rollDice(uuid).get("dice");
		assertThat(((JSONObject) boardreader.getData().getJSONArray("players").get(1)).get("position")).isEqualTo(dice);

		
	}

}
