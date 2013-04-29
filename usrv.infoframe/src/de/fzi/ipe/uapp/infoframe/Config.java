package de.fzi.ipe.uapp.infoframe;

import org.osgi.framework.FrameworkUtil;

import org.universAAL.ucc.configuration.api.ConfigPreferences;

/**
 * 
 * The configuration class for this use case.
 * 
 * @author Sebastian Schoebinger
 *
 */
public class Config {
	
	private ConfigPreferences config;
	
	private int pointerY;
	
	/**
	 * Create new ConfigPreferences for this bundle.
	 */
	public Config(){
		config = new ConfigPreferences(FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundle());
	}

	public int getMax_displayed_events() {
		// Get the value for id "calendarMsgCount" or the defualt 3.
		return config.getInt("calendarMsgCount", 3);
	}

	public int getUpdatetime() {
		return config.getInt("RSSUpdateTime", 10);
	}

	public int getImage_width() {
		return config.getInt("ImageWidth", 1200);
	}

	public int getImage_height() {
		return config.getInt("ImageHeight", 700);
	}

	public String getFont() {
		return config.getString("InfoFont", "Arial");
	}

	public double getFont_scale() {
		return config.getDouble("FontScale", 1);
	}

	public String getLocation() {
		return config.getString("WeatherStationLocation", "Heilbronn");
	}

	public String getLanguage() {
		return config.getString("Language", "de");
	}

	public int getAdd_mailplugin() {
		return config.getInt("addMailPlugin", 0);
	}

	public int getAdd_weahterplugin() {
		return config.getInt("addWeatherPlugin", 0);
	}
	public String getProtocol() {
		return config.getString("mailProtocol", "imaps");
	}

	public String getHost() {
		return config.getString("mailServer", "imap.gmail.com");
	}

	public String getUser() {
		return config.getString("mailUsername", "");
	}

	public String getPassword() {
		return config.getString("mailPassword", "");
	}

	public int getMsg_count() {
		return config.getInt("mailMsgCount", 2);
	}

	public int getAdd_calendarplugin() {
		return config.getInt("addCalendarPlugin", 0);
	}

	public String getCalendar_user() {
		return config.getString("calendarUsername", "");
	}

	public String getCalendar_password() {
		return config.getString("calendarPassword", "");
	}

	public int getAdd_feedplugin() {
		return config.getInt("AddFeedPlugin", 1);
	}

	public String getFeed_url_1() {
		return config.getString("FeedURL1", "http://www.heise.de/newsticker/heise-top-atom.xml");
	}

	public int getMax_displayed_items_1() {
		return config.getInt("MaxDispItems1", 3);
	}

	public String getTitle_regex_1() {
		return config.getString("RegExp1", "");
	}

	public String getFeed_url_2() {
		return config.getString("FeedURL2", "http://twitter.com/statuses/user_timeline/66680076.rss");
	}

	public int getMax_displayed_items_2() {
		return config.getInt("MaxDispItems2", 3);
	}

	public String getTitle_regex_2() {
		return config.getString("RegExp1", "");
	}

	public String getFeed_url_3() {
		return config.getString("FeedURL3", "http://twitter.com/statuses/user_timeline/62478104.rss");
	}

	public int getMax_displayed_items_3() {
		return config.getInt("MaxDispItems3", 3);
	}

	public String getTitle_regex_3() {
		return config.getString("RegExp1", "");
	}

	public int getPointerY() {
		return pointerY;
	}

	public void setPointerY(int pointerY) {
		this.pointerY = pointerY;
	}

	public String getProperty(String id) {
		return config.getString(id, "");
	}
	
}
