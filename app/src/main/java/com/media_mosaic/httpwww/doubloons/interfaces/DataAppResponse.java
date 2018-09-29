package com.media_mosaic.httpwww.doubloons.interfaces;

/**
 * Created by Seo on 31-10-2017.
 */

public interface DataAppResponse {

    public void onResponse(String response, String responseType);
    public void onError(String error, String responseType);

}
