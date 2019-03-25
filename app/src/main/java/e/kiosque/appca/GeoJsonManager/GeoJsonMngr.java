package e.kiosque.appca.GeoJsonManager;

import android.content.Context;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonMngr {

    public static String loadGeoJson(Context context, int ressourceId) {
        String jsonString = null;
        try {
            InputStream is = context.getResources().openRawResource(ressourceId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            return jsonString;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static List<Feature> createFeaturesCollection(String jsonString) {
        FeatureCollection featureCollection = FeatureCollection.fromJson(jsonString);
        List<Feature> features = featureCollection.features();
        return features;
    }

}
