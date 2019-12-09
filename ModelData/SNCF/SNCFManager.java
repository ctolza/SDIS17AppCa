package e.kiosque.appca.ModelData.SNCF;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class SNCFManager {
    private List<SNCF> sncfList = new ArrayList<>();

    public SNCFManager() {
    }

    public void initListSNCF(Context context, int ressourceId) {
        String jsonEtare;
        List<Feature> features;
        jsonEtare = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonEtare);

        for (Feature f : features) {
            String sousType ="";
            String nom ="";
            String commune ="";
            String insee ="";
            String idKey ="";
            Point geometry;

            try{
                sousType = f.getStringProperty("SOUS-TYPE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "SOUS-TYPE : " + er.toString());
                sousType = "";
            }
            try{
                nom = f.getStringProperty("NOM");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "NOM : "+ er.toString());
                nom = "";
            }
            try{
                commune = f.getStringProperty("COMMUNE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "COMMUNE" + er.toString());
                commune = "";
            }
            try{
                insee = f.getStringProperty("INSEE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "INSEE" + er.toString());
                insee = "0";
            }
            try{
                idKey = f.getStringProperty("ID_KEY");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "ID_KEY"+ er.toString());
                idKey = "0";
            }
            try{
                geometry = Point.fromJson(f.geometry().toJson());
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , " Geometry" +  er.toString());
                geometry = null;
            }

            sncfList.add(new SNCF(
                    sousType,
                    nom,
                    commune,
                    Integer.valueOf(insee),
                    Integer.valueOf(idKey),
                    geometry)
            );


        }
    }

    public SNCF getSNCFById (int id) {
        return sncfList.get(id);
    }

    public List<SNCF> getSNCFs () {
        return sncfList;
    }

    public void addEtare (SNCF p){
        sncfList.add(p);
    }

    public int sncfLength() {
        return sncfList.size();
    }
}
