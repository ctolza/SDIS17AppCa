package e.kiosque.appca.ModelData.InaccessibleFptEpa;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

public class InaccessibleFptEpa {
    private String nom;
    private Double xPourGPS;
    private Double yPourGPS;
    private Point geometry;

    public InaccessibleFptEpa(String nom, Double xPourGPS, Double yPourGPS, Point geometry) {
        this.nom = nom;
        this.xPourGPS = xPourGPS;
        this.yPourGPS = yPourGPS;
        this.geometry = geometry;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getxPourGPS() {
        return xPourGPS;
    }

    public void setxPourGPS(Double xPourGPS) {
        this.xPourGPS = xPourGPS;
    }

    public Double getyPourGPS() {
        return yPourGPS;
    }

    public void setyPourGPS(Double yPourGPS) {
        this.yPourGPS = yPourGPS;
    }

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
