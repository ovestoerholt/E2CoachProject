package no.e2.coach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

//public class TasksFragment extends Fragment implements OnItemClickListener, AbsListView.MultiChoiceModeListener {
//public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener {
public class TasksFragment extends Fragment {

	private ProgressBar progressBar;
	private ListView listView;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_tasks, container, false);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			listView = (ListView) view.findViewById(R.id.listView);
			//listView.setOnItemClickListener(this);
			//listView.setMultiChoiceModeListener(this);
			//listView.setSelector(R.drawable.list_item_selector);
			//listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
			startService();
		} else {
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		return view;
	}

	private void startService() {
		Intent intent = new Intent(getActivity(), TasksService.class);
		intent.putExtra(TasksService.RECEIVER, resultReceiver);
		getActivity().startService(intent);
	}

	/**
	 * Once the {@link no.e2.coach.TasksService} finishes its task, the result is sent to this
	 * ResultReceiver.
	 */
	private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
		@SuppressWarnings("unchecked")
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			progressBar.setVisibility(View.GONE);
			List<Task> items = (List<Task>) resultData.getSerializable(TasksService.ITEMS);
			if (items != null) {
				TasksAdapter adapter = new TasksAdapter(getActivity(), R.layout.fragment_tasks, items);
				listView.setAdapter(adapter);
			} else {
				Toast.makeText(getActivity(), "An error occured while downloading the rss feed.",
						Toast.LENGTH_LONG).show();
			}
		};
	};


	/*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		LunchItemAdapter adapter = (LunchItemAdapter) parent.getAdapter();
		LunchItem item = (LunchItem) adapter.getItem(position);
		Uri uri = Uri.parse(item.getComicLink());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	*/

/*

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

     */
}
