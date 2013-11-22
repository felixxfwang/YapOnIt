package com.yaponit.entity;

public class Post {

	int id;
	String title;
	String content;
	String date;
	int comments;
	int votes;
	String author;
	boolean answered;

	public Post(int id, String title, String content, String date,int comments,int votes,String author,boolean answered) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.comments = comments;
		this.votes = votes;
		this.author = author;
		this.answered = answered;
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

	public String getDate() {
		return this.date;
	}
	
	public int getVotesCount(){
		return this.votes;
	}

	public int getCommentsCount(){
		return this.comments;
	}
	
	public String getAuthor() {
		return this.author;
	}
	public boolean getAnswered(){
		return this.answered;
	}
	public void setAnswered(boolean answered){
		this.answered = answered;
	}
}
