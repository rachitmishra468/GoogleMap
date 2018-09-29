package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import com.media_mosaic.httpwww.doubloons.Data_Model.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media_mosaic.httpwww.doubloons.Adapters.GalleryAdapter;
import com.media_mosaic.httpwww.doubloons.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    View view;
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_gallery, container, false);

        pDialog = new ProgressDialog(getActivity());
        images = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mAdapter = new GalleryAdapter(getActivity(), images);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fetchImages(getArguments().getString("img"));


/*
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
               // newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
*/
       return  view;
    }





    private void fetchImages(String img) {

       try {
           JSONArray response = new JSONArray(img);
           for (int i = 0; i < response.length(); i++) {
               try {

                   Image image = new Image();
                   image.setSmall(response.get(i).toString());
                   images.add(image);

               } catch (JSONException e) {

               }
           }
       }catch (JSONException e){
       }
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
