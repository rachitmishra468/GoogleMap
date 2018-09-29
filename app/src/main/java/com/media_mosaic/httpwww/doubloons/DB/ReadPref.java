package com.media_mosaic.httpwww.doubloons.DB;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class ReadPref {
	
	private SharedPreferences prefs;
	String myprefs="Entrepreneur_Challenge";
	int mode = Activity.MODE_PRIVATE;
	boolean result=false;
	String TAG="ReadPref";
	Context ctx;
	String res="";
	
	public ReadPref(Context ctx){
		this.ctx=ctx;
		prefs = this.ctx.getSharedPreferences(myprefs, mode);
	}


	public String getuserId(){
		String userId="";
		userId=prefs.getString("userId", "");
		return userId;
	}

	public String getusertype(){
		String usertype="";
		usertype=prefs.getString("usertype", "");
		return usertype;
	}
	public String getLoggedIn(){
		String userId="";
		userId=prefs.getString("LoggedIn", "");
		return userId;
	}
	public String getpartnerLoggedIn(){
		String userId="";
		userId=prefs.getString("PartnerLoggedIn", "");
		return userId;
	}
	public String getLoggedname(){
		String userId="";
		userId=prefs.getString("Name", "");
		return userId;
	}
	public String getLoggedemail(){
		String userId="";
		userId=prefs.getString("Email", "");
		return userId;
	}
}
