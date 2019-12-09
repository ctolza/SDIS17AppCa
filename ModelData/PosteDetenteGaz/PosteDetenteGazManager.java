package e.kiosque.appca.ModelData.PosteDetenteGaz;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class PosteDetenteGazManager {

    private List<PosteDetenteGaz> posteDetenteGazList = new ArrayList<>();

    public PosteDetenteGazManager() {
    }

    public void initListPosteDetenteGaz(Context context, int ressourceId) {
        String jsonGaz;
        List<Feature> features;
        jsonGaz = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonGaz);
        for (Feature f : features) {
            String nom ="";
            String commune ="";
            String insee ="";
            String idKey ="";
            Point geometry;

            try{
                nom = f.getStringProperty("NOM");
            }catch (UnsupportedOperationException er){
                Log.e("initGaz : " , "NOM : " + er.toString());
                nom = "";
            }
            try{
                commune = f.getStringProperty("COMMUNE");
            }catch (UnsupportedOperationException er){
                Log.e("initGaz : " , "COMMUNE : "+ er.toString());
                commune = "";
            }
            try{
                insee = f.getStringProperty("INSEE");
            }catch (UnsupportedOperationException er){
                Log.e("initGaz : " , "INSEE" + er.toString());
                insee = "0";
            }
            try{
                idKey = f.getStringProperty("ID_KEY");
            }catch (UnsupportedOperationException er){
                Log.e("initGaz : " , "ID_KEY " + er.toString());
                idKey = "0";
            }
            try{
                geometry = Point.fromJson(f.geometry().toJson());
            }catch (UnsupportedOperationException er){
                Log.e("initGaz : " , " Geometry" +  er.toString());
                geometry = null;
            }

            posteDetenteGazList.add(new PosteDetenteGaz(
                    nom,
                    commune,
                    Integer.valueOf(insee),
                    Integer.valueOf(idKey),
                    geometry));
        }
    }

    public PosteDetenteGaz getPosteDetenteGazById (int id) {
        return posteDetenteGazList.get(id);
    }

    public List<PosteDetenteGaz> getPosteDetenteGazs () {
        return posteDetenteGazList;
    }

    public void addGaz (PosteDetenteGaz p){
        posteDetenteGazList.add(p);
    }

    public int gazLength() {
        return posteDetenteGazList.size();
    }
}
