# GoogleMap
![alt text](https://developers.google.com/maps/documentation/android-sdk/images/map-with-marker.png "Description goes here")

The basic steps for adding a map are:

(You only need to do this step once.) Follow the steps in the project configuration guide to get the API, obtain a key and add the required attributes to your Android manifest.
Add a Fragment object to the Activity that will handle the map. The easiest way to do this is to add a <fragment> element to the layout file for the Activity.
Implement the OnMapReadyCallback interface and use the onMapReady(GoogleMap) callback method to get a handle to the GoogleMap object. The GoogleMap object is the internal representation of the map itself. To set the view options for a map, you modify its GoogleMap object.
Call getMapAsync() on the fragment to register the callback.

Add a fragment
Add a <fragment> element to the activity's layout file to define a Fragment object. In this element, set the android:name attribute to "com.google.android.gms.maps.MapFragment". This automatically attaches a MapFragment to the activity.



<?xml version="1.0" encoding="utf-8"?>
<fragment xmlns:android="http://schemas.android.com/apk/res/android"
    android:name="com.google.android.gms.maps.MapFragment"
    android:id="@+id/map"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
    
    
    If any Query Please Contact My Email
    # Rachitmishra468@gmail.com
    #RACHIT MISHRA
