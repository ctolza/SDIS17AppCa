package e.kiosque.appca.ModelData.Pei;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.mapbox.geojson.Geometry;

public class Pei {
    private String autreNom;
    private String commune;
    private Double debit1b;
    private String disponibilite;
    private String domaine;
    private Integer insee;
    private String nomComplet;
    private Double pression;
    private String volume;
    private String communeSt;
    private String tyStart;
    private Point geometry;

    public Pei(String autreNom, String commune, Double debit1b, String disponibilite, String domaine, Integer insee, String nomComplet, Double pression, String volume, String communeSt, String tyStart, Point geometry) {
        this.autreNom = autreNom;
        this.commune = commune;
        this.debit1b = debit1b;
        this.disponibilite = disponibilite;
        this.domaine = domaine;
        this.insee = insee;
        this.nomComplet = nomComplet;
        this.pression = pression;
        this.volume = volume;
        this.communeSt = communeSt;
        this.tyStart = tyStart;
        this.geometry = geometry;
    }

    public String getAutreNom() {
        return autreNom;
    }

    public void setAutreNom(String autreNom) {
        this.autreNom = autreNom;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public Double getDebit1b() {
        return debit1b;
    }

    public void setDebit1b(Double debit1b) {
        this.debit1b = debit1b;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public Integer getInsee() {
        return insee;
    }

    public void setInsee(Integer insee) {
        this.insee = insee;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public Double getPression() {
        return pression;
    }

    public void setPression(Double pression) {
        this.pression = pression;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCommuneSt() {
        return communeSt;
    }

    public void setCommuneSt(String communeSt) {
        this.communeSt = communeSt;
    }

    public String getTyStart() {
        return tyStart;
    }

    public void setTyStart(String tyStart) {
        this.tyStart = tyStart;
    }

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
