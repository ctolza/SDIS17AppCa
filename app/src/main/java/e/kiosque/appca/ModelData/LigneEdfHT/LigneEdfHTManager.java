package e.kiosque.appca.ModelData.LigneEdfHT;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.mapbox.geojson.Feature;


import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;

import e.kiosque.appca.ModelData.Etare.Etare;
import e.kiosque.appca.ModelData.Pei.Pei;

public class LigneEdfHTManager {

    private List<LigneEdfHT> ligneEdfHTList = new ArrayList<>();

    public LigneEdfHTManager() {
    }

    public void initListLigneEdfHT(Context context, int ressourceId) {
        String jsonEtare;
        List<Feature> features;
        jsonEtare = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonEtare);
            for (Feature f : features) {
                String nom;
                String commune;
                String insee;
                String idKey;
                String tension;
                LineString geometry;
                try{
                    nom = f.getStringProperty("autrenom");
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , "autrenom : " + er.toString());
                    nom = "";
                }
                try{
                    commune = f.getStringProperty("autrenom");
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , "autrenom : " + er.toString());
                    commune = "";
                }
                try{
                    insee = f.getStringProperty("autrenom");
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , "autrenom : " + er.toString());
                    insee = "";
                }
                try{
                    idKey = f.getStringProperty("autrenom");
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , "autrenom : " + er.toString());
                    idKey = "";
                }
                try{
                    tension = f.getStringProperty("autrenom");
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , "autrenom : " + er.toString());
                    tension = "";
                }
                try{
                    geometry = LineString.fromJson(f.geometry().toJson());
                }catch (UnsupportedOperationException er){
                    Log.e("initEtare : " , " Geometry" +  er.toString());
                    geometry = null;
                }
                ligneEdfHTList.add(new LigneEdfHT(nom,commune,insee,Integer.valueOf(idKey),tension,geometry));
            }
        }
    public LigneEdfHT getLigneEdfHTById (int id) {
        return ligneEdfHTList.get(id);
    }

    public List<LigneEdfHT> getLignesEdfHT () {
        return ligneEdfHTList;
    }

    public void addEtare (LigneEdfHT e){
        ligneEdfHTList.add(e);
    }

    public int ligneEdfHTLength () {
        return ligneEdfHTList.size();
    }


}
