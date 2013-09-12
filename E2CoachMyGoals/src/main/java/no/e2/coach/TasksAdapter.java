package no.e2.coach;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends ArrayAdapter<Task> {

	Context context;
	int layoutResourceId;
	List<Task> data;

	public TasksAdapter(Context context, int layoutResourceId, List<Task> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskHolder holder = null;

		if(row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new TaskHolder();
			holder.title = (TextView)row.findViewById(R.id.title);
			holder.datetime = (TextView)row.findViewById(R.id.datetime);
			holder.category = (TextView)row.findViewById(R.id.category);

			row.setTag(holder);
		} else {
			holder = (TaskHolder)row.getTag();
		}


		Task task = data.get(position);
		holder.title.setText(task.getTitle());

		Resources resources = context.getResources();
		Locale currentLocale = resources.getConfiguration().locale;
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
		String formattedDate = df.format(task.getDateTime());
		holder.datetime.setText(String.format(resources.getString((R.string.rowDateTime), formattedDate)));



		return row;
	}

	static class TaskHolder
	{
		TextView title;
		TextView datetime;
		TextView category;
	}
}