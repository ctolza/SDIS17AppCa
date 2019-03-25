package e.kiosque.appca.ModelData.SNCF;

import com.mapbox.geojson.Point;

public class SNCF {
    private String sousType;
    private String nom;
    private String commune;
    private int insee;
    private int idKey;

    private Point geometry;

    public SNCF(String sousType, String nom, String commune, int insee, int idKey, Point geometry) {
        this.sousType = sousType;
        this.nom = nom;
        this.commune = commune;
        this.insee = insee;
        this.idKey = idKey;
        this.geometry = geometry;
    }

    public String getSousType() {
        return sousType;
    }

    public void setSousType(String sousType) {
        this.sousType = sousType;
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
