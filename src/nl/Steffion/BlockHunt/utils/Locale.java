package nl.Steffion.BlockHunt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import nl.Steffion.BlockHunt.BlockHunt;

import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Get locale related information from players.
 * Thanks to http://freegeoip.net/ for their API.
 * 
 * @author Steffion
 */
public class Locale {

	/**
	 * Get the player's country code, so you can use it for e.g. the language
	 * system. If it can't find the country assigned to the IP or if the player
	 * is connecting from localhost, it will return "GB".
	 *
	 * @param player
	 *            - Person you want to know the country code of.
	 * @return Read above.
	 * @author Steffion
	 */
	public static String getCountryCode(final Player player) {
		final String urlString = "http://freegeoip.net/json/"
				+ player.getAddress().getHostString();

		/*
		 * This is the format of the returning JSON:
		 * {"ip":"1.1.1.1","country_code
		 * ":"AU","country_name":"Australia","region_code
		 * ":"","region_name":"","city
		 * ":"","zip_code":"","time_zone":"","latitude
		 * ":-27,"longitude":133,"metro_code":0}
		 */
		try {
			final URL url = new URL(urlString);

			final HttpURLConnection request = (HttpURLConnection) url
					.openConnection();
			request.connect();

			final JsonParser parser = new JsonParser();
			final JsonElement element = parser.parse(new InputStreamReader(
					(InputStream) request.getContent()));
			final JsonObject object = element.getAsJsonObject();

			return player.getAddress().getHostString().equals("127.0.0.1") ? "GB"
					: object.get("country_code").getAsString();
		} catch (final MalformedURLException e) {
			BlockHunt.plugin.getLogger().log(Level.SEVERE,
					"Malformed URL Exception:", e);
		} catch (final IOException e) {
			BlockHunt.plugin.getLogger().log(Level.SEVERE,
					"Input Output Exception:", e);
		}

		return "GB";
	}
}