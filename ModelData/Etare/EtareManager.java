package e.kiosque.appca.ModelData.Etare;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class EtareManager {

    private List<Etare> etares = new ArrayList<>();

    public EtareManager() {
    }

    public void initListEtare(Context context, int ressourceId) {
        String jsonEtare;
        List<Feature> features;
        jsonEtare = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features =  GeoJsonMngr.createFeaturesCollection(jsonEtare);
        for (Feature f : features) {
            String nom ="";
            String xPourGPS ="";
            String yPourGPS ="";
            String idKey ="";
            String planEr ="";
            String commune ="";
            String INSEE ="";
            String address ="";
            String httpDoc="";
            Point geometry;

            try{
                nom = f.getStringProperty("NOM");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "nom : " + er.toString());
                nom = "";
            }
            try{
                xPourGPS = f.getStringProperty("XPOURGPS");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "XPOURGPS : "+ er.toString());
                xPourGPS = "";
            }
            try{
                yPourGPS = f.getStringProperty("YPOURGPS");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "YPOURGPS" + er.toString());
                yPourGPS = "";
            }
            try{
                idKey = f.getStringProperty("ID_KEY");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "ID_KEY" + er.toString());
                idKey = "";
            }
            try{
                planEr = f.getStringProperty("N__PLAN_ER");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "N__PLAN_ER"+ er.toString());
                planEr = "";
            }
            try{
                commune = f.getStringProperty("COMMUNE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "COMMUNE" + er.toString());
                commune = "";
            }
            try{
                INSEE = f.getStringProperty("INSEE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "INSEE " + er.toString());
                INSEE = "";
            }
            try{
                address = f.getStringProperty("ADRESSE_AD");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "ADRESSE_AD " +er.toString());
                address = "";
            }
            try{
                httpDoc = f.getStringProperty("HTTP_ETARE");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "HTTP_ETARE "+ er.toString());
                httpDoc = "";
            }
            try{
                geometry = Point.fromJson(f.geometry().toJson());
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , " Geometry" +  er.toString());
                geometry = null;
            }

            etares.add(new Etare(
                    nom,
                    Double.valueOf(xPourGPS),
                    Double.valueOf(yPourGPS),
                    Integer.valueOf(idKey),
                    planEr,
                    commune,
                    Integer.valueOf(INSEE),
                    address,
                    httpDoc, geometry));
        }
    }

    public Etare getEtareById (int id) {
        return etares.get(id);
    }

    public List<Etare> getEtares () {
        return etares;
    }

    public void addEtare (Etare e){
        etares.add(e);
    }

    public int etaresLength () {
        return etares.size();
    }

}
