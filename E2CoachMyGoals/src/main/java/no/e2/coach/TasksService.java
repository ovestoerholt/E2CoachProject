package no.e2.coach;


import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class TasksService extends IntentService {

	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";

	public TasksService() {
		super("TasksService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(Constants.TAG, "Service started");
		List<Task> tasks = null;
		try {
			TaskParser parser = new TaskParser();
			tasks = parser.parse(getInputStream());
		} catch (XmlPullParserException e) {
			Log.w(e.getMessage(), e);
		} catch (IOException e) {
			Log.w(e.getMessage(), e);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable(ITEMS, (Serializable) tasks);
		ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
		receiver.send(0, bundle);
	}

	public InputStream getInputStream() {
		// To load text file
		InputStream input = null;
		try {
			AssetManager am = getApplicationContext().getAssets();
			input = am.open("tasks.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}