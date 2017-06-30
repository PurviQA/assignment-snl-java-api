package com.qainfotech.tap.training.snl.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BoardTest {

	Board boardreader;
	Board boardreader1;

	@BeforeMethod
	public void loadDB() throws PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {

		boardreader = new Board();

		boardreader.registerPlayer("Purvi");
		boardreader.registerPlayer("Priya");

	}

	@Test
	public void checking_for_constructor() throws IOException {
		JSONObject data1 = boardreader.data;
		Board board = new Board(boardreader.uuid);
		JSONObject data2 = board.data;
		assertThat(data1.toString().compareTo(data2.toString())).isEqualTo(0);

	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void register_player_should_throw_Player_Exists_Exception_for_already_existing_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		boardreader.registerPlayer("Priya");

	}

	@Test
	public void register_player_should_return_players_registered_for_game()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {

		assertThat(((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position")).isEqualTo(0);
		assertThat(boardreader.registerPlayer("Avni").length()).isEqualTo(3);
		assertThat(((JSONObject) boardreader.registerPlayer("Aparna").get(3)).get("name")).isEqualTo("Aparna");
	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void register_player_should_throw_Max_Players_Reached_Exeption_for_max_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		boardreader.registerPlayer("Srishti");
		boardreader.registerPlayer("Anvi");
		boardreader.registerPlayer("Aparna");

	}

	@Test
	public void delete_player_should_delete_the_player_from_the_board()
			throws FileNotFoundException, UnsupportedEncodingException, JSONException, NoUserWithSuchUUIDException {

		UUID uuid = (UUID) boardreader.data.getJSONArray("players").getJSONObject(0).get("uuid");
		assertThat(boardreader.deletePlayer(uuid).length()).isEqualTo(1);

	}

	@Test(expectedExceptions = NoUserWithSuchUUIDException.class)
	public void delete_player_should_throw_No_User_With_Such_UUID_Exception_for_invalid_players()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {
		boardreader.deletePlayer(this.boardreader.getUUID());
	}

	@Test(expectedExceptions = GameInProgressException.class)
	public void register_player_should_throw_game_in_progress_exception_when_player_comes_in_between_the_game()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {

		boardreader.rollDice((UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid"));
		boardreader.registerPlayer("Anvit");
	}

	@Test
	public void roll_dice_should_return_incorrect_roll()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {
		((JSONObject) boardreader.getData().getJSONArray("players").get(0)).put("position", 100);
		UUID uuid = (UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid");
		Object message = boardreader.rollDice(uuid).get("message");

		assertThat(message.toString().compareTo("Incorrect roll of dice. Player did not move")).isEqualTo(0);

	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void roll_dice_should_throw_InvalidTurnException_for_wrong_turn()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {

		boardreader.rollDice((UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(1)).get("uuid"));

	}

	@Test
	public void roll_dice_should_return_outcome_of_the_dice()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {
		UUID uuid = (UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid");
		Object currpos = ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position");
		Object dice = boardreader.rollDice(uuid).get("dice");
		Object exppos = boardreader.getData().getJSONArray("steps").getJSONObject((int) currpos + (int) dice)
				.get("target");
		Object newpos = ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position");

		assertThat(exppos).isEqualTo(newpos);

		UUID uuid1 = (UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(1)).get("uuid");
		boardreader.rollDice(uuid1);
		assertThat(boardreader.getData().get("turn")).isEqualTo(0);

	}

	@Test
	public void checking_type_and_msg_of_response_from_roll_dice()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, JSONException, NoUserWithSuchUUIDException {
		((JSONObject) boardreader.getData().getJSONArray("players").get(0)).put("position", 0);

		UUID uuid = (UUID) ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("uuid");
		Object currpos = ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position");
		JSONObject obj = boardreader.rollDice(uuid);
		Object newpos = ((JSONObject) boardreader.getData().getJSONArray("players").get(0)).get("position");
		Object dice = obj.get("dice");

		Object msg = obj.get("message");

		int number = (int) currpos + (int) dice;
		int type = (int) ((JSONObject) ((JSONArray) boardreader.getData().get("steps")).get(number)).get("type");
		if (type == 0) {
			assertThat(msg.toString().compareTo("Player moved to " + number)).isEqualTo(0);

		}
		if (type == 1) {
			assertThat(msg.toString().compareTo("Player was bit by a snake, moved back to " + newpos)).isEqualTo(0);

		}
		if (type == 2) {
			assertThat(msg.toString().compareTo("Player climbed a ladder, moved to " + newpos)).isEqualTo(0);

		}

	}

}
