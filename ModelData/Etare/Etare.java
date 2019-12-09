package e.kiosque.appca.ModelData.Etare;

import android.graphics.Point;

import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

public class Etare {
    private String nom;
    private String numero_pla;
    private Double xPourGPS;
    private Double yPourGPS;
    private Integer id_key;
    private String nPlanER;
    private String commune;
    private int insee;
    private String adress_ad;
    private String httpEtare;
    private Point geometry;

    public void setId_key(Integer id_key) {
        this.id_key = id_key;
    }

    public String getnPlanER() {
        return nPlanER;
    }

    public void setnPlanER(String nPlanER) {
        this.nPlanER = nPlanER;
    }

    public Etare(String nom, Double xPourGPS, Double yPourGPS, int id_key, String nPlanER, String commune, int insee, String adress_ad, String httpEtare, Point geometry) {
        this.nom = nom;
        this.xPourGPS = xPourGPS;
        this.yPourGPS = yPourGPS;
        this.id_key = id_key;
        this.nPlanER = nPlanER;

        this.commune = commune;
        this.insee = insee;
        this.adress_ad = adress_ad;
        this.httpEtare = httpEtare;
        this.geometry = geometry;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero_pla() {
        return numero_pla;
    }

    public void setNumero_pla(String numero_pla) {
        this.numero_pla = numero_pla;
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

    public int getId_key() {
        return id_key;
    }

    public void setId_key(int id_key) {
        this.id_key = id_key;
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

    public String getAdress_ad() {
        return adress_ad;
    }

    public void setAdress_ad(String adress_ad) {
        this.adress_ad = adress_ad;
    }

    public String getHttpEtare() {
        return httpEtare;
    }

    public void setHttpEtare(String httpEtare) {
        this.httpEtare = httpEtare;
    }

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
