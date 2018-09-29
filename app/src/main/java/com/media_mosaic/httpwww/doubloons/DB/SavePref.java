package com.media_mosaic.httpwww.doubloons.DB;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {
	
	private SharedPreferences prefs;
	String myprefs="Entrepreneur_Challenge";
	int mode = Activity.MODE_PRIVATE;
	boolean result=false;
	String TAG="SavePref";
	Context context;
	public void saveuserId(Context ctx, String id){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userId",id);
		result= editor.commit();

	}
	public void saveusertype(Context ctx, String type){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("usertype",type);
		result= editor.commit();

	}
	public void saveLoggedIn(Context ctx, String login){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("LoggedIn",login);
		result= editor.commit();

	}
	public void savepartnerLoggedIn(Context ctx, String login){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("PartnerLoggedIn",login);
		result= editor.commit();

	}

	public void saveLoggedname(Context ctx, String name){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("Name",name);
		result= editor.commit();

	}
	public void saveLoggedemail(Context ctx, String email){
		result=false;
		prefs = ctx.getSharedPreferences(myprefs, mode);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("Email",email);
		result= editor.commit();

	}











}



