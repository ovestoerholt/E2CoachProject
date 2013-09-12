package no.e2.coach;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ove on 8/29/13.
 */
public class Task implements Serializable {
	private String title;
	private Date dateTime;
	private boolean completed;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
