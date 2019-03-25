package e.kiosque.appca.ModelData.LigneEdfHT;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;

public class LigneEdfHT {
    private String nom;
    private String commune;
    private String insee;
    private int idKey;
    private String tension;
    private LineString geometry;

    public LigneEdfHT(String nom, String commune, String insee, int idKey, String tension, LineString geometry) {
        this.nom = nom;
        this.commune = commune;
        this.insee = insee;
        this.idKey = idKey;
        this.tension = tension;
        this.geometry = geometry;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getInsee() {
        return insee;
    }

    public void setInsee(String insee) {
        this.insee = insee;
    }

    public int getIdKey() {
        return idKey;
    }

    public void setIdKey(int idKey) {
        this.idKey = idKey;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }
}

