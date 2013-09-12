package no.e2.coach;


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TaskParser {

	// We don't use namespaces
	private final String ns = null;

	public List<Task> parse(InputStream inputStream) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();

			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			inputStream.close();
		}
	}

	private List<Task> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, "rss");
		String title = null;
		String category = null;
		Date dateTime = null;
		boolean completed = false;

		boolean isItemProcessing = false;

		List<Task> items = new ArrayList<Task>();

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = parser.getName();
				Log.i(Constants.TAG, String.format("Start tag name=%s", name));
				if ("task".equals(name)) {
					isItemProcessing = true;
				} else if (name.equals("title")) {
					if (isItemProcessing) {
						title = readTitle(parser);
					}

				} else if (name.equals("category")) {
					if (isItemProcessing) {
						category = readCategory(parser);
					}

				} else if (name.equals("datetime")) {
					if (isItemProcessing) {
						dateTime = readDate(parser);
					}
				} else if (name.equals("completed")) {
					if (isItemProcessing) {
						completed = readCompleted(parser);
					}
				}

				if (title != null && dateTime != null && category != null) {
						Task item = new Task();
						item.setTitle(title);
						item.setCategory(category);
						item.setDateTime(dateTime);

						items.add(item);

					title = null;
					dateTime = null;
					category = null;
					completed = false;
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				String name = parser.getName();
				Log.i(Constants.TAG, String.format("Start tag name=%s", name));
				if ("item".equals(name)) {
					isItemProcessing = false;
				}
			}

			eventType = parser.next();
		}
		return items;
	}


	private boolean readCompleted(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "completed");
		String completed = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "completed");
		boolean isCompleted = false;
		if (completed.compareTo("1") == 0) {
			isCompleted = true;
		}
		return isCompleted;
	}

	private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	private String readCategory(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "category");
		String category = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "category");
		return category;
	}


	private Date readDate(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "datetime");
		String dateString = readText(parser);
		Date date = null;
		try {
			date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		parser.require(XmlPullParser.END_TAG, ns, "datetime");
		return date;
	}


	// For the tags title and link, extract their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}


}
