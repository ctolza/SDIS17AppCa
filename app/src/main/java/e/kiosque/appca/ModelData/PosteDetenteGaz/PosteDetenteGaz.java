package e.kiosque.appca.ModelData.PosteDetenteGaz;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

public class PosteDetenteGaz {
    private String nom;
    private String commune;
    private int insee;
    private int idKey;
    private Point geometry;

    public PosteDetenteGaz(String nom, String commune, int insee, int idKey, Point geometry) {
        this.nom = nom;
        this.commune = commune;
        this.insee = insee;
        this.idKey = idKey;
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

    public int getInsee() {
        return insee;
    }

    public void setInsee(int insee) {
        this.insee = insee;
    }

    public int getIdKey() {
        return idKey;
    }

    public void setIdKey(int idKey) {
        this.idKey = idKey;
    }

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
