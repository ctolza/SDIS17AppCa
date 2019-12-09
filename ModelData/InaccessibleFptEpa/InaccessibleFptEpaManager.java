package e.kiosque.appca.ModelData.InaccessibleFptEpa;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class InaccessibleFptEpaManager {

    private List<InaccessibleFptEpa> inaccessibleFptEpaList = new ArrayList<>();

    public InaccessibleFptEpaManager() {
    }

    public void initListInaccessibleFptEpa(Context context, int ressourceId) {
        String jsonEtare;
        List<Feature> features;
        jsonEtare = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonEtare);
        for (Feature f : features) {
            String nom ="";
            String xPourGPS ="";
            String yPourGPS ="";
            Point geometry;

            try{
                nom = f.getStringProperty("NOM");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "NOM : " + er.toString());
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
                geometry = Point.fromJson(f.geometry().toJson());
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , " Geometry" +  er.toString());
                geometry = null;
            }

            inaccessibleFptEpaList.add(new InaccessibleFptEpa(
                    nom,
                    Double.valueOf(xPourGPS),
                    Double.valueOf(yPourGPS),
                    geometry));
        }
    }

    public InaccessibleFptEpa getInaccessibleFptEpaById (int id) {
        return inaccessibleFptEpaList.get(id);
    }

    public List<InaccessibleFptEpa> getInaccessibleFptEpas () {
        return inaccessibleFptEpaList;
    }

    public void addEtare (InaccessibleFptEpa p){
        inaccessibleFptEpaList.add(p);
    }

    public int fptLength() {
        return inaccessibleFptEpaList.size();
    }
}
