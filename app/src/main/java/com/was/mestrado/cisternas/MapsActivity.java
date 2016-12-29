package com.was.mestrado.cisternas;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Cisterna;
import services.CisternaService;
import util.DrawableUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATUALIZA O MAPA A CADA 10 SEGUNDOS
        //PENSAR EM MUDAR A ESTRATÉGIA DEPOIS
        //---------------------------------------------------------------------------
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {
                    new BuscaTask().execute();
                    handler.postDelayed(this, 10 * 1000); //now is every 2 minutes
                }
            }, 10 * 1000);
        //---------------------------------------------------------------------------

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cin = new LatLng(-8.055861, -34.951083);
        mMap.addMarker(new MarkerOptions()
                .position(cin)
                .title("CIn"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cin));
        CameraPosition cameraPosition=new CameraPosition.Builder()
                .target(cin)
                .zoom(9)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        new BuscaTask().execute();
    }
    List<Cisterna>cisternas=new ArrayList<Cisterna>();

    private void atualizaMapa(){
         mMap.clear();
         Cisterna cisterna;
         for(int i=0;i<cisternas.size();i++){
             cisterna=cisternas.get(i);
             //POR ENQUANTO SÓ PARA TESTE
             //DEPOIS SAI
             //------------------
                if(i==1){
                    Random gerador = new Random();

                    double volume = gerador.nextDouble()*cisterna.getCapacidade();
                    cisterna.setVolume(volume);
                }

             //------------------
             String[] localizacao=cisterna.getLocalizacao().split(",");
             LatLng latlng=new LatLng(Double.parseDouble(localizacao[0]),Double.parseDouble(localizacao[1]));

             Bitmap markerBmp = getIcon(cisterna.getVolume(),cisterna.getCapacidade());
             Marker marker=mMap.addMarker(new MarkerOptions()
                     .position(latlng)
                     .title(cisterna.getId())
                     .snippet(getSnippet(cisterna))

                     //.icon(BitmapDescriptorFactory.defaultMarker(getIcon(cisterna.getVolume(),cisterna.getCapacidade()))));
                     .icon(BitmapDescriptorFactory.fromBitmap(markerBmp)));
             mMap.setInfoWindowAdapter(getInfoWindow());
         }
    }

    private Bitmap getIcon(double volume, double capacidade){
        int drawableId;
        Bitmap markerBmp;

        double percent=(volume/capacidade)*100;
        if(percent<25){
            drawableId = R.drawable.marker_red_ecopoint;
        }else if(percent>=25 && percent<50){
            drawableId = R.drawable.marker_orange_ecopoint;
        }else if(percent>=50 && percent<75){
            drawableId = R.drawable.marker_green_ecopoint;
        }else{
            drawableId = R.drawable.marker_green_ecopoint;
        }
        String percento = String.format("%.0f", percent);
        markerBmp=DrawableUtil.getMarkerView(this, percento, drawableId);
        return  markerBmp;
    }

    /*ANTIGA
    private float getIcon(double volume, double capacidade){
        float retorno;
        double percent=(volume/capacidade)*100;
        if(percent<25){
            retorno=BitmapDescriptorFactory.HUE_RED;
        }else if(percent>=25 && percent<50){
            retorno=BitmapDescriptorFactory.HUE_ORANGE;
        }else if(percent>=50 && percent<75){
            retorno=BitmapDescriptorFactory.HUE_YELLOW;
        }else{
            retorno=BitmapDescriptorFactory.HUE_GREEN;
        }
        return  retorno;
    }
    */
    private String getSnippet(Cisterna cisterna){
        double percent=(cisterna.getVolume()/cisterna.getCapacidade())*100;
        String info="Capacidade: "+cisterna.getCapacidade()+" Litros \n"
                +"Volume: "+String.format("%.1f", cisterna.getVolume())+" Litros \n"
                +"Porcentagem: "+ String.format("%.1f", percent)+"% da capacidade";
        return info;
    }
    private GoogleMap.InfoWindowAdapter getInfoWindow(){
        GoogleMap.InfoWindowAdapter infoWindowAdapter=new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        };
        return infoWindowAdapter;
    }


    private ProgressDialog progress;
    private class BuscaTask extends AsyncTask<String, String, Boolean> {
        protected void onPreExecute() {
           // progress= ProgressDialog.show(MapsActivity.this, "Buscando...","Espere");//show progress
        }
        protected Boolean  doInBackground(String... params) {
            CisternaService cisternaService=new CisternaService();
            cisternas=cisternaService.getCisternas();

            if(cisternas!=null){
                Log.d("was->","sisterna 1: "+cisternas.get(0).toString());
                return true;
            }else{
                Log.d("was->","Sisterna não existe");
                return false;
            }

        }
        protected void onPostExecute(Boolean args) {
            //progress.dismiss();
            if (args==true){
                atualizaMapa();
            }else{
                Toast.makeText(MapsActivity.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }
        }
    }



}
