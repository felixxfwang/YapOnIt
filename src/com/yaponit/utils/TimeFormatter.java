package com.yaponit.utils;

public class TimeFormatter {
	final static long SECOND = 1000;
	final static long MINUTE = 60 ;
	final static long HOUR = 60 ;
	final static long DAY = 24 ;

	public static String millisFormat(long ms) {
		long millis = (ms % SECOND) /10;
		long seconds = ms / SECOND;
		long minutes = seconds / MINUTE;
		seconds %= MINUTE;
		long hours = minutes / HOUR;
		minutes %= HOUR;
		String time;
		if (hours == 0) {
			time = String.format("%1$02d:%2$02d.%3$02d", minutes, seconds, millis);
		}else{
			time = String.format("%1$02D:%2$02d:%3$02d.%4$02d", hours, minutes, seconds, millis);
		}
		return time;
	}
	
	public static String secondFormat(long s){
		long seconds = s % MINUTE;
		long minutes = s / MINUTE;
		long hours = minutes / HOUR;
		minutes %= HOUR;
		String time;
		if (hours == 0) {
			time = String.format("%1$02d:%2$02d", minutes, seconds);
		}else{
			time = String.format("%1$02D:%2$02d:%3$02d", hours, minutes, seconds);
		}
		return time;
	}

}
