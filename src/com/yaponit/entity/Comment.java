package com.yaponit.entity;

public class Comment {

	int id;
	String author;
	String content;
	String date;
	boolean audio;

	public Comment(int id,  String content, String author, String date,boolean audio) {
		this.id = id;
		this.author = author;
		this.content = content;
		this.date = date;
		this.audio = audio;
	}

	public int getId() {
		return this.id;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getContent() {
		return this.content;
	}

	public String getDate() {
		return this.date;
	}
	
	public boolean getAudio(){
		return this.audio;
	}
}
