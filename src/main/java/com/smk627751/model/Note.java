package com.smk627751.model;

public class Note {
	private int id;
	private String title;
	private String content;
	private String lastEdit;
	public Note(int id,String title,String content,String lastEdit)
	{
		this.id = id;
		this.title = title;
		this.content = content;
		this.lastEdit = lastEdit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(String lastEdit) {
		this.lastEdit = lastEdit;
	}
}
