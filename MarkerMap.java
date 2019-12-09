/*package e.kiosque.appca;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

import e.kiosque.appca.Lambert_conversion.Lambert;
import e.kiosque.appca.ModelData.Etare.EtareManager;
import e.kiosque.appca.ModelData.InaccessibleFptEpa.InaccessibleFptEpaManager;
import e.kiosque.appca.ModelData.Pei.PeiManager;
import e.kiosque.appca.ModelData.PosteDetenteGaz.PosteDetenteGazManager;

public class MarkerMap {
    Lambert lambert = new Lambert();
    private List<Marker> listEtareMarker = new ArrayList<>();
    private List<MarkerOptions> listPeiMarker = new ArrayList<>();
    private List<MarkerOptions> listGazMarker = new ArrayList<>();
    private List<MarkerOptions> listFptMarker = new ArrayList<>();
    //Initialisation position marker
    public MarkerMap(){
    }
    public List<Marker> setPositionEtare(Icon icon, EtareManager etareMgr) {
        listEtareMarker.clear();
        for (int i = 0; i < etareMgr.etaresLength(); i++) {
            Position pos = (Position)etareMgr.getEtareById(i).getGeometry().getCoordinates();
            listEtareMarker.add( new Marker(new MarkerOptions()
                    .position(new LatLng(pos.getLatitude(), pos.getLongitude()))
                    .title(etareMgr.getEtareById(i).getNom())
                    .snippet(etareMgr.getEtareById(i).getCommune())
                    .icon(icon)
            ));
        }
        return listEtareMarker;
    }
    public List<MarkerOptions> setPositionPei(Icon icon, PeiManager peiManager) {
        listPeiMarker.clear();
        for (int i = 0; i < peiManager.peiLength(); i++) {
            Position pos = (Position) peiManager.getPeiById(i).getGeometry().getCoordinates();
            listPeiMarker.add(new MarkerOptions()
                    .position(new LatLng(pos.getLatitude(), pos.getLongitude()))
                    .title("Ref : " + peiManager.getPeiById(i).getAutreNom() + " Commune : " +peiManager.getPeiById(i).getCommune())
                    .snippet("Volume : " + peiManager.getPeiById(i).getVolume() + " Débit : " + peiManager.getPeiById(i).getDebit1b())
                    .icon(icon)
            );
        }
        return listPeiMarker;
    }
    public List<MarkerOptions> setPositionGaz(Icon icon, PosteDetenteGazManager gazManager) {
        listGazMarker.clear();
        for (int i = 0; i < gazManager.gazLength(); i++) {
            Position pos = (Position) gazManager.getPosteDetenteGazById(i).getGeometry().getCoordinates();
            listGazMarker.add(new MarkerOptions()
                    .position(new LatLng(pos.getLatitude(), pos.getLongitude()))
                    .title("Ref : " + gazManager.getPosteDetenteGazById(i).getNom() + " Commune : " +gazManager.getPosteDetenteGazById(i).getCommune())
                    .snippet("Insee :" + gazManager.getPosteDetenteGazById(i).getInsee())
                    .icon(icon)
            );
        }
        return listGazMarker;
    }

    public List<MarkerOptions> setPositionFpt(Icon icon, InaccessibleFptEpaManager fptManager) {
        listFptMarker.clear();
        for (int i = 0; i < fptManager.fptLength(); i++) {
            Position pos = (Position) fptManager.getInaccessibleFptEpaById(i).getGeometry().getCoordinates();
            listFptMarker.add(new MarkerOptions()
                    .position(new LatLng(pos.getLatitude(), pos.getLongitude()))
                    .title("Ref : " + fptManager.getInaccessibleFptEpaById(i).getNom())
                    .icon(icon)
            );
        }
        return listFptMarker;
    }
}

*/