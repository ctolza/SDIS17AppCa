package e.kiosque.appca.ModelData.Pei;

import android.content.Context;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;

public class PeiManager {

    private List<Pei> peiList = new ArrayList<>();

    public PeiManager() {
    }



    public List<Pei> initListPei(Context context, int ressourceId) {
        String jsonPei;
        List<Feature> features;
        jsonPei = GeoJsonMngr.loadGeoJson(context, ressourceId);
        features = GeoJsonMngr.createFeaturesCollection(jsonPei);
        for (Feature f : features) {
            String autreNom = "";
            String commune = "";
            String debit1b = "";
            String disponibilite = "";
            String domaine = "";
            String insee = "";
            String nom = "";
            String pression = "";
            String volume = "";
            String communeSt = "";
            String tyStart = "";
            Point geometry;

            try {
                autreNom = f.getStringProperty("autrenom");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "autrenom : " + er.toString());
                autreNom = "";
            }
            try {
                commune = f.getStringProperty("commune");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "commune : " + er.toString());
                commune = "";
            }
            try {
                debit1b = f.getStringProperty("debit_1b");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "debit_1b" + er.toString());
                debit1b = "0";
            }
            try {
                disponibilite = f.getStringProperty("disponibil");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "disponibil" + er.toString());
                disponibilite = "";
            }
            try {
                domaine = f.getStringProperty("domaine");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "domaine" + er.toString());
                domaine = "";
            }
            try {
                insee = f.getStringProperty("insee");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "insee" + er.toString());
                insee = "0";
            }
            try {
                nom = f.getStringProperty("nom");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "nom " + er.toString());
                nom = "";
            }
            try {
                pression = f.getStringProperty("pression");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "pression " + er.toString());
                pression = "0";
            }
            try {
                volume = f.getStringProperty("volume");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "volume " + er.toString());
                volume = "";
            }
            try {
                communeSt = f.getStringProperty("commune_st");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "commune_st " + er.toString());
                communeSt = "";
            }
            try {
                tyStart = f.getStringProperty("ty_start");
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", "ty_start " + er.toString());
                tyStart = "";
            }
            try {
                geometry = Point.fromJson(f.geometry().toJson());
            } catch (UnsupportedOperationException er) {
                Log.e("initPei : ", " Geometry" + er.toString());
                geometry = null;
            }

            peiList.add(new Pei(
                    autreNom,
                    commune,
                    Double.valueOf(debit1b),
                    disponibilite,
                    domaine,
                    Integer.valueOf(insee),
                    nom,
                    Double.valueOf(pression),
                    volume,
                    communeSt,
                    tyStart,
                    geometry));
        }
        return peiList;
    }

    public Pei getPeiById(int id) {
        return peiList.get(id);
    }

    public List<Pei> getPeis() {
        return peiList;
    }

    public void addPei(Pei p) {
        peiList.add(p);
    }

    public int peiLength() {
        return peiList.size();
    }
}
