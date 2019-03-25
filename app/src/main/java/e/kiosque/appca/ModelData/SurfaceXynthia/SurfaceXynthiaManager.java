package e.kiosque.appca.ModelData.SurfaceXynthia;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class SurfaceXynthiaManager {
    private List<SurfaceXynthia> surfaceXynthiaList = new ArrayList<>();

    public SurfaceXynthiaManager() {
    }

    public void initListSurfaceXynthia(Context context, int ressourceId) {
        String jsonEtare;
        List<Feature> features;
        jsonEtare = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonEtare);
        for (Feature f : features) {
            String nom ="";
            String id ="";
            Polygon geometry;

            try{
                nom = f.getStringProperty("NOM");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "NOM : " + er.toString());
                nom = "";
            }
            try{
                id = f.getStringProperty("ID");
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , "ID : "+ er.toString());
                id = "";
            }
            try{
                geometry = Polygon.fromJson(f.geometry().toJson());
            }catch (UnsupportedOperationException er){
                Log.e("initEtare : " , " Geometry" +  er.toString());
                geometry = null;
            }

            surfaceXynthiaList.add(new SurfaceXynthia(
                    nom,
                    Integer.valueOf(id),
                    geometry));
        }
    }

    public SurfaceXynthia getXynthiaById (int id) {
        return surfaceXynthiaList.get(id);
    }

    public List<SurfaceXynthia> getSurfaceXynthias () {
        return surfaceXynthiaList;
    }

    public void addEtare (SurfaceXynthia p){
        surfaceXynthiaList.add(p);
    }

    public int etaresLength () {
        return surfaceXynthiaList.size();
    }
}
