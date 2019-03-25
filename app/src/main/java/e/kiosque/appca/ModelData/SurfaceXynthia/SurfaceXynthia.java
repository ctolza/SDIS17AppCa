package e.kiosque.appca.ModelData.SurfaceXynthia;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Polygon;

public class SurfaceXynthia {
    private String nom;
    private int id;
    private Polygon geometry;

    public SurfaceXynthia(String nom, int id, Polygon geometry) {
        this.nom = nom;
        this.id = id;
        this.geometry = geometry;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Polygon getGeometry() {
        return geometry;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }
}
