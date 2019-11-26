package e.kiosque.appca;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;
import e.kiosque.appca.Lambert_conversion.LambertPoint;
import e.kiosque.appca.Scales.ScaledMapview;
import e.kiosque.appca.Scales.ScaledMapview.ScaleUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.View.inflate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslateAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

//Route calculation import
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>, Callback<DirectionsResponse>, MapboxMap.OnMapClickListener {

    private static final double ZOOM_THRESHOLD_LINE_LAYER = 13;
    private static final double ZOOM_THRESHOLD_SYMBOL_LAYER = 12;
    private static final double ZOOM_THRESHOLD_AREA_LAYER = 15;

    //Variable d'échelle
    private TextView scaleText;
    private ScaleUnit scaleUnit = ScaleUnit.KM;
    private float labelWidth = 0.33f;

    //Variable de la classe Map
    private MapView mapView;
    private MapboxMap map;

    private boolean isEndNotified;
    //Permission manager for location
    private PermissionsManager permissionsManager;

    //location engine attribute
    private LocationEngine locationEngine;
    private LocationComponent locationComponent;
    private LocationEngineRequest request;

    //Nav Route Attribute
    private NavigationMapRoute navigationMapRoute;
    private DirectionsRoute currentRoute;
    private List<DirectionsRoute> routes = new ArrayList<>();

    //Location attribute
    private Location actualLocation;
    private Point originPositionPoint;
    private Point destinationPosition;
    private Marker destinationMarker;

    //Data view Source
    private Source sourceEtare;
    private Source sourceER;
    private Source sourcePeiTot;
    private Source sourcePisteCyclable;
    private Source sourceGazDetendeur;
    private Source sourceGazLine;
    private Source sourceEdfTransfo;
    private Source sourceEdfLine;
    private Source sourceForestLine;
    private Source sourcePKSncf;
    private Source sourcePKCyclable;
    private Source sourceContraintesVehicules;

    //Switch
    private Switch switchLigneEdf;
    private Switch switchTransfoEdf;
    private Switch switchLigneGRDF;
    private Switch switchDetendeurGRDF;

    //Layers
    private SymbolLayer pkCyclableSymbolLayer;
    private SymbolLayer pkSncfSymbolLayer;
    private SymbolLayer contraintesVehiculesSymbolLayer;
    private LineLayer lineGazLayer;
    private LineLayer lineEdfLayer;
    private LineLayer lineCyclePiste;
    private LineLayer linePisteForestLayer;
    private SymbolLayer transfoEdfSymbolLayer;
    private SymbolLayer etareSymbolLayer;
    private SymbolLayer erSymbolLayer;
    private SymbolLayer gazDetendeurSymbolLayer;
    private FillLayer empriseEtareAreaLayer;
    private FillLayer empriseERAreaLayer;

    //Cluster's Variables
    private SymbolLayer peiUnclSymbolLayer;
    private SymbolLayer countClusteredPEI;
    private CircleLayer circlesClusteredPEI;

    // Route
    private Boolean routeIsDisplayed = true;
    private Boolean markerSelected = false;


    //Binding View
    @BindView(R.id.recentrageDestination)
    FloatingActionButton recenterInter;

    @BindView(R.id.btn_recentrageUser)
    FloatingActionButton recenterUser;

    Boolean isFilterOpen = false;

    @BindView(R.id.btn_filter)
    FloatingActionButton buttonFilter;

    @BindView(R.id.btn_normal_map)
    FloatingActionButton buttonNormalMap;

    @BindView(R.id.btn_ortho_photo_map)
    FloatingActionButton buttonOrthoPhotoMap;

    @BindView(R.id.btn_forest_map)
    FloatingActionButton buttonForestMap;
    //Progress Bar
    @BindView(R.id.progressData)
    ProgressBar progressBar;

    @BindView(R.id.text_loading)
    TextView textLoading;

    private boolean lineForestLayerIsActive = false;

    //Switch des paramètres
    private boolean lineEdfIsActive = false;
    private boolean transfoEdfIsActive = false;
    private boolean lineGdfIsActive = false;
    private boolean detendeurGdfIsActive = false;

    Dialog parameterDialog;

    private static final String DIRECTIONS_RESPONSE = "directions-route.json";

    public static final String DESTINATION_EXTRA_KEY = "DESTINATION_EXTRA_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mapbox key
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        //Creating map..
        //startProgress();
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //get SDIS Position
        originPositionPoint = Point.fromLngLat(-1.11568, 46.156259);
        mapView.getMapAsync(this);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.map = mapboxMap;
        this.map.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style)
            {
                enableLocation();
                setDestinationPoint();
                setMissionMarker();
                setNavigationRouteObject();
                generateRoute(originPositionPoint, destinationPosition);
                initSourceData();
                initDefaultInformationLayer();
                setCameraPosition(destinationPosition);
                map.addOnMapClickListener(MapActivity.this);
                //Stop Progressbar when map is loaded.

                progressBar.setVisibility(View.INVISIBLE);
                textLoading.setVisibility(View.INVISIBLE);
            }
        });
        map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {

            @Override
            public boolean onMapClick(@NonNull LatLng point) {

                if (map.getStyle() != null) {
                    final SymbolLayer selectedSymbolLayer = (SymbolLayer) map.getStyle().getLayer("selected-marker-layer");
                    PointF screenPoint = map.getProjection().toScreenLocation(point);
                    List<Feature> features = map.queryRenderedFeatures(screenPoint, "unclusteredPei");
                    List<Feature> selectedFeature = map.queryRenderedFeatures(screenPoint, "selected-marker-layer");

                    //si on clique sur quelque chose deja
                    if (selectedFeature.size() > 0 && markerSelected) {
                        return false;
                    }
                    //en cas de clic sur le marker déjà selectionné on a une deselection
                    if (features.isEmpty()) {
                        if (markerSelected) {
                            deselectLayer(selectedSymbolLayer);
                        }
                        return false;
                    }

                    //Liaison entre la source existante des peis avec la source du pei selectionné
                    GeoJsonSource source = map.getStyle().getSourceAs("selected-marker");
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromGeometry(features.get(0).geometry())}));
                    }
                    //Si déjà selectionné, on le déselectionne
                    if (markerSelected) {
                        deselectLayer(selectedSymbolLayer);
                    }

                    //Selection du Layer
                    if (features.size() > 0) {
                        String debit = "";
                        String pression = "";
                        String title = features.get(0).getStringProperty("autrenom");
                        try {
                            if (features.get(0).getStringProperty("debit_1b") != null) {
                                debit = features.get(0).getStringProperty("debit_1b");
                            }

                            if (features.get(0).getStringProperty("pression") != null) {
                                pression = features.get(0).getStringProperty("pression");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MapActivity.this, title + " débit : " + debit + " pression : " + pression, Toast.LENGTH_LONG).show();
                        selectLayer(selectedSymbolLayer);
                    }
                }
                return true;
            }
        });
        map.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if (scaleText == null) {
                    ViewParent v = (ViewParent) getParent();
                    if (v instanceof FrameLayout) {
                        scaleText = (TextView) findViewById(R.id.scale_text);
                        ((FrameLayout) v).addView(scaleText);
                    }
                }
                if (scaleText != null) {
// compute the horizontal span in metres of the bottom of the map

                }

                //Log.e("ZOOM LVL : " , "" + map.getCameraPosition().zoom);
                if (map.getCameraPosition().zoom > 12) {
                    if (peiUnclSymbolLayer != null) {
                        peiUnclSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                } else {
                    peiUnclSymbolLayer.setProperties(visibility(NONE));
                }
                if (map.getCameraPosition().zoom > 14) {
                    if (erSymbolLayer != null) {
                        erSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (contraintesVehiculesSymbolLayer != null) {
                        contraintesVehiculesSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (etareSymbolLayer != null) {
                        etareSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (pkSncfSymbolLayer != null) {
                        pkSncfSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (pkCyclableSymbolLayer != null) {
                        pkCyclableSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (detendeurGdfIsActive) {
                        gazDetendeurSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                    if (transfoEdfIsActive) {
                        transfoEdfSymbolLayer.setProperties(visibility(VISIBLE));
                    }
                } else {
                    if (etareSymbolLayer != null) {
                        etareSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (erSymbolLayer != null) {
                        erSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (contraintesVehiculesSymbolLayer != null) {
                        contraintesVehiculesSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (pkSncfSymbolLayer != null) {
                        pkSncfSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (pkCyclableSymbolLayer != null) {
                        pkCyclableSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (detendeurGdfIsActive) {
                        gazDetendeurSymbolLayer.setProperties(visibility(NONE));
                    }
                    if (transfoEdfIsActive) {
                        transfoEdfSymbolLayer.setProperties(visibility(NONE));
                    }
                }
            }
        });

    }

    //Méthode quand Layer selectionné
    private void selectLayer(final SymbolLayer iconLayer) {
        ValueAnimator markerAnimator = new ValueAnimator();
        markerAnimator.setObjectValues(1f, 2f);
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iconLayer.setProperties(
                        PropertyFactory.iconSize((float) animation.getAnimatedValue())
                );
            }
        });
        markerAnimator.start();
        markerSelected = true;
    }


    //Méthode quand Layer déselectionné
    private void deselectLayer(final SymbolLayer iconLayer) {
        ValueAnimator markerAnimator = new ValueAnimator();
        markerAnimator.setObjectValues(2f, 1f);
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iconLayer.setProperties(
                        PropertyFactory.iconSize((float) animation.getAnimatedValue())
                );
            }
        });
        markerAnimator.start();
        markerSelected = false;
    }


    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates(request, this, getMainLooper());
        }
        if (locationComponent != null) {
            locationComponent.onStart();
        }
        mapView.onStart();
    }

    ////////////////////////////////////////////////////////////////////POIS METHOD////////////////////////////////////////////////////////////////////

    /**
     * Alls Layers initialisation
     */
    private void initSourceData() {
        initSourceERSurface();
        initSourceEtareSurface();
        initSourceAndLayerEdf();
        initSourceAndLayerEdfLine();
        initSourceAndLayerPisteCyclable();
        initSourceAndLayerGazDetendeur();
        initSourceAndLayerGazLine();
        initSourceAndLayerFptPoint();
        initSourceAndLayerPKSncf();
        initSourceAndLayerPisteForestiere();
        initSourceAndLayerPKCyclable();
        initSourceAndLayerSymbolER();
        initSourceAndLayerETARE();
        initSourcePeiPoint();
        initLayerIcons();
        addClusteredPei();
        map.getStyle().addSource(new GeoJsonSource("selected-marker"));
        map.getStyle().addLayer(new SymbolLayer("selected-marker-layer", "selected-marker")
                .withProperties(PropertyFactory.iconImage("{ty_start}"),
                        iconOffset(new Float[]{0f, -9f})));

    }

    private void initDefaultInformationLayer() {
        lineGazLayer.setProperties(visibility(NONE));
        lineEdfLayer.setProperties(visibility(NONE));
        lineCyclePiste.setProperties(visibility(VISIBLE));
        linePisteForestLayer.setProperties(visibility(NONE));
        transfoEdfSymbolLayer.setProperties(visibility(NONE));
        gazDetendeurSymbolLayer.setProperties(visibility(NONE));
        lineForestLayerIsActive = false;
    }

    private void initDefaultForestLayer() {
        initDefaultInformationLayer();
        linePisteForestLayer.setProperties(visibility(VISIBLE));
        lineForestLayerIsActive = true;
    }


    /**
     * Initialisation Source/Layer Pei
     */
    private void initSourcePeiPoint() {
        String geoJsonPei = GeoJsonMngr.loadGeoJson(this, R.raw.pei);
        sourcePeiTot = new GeoJsonSource("peis", geoJsonPei, new GeoJsonOptions()
                .withCluster(true)
                .withClusterMaxZoom(16)
                .withClusterRadius(50)
        );
    }

    /**
     * Contraintes véhicules Source and Layer initialisation
     */
    private void initSourceAndLayerFptPoint() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.inaccessible_fpt_epa);
        sourceContraintesVehicules = new GeoJsonSource("contraintesVehiculesJson", geoJsonString);

        map.getStyle().addSource(sourceContraintesVehicules);
        contraintesVehiculesSymbolLayer = new SymbolLayer("Contraintes Vehicules", "contraintesVehiculesJson");
        contraintesVehiculesSymbolLayer.setProperties(iconImage("contraintes-vehicules"), visibility(NONE));
        map.getStyle().addLayer(contraintesVehiculesSymbolLayer);

    }

    /**
     * PK SNCF Source and Layer initialisation
     */
    private void initSourceAndLayerPKSncf() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.sncf);
        sourcePKSncf = new GeoJsonSource("pkSncf", geoJsonString);

        map.getStyle().addSource(sourcePKSncf);
        pkSncfSymbolLayer = new SymbolLayer("PK SNCF", "pkSncf");
        pkSncfSymbolLayer.setProperties(iconImage("pk-sncf"), visibility(NONE));
        map.getStyle().addLayer(pkSncfSymbolLayer);
    }

    private void initSourceAndLayerPKCyclable() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.jalon_piste_cycle);
        sourcePKCyclable = new GeoJsonSource("pkCyclable", geoJsonString);

        map.getStyle().addSource(sourcePKCyclable);
        pkCyclableSymbolLayer = new SymbolLayer("PK Cyclable", "pkCyclable");
        pkCyclableSymbolLayer.setProperties(iconImage("pk-cyclable"), visibility(NONE));
        map.getStyle().addLayer(pkCyclableSymbolLayer);
    }

    /**
     * Piste Cyclable Source and Layer initialisation
     */
    private void initSourceAndLayerPisteCyclable() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.piste_cycle);
        sourcePisteCyclable = new GeoJsonSource("pisteCyclable", geoJsonString);

        map.getStyle().addSource(sourcePisteCyclable);
        lineCyclePiste = new LineLayer("Piste Cyclable", "pisteCyclable");
        lineCyclePiste.setProperties(
                lineColor(getResources().getColor(R.color.pisteCyclable)),
                visibility(NONE)
        );
        map.getStyle().addLayer(lineCyclePiste);
    }


    /**
     * Gaz Detendeur Source and Layer initialisation
     */
    private void initSourceAndLayerGazDetendeur() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.poste_detente_gaz);
        sourceGazDetendeur = new GeoJsonSource("posteGazJson", geoJsonString);
        map.getStyle().addSource(sourceGazDetendeur);

        gazDetendeurSymbolLayer = new SymbolLayer("Poste Detendeur Gaz", "posteGazJson");
        gazDetendeurSymbolLayer.setProperties(iconImage("gaz-detendeur"), visibility(NONE));
        map.getStyle().addLayer(gazDetendeurSymbolLayer);
    }

    /**
     * Gaz Line Source and Layer initialisation
     */
    private void initSourceAndLayerGazLine() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.transport_gaz);
        sourceGazLine = new GeoJsonSource("lineGazJson", geoJsonString);

        map.getStyle().addSource(sourceGazLine);
        lineGazLayer = new LineLayer("Line Gaz", "lineGazJson");
        map.getStyle().addLayer(lineGazLayer);

        lineGazLayer.setProperties(lineColor(getResources().getColor(R.color.ligneGaz)),
                lineWidth(4.5f),
                visibility(NONE));
    }

    /**
     * Etare Surface Source and Layer Initialisation
     */
    private void initSourceEtareSurface() {
        String geoJsonEmpriseEtare = GeoJsonMngr.loadGeoJson(this, R.raw.etare_v2);

        Source sourceEmpriseEtare = new GeoJsonSource("sourceEmpriseEtare", geoJsonEmpriseEtare, new GeoJsonOptions()
                .withClusterMaxZoom(14));
        empriseEtareAreaLayer = new FillLayer("layerEmpriseEtare", "sourceEmpriseEtare");

        map.getStyle().addSource(sourceEmpriseEtare);
        map.getStyle().addLayer(empriseEtareAreaLayer);

        empriseEtareAreaLayer = (FillLayer) map.getStyle().getLayer("layerEmpriseEtare");
        empriseEtareAreaLayer.setProperties(fillColor(Color.rgb(255, 32, 32)),
                fillOpacity((float) 0.5),
                visibility(VISIBLE));
    }

    private void initSourceERSurface() {
        String geoJsonEmpriseER = GeoJsonMngr.loadGeoJson(this, R.raw.etablissement_v2);
        Source sourceER = new GeoJsonSource("sourceEmpriseER", geoJsonEmpriseER);

        empriseERAreaLayer = new FillLayer("layerEmpriseER", "sourceEmpriseER");

        map.getStyle().addSource(sourceER);
        map.getStyle().addLayer(empriseERAreaLayer);
        empriseERAreaLayer = (FillLayer) map.getStyle().getLayer("layerEmpriseER");
        empriseERAreaLayer = (FillLayer) map.getStyle().getLayer("layerEmpriseER");
        empriseERAreaLayer.withProperties(
                fillColor(
                        match(get("SOUS-TYPE"),
                                color(getResources().getColor(R.color.defaultERcolor)),
                                stop("Habitations", color(getResources().getColor(R.color.habitationERcolor))
                                )
                        )),
                fillOpacity((float) 0.5),
                visibility(VISIBLE));
    }

    private void initSourceAndLayerSymbolER() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.etablissement_v2);
        sourceER = new GeoJsonSource("sourceER", geoJsonString);

        map.getStyle().addSource(sourceER);
        erSymbolLayer = new SymbolLayer("erSymbolLayer", "sourceER");
        erSymbolLayer.withProperties(PropertyFactory.iconImage("{SOUS-TYPE}"), visibility(NONE));

        map.getStyle().addLayer(erSymbolLayer);
    }

    private void initSourceAndLayerETARE() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.etare_v2);
        sourceEtare = new GeoJsonSource("sourceETARE", geoJsonString);
        map.getStyle().addSource(sourceEtare);
        etareSymbolLayer = new SymbolLayer("etareSymbolLayer", "sourceETARE");
        etareSymbolLayer.setProperties(visibility(NONE));
        etareSymbolLayer.withProperties(PropertyFactory.iconImage("Etare"));

        map.getStyle().addLayer(etareSymbolLayer);
    }

    /**
     * Edf Line Source and Layer initialisation
     */
    private void initSourceAndLayerEdfLine() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.ligne_ht_edf_lineaire);
        sourceEdfLine = new GeoJsonSource("ligneHtJson", geoJsonString);
        map.getStyle().addSource(sourceEdfLine);
        lineEdfLayer = new LineLayer("Line HT ERDF", "ligneHtJson");
        map.getStyle().addLayer(lineEdfLayer);
        lineEdfLayer.setProperties(lineColor(getResources().getColor(R.color.ligneEdf)), visibility(NONE));
    }

    private void initSourceAndLayerPisteForestiere() {

        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.pistes_forestieres);
        sourceForestLine = new GeoJsonSource("forestLineJson", geoJsonString);

        map.getStyle().addSource(sourceForestLine);
        linePisteForestLayer = new LineLayer("Forest Line", "forestLineJson");
        map.getStyle().addLayer(linePisteForestLayer);
        linePisteForestLayer.setProperties(
                lineColor(getResources().getColor(R.color.pisteForest)),
                lineWidth(4.5f),
                visibility(NONE));
    }

    /**
     * Edf Transfo Source and Layer initialisation
     */
    private void initSourceAndLayerEdf() {

        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.poste_distribution_edf);
        sourceEdfTransfo = new GeoJsonSource("posteDistriEdf", geoJsonString);

        map.getStyle().addSource(sourceEdfTransfo);
        transfoEdfSymbolLayer = new SymbolLayer("Transfo Edf", "posteDistriEdf");

        map.getStyle().addLayer(transfoEdfSymbolLayer);
        transfoEdfSymbolLayer.setProperties(iconImage("edf-transfo"), visibility(NONE));
    }

    /**
     * Icon layer initialisation
     */
    private void initLayerIcons() {
        map.getStyle().addImage("contraintes-vehicules", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_contraintes_vehicules)));

        map.getStyle().addImage("pk-cyclable", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pk_pistecyclable)));

        map.getStyle().addImage("pk-SNCF", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pk_sncf)));

        map.getStyle().addImage("gaz-detendeur", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_detendeur_gaz)));

        map.getStyle().addImage("edf-transfo", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_transfo_edf)));

        map.getStyle().addImage("Aire d'aspiration permanente", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_aire_asp)
        ));
        map.getStyle().addImage("Aire d'aspiration variable", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_aire_asp_var)
        ));
        map.getStyle().addImage("Reserve en eau non alimentée", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_citerne_nonalim)
        ));
        map.getStyle().addImage("PI 80", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_80_dis)
        ));
        map.getStyle().addImage("PI 100", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_100_dis)
        ));
        map.getStyle().addImage("PI 150", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_150_dis)
        ));
        map.getStyle().addImage("Poteau relais en aspiration", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_relais_asp_dis)
        ));
        map.getStyle().addImage("Poteau relais en refoulement", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pei_relais_ref_dis)
        ));
        map.getStyle().addImage("Puisard", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_puisard_dis)
        ));

        map.getStyle().addImage("Administrations", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_administration)
        ));
        map.getStyle().addImage("Etablissements sanitaires", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_etablissements_sanitaires)
        ));
        map.getStyle().addImage("Etablissements de culte", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_lieu_de_culte)
        ));
        map.getStyle().addImage("Etablissements militaires", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_militaire)
        ));
        map.getStyle().addImage("Patrimoine", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_patrimoine)
        ));
        map.getStyle().addImage("Gare SNCF voyageurs", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_gare)
        ));
        map.getStyle().addImage("Gare routière", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_gare)
        ));
        map.getStyle().addImage("Gare SNCF de frêt", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_gare_fret)
        ));
        map.getStyle().addImage("Campings", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_camping)
        ));
        map.getStyle().addImage("Emprise établissement enseignement", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_enseignement)
        ));
        map.getStyle().addImage("Etablissments d enseignements", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_enseignement)
        ));
        map.getStyle().addImage("Bases de loisirs", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_base_loisirs)
        ));
        map.getStyle().addImage("ERP", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_erp)
        ));
        map.getStyle().addImage("Centres commerciaux", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_centre_commerciaux)
        ));
        map.getStyle().addImage("Complexes sportifs", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_complexe_sportif)
        ));

        map.getStyle().addImage("Etare", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_etare)
        ));

    }

    /**
     * Cluster Peis
     */
    private void addClusteredPei() {
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        map.getStyle().addSource(sourcePeiTot);

        int[][] layers = new int[][]{
                new int[]{150, ContextCompat.getColor(this, R.color.peiCluster)},
                new int[]{20, ContextCompat.getColor(this, R.color.peiCluster)},
                new int[]{0, ContextCompat.getColor(this, R.color.peiCluster)}
        };
        //Creating a SymbolLayer icon layer for single data/icon points
        peiUnclSymbolLayer = new SymbolLayer("unclusteredPei", "peis");
        peiUnclSymbolLayer.withProperties(
                PropertyFactory.iconImage("{ty_start}"),
                iconAllowOverlap(true),
                iconOffset(new Float[]{-2f, -25f}),
                textField("{autrenom}")
        );

        map.getStyle().addLayer(peiUnclSymbolLayer);


        for (int i = 0; i < layers.length; i++) {
            //Add clusters' circlesClusteredPEI
            circlesClusteredPEI = new CircleLayer("clusterPei-" + i, "peis");
            circlesClusteredPEI.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );

            Expression pointCount = toNumber(get("point_count"));

            // Add a filter to the cluster layer that hides the circlesClusteredPEI based on "point_count"
            circlesClusteredPEI.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has("point_count"),
                            gt(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            map.getStyle().addLayer(circlesClusteredPEI);
        }

        //Add a SymbolLayer for the cluster data number point countClusteredPEI
        countClusteredPEI = new SymbolLayer("countPei", "peis");
        countClusteredPEI.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(14f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        map.getStyle().addLayer(countClusteredPEI);
    }


    ////////////////////////////////////////////////////////////////////NAVIGATION METHOD////////////////////////////////////////////////////////////////////


    //////LOCATION PERMISSION//////
    @SuppressWarnings({"MissingPermission"})
    /**
     * Initialize the Location Permission
     */
    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationComponent();
            initializeLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Initialize the LocationEngine.
     */
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        locationEngine.getLastLocation(this);

        request = new
                LocationEngineRequest.Builder(1000L)
                .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                .setMaxWaitTime(1000L * 5).build();
        locationEngine.requestLocationUpdates(request, this, getMainLooper());
    }

    /**
     * Initialize the locationComponent
     */
    @SuppressWarnings("MissingPermission")
    private void initializeLocationComponent() {
        //Get instance of the component
        locationComponent = map.getLocationComponent();

        //ACTIVATE WITH OPTION
        locationComponent.activateLocationComponent(this, map.getStyle());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set component camera mode et style
        //locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.NORMAL);
    }
    ///////SET COORDINATES LOCATION//////

    /**
     * Intervention place manager
     */
    private void setDestinationPoint() {
        //Ajout de la destination depuis la page d'accueil :
        LambertPoint dest = (LambertPoint) getIntent().getSerializableExtra(DESTINATION_EXTRA_KEY);
        destinationPosition = Point.fromLngLat(dest.getX(), dest.getY());
    }

    /**
     * set Marker for Intervention place
     */
    private void setMissionMarker() {
        IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
        com.mapbox.mapboxsdk.annotations.Icon fireIcon = iconFactory.fromResource((R.drawable.ic_fire));
        destinationMarker = map.addMarker(new MarkerOptions().position(new LatLng(destinationPosition.latitude(), destinationPosition.longitude())).icon(fireIcon));
    }

    /**
     * Paramètre le point d'origine pour le calcul de l'itinéraire
     */
    private void setOriginePoint() {
        originPositionPoint = Point.fromLngLat(actualLocation.getLongitude(), actualLocation.getLatitude());
    }

    /**
     * Navigation Method
     */
    private void setNavigationRouteObject() {
        navigationMapRoute = new NavigationMapRoute(null, mapView, map,
                "admin-3-4-boundaries-bg");
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(DirectionsAdapterFactory.create())
                .create();
        DirectionsResponse response = gson.fromJson(loadJsonFromAsset(DIRECTIONS_RESPONSE),
                DirectionsResponse.class);
        if (response != null) {
            navigationMapRoute.addRoute(response.routes().get(0));
        }

    }

    /**
     * Location engine Callback
     *
     * @param result
     */
    @Override
    public void onSuccess(LocationEngineResult result) {
        actualLocation = result.getLastLocation();
        setOriginePoint();
    }

    /**
     * On failure location engine callback
     *
     * @param exception
     */
    @Override
    public void onFailure(@NonNull Exception exception) {

    }

    //Navigation Json recovery
    private String loadJsonFromAsset(String filename) {
        // Using this method to load in GeoJSON files from the assets folder.

        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////ROUTE GENERATION METHOD////////////////////////////////////////////////////////////////////

    //Route Generation
    private void generateRoute(Point origin, Point destination) {
        MapboxDirections directions = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .accessToken(Mapbox.getAccessToken())
                .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .annotations(DirectionsCriteria.ANNOTATION_CONGESTION)
                .alternatives(true)
                .steps(true)
                .build();
        directions.enqueueCall(this);
    }

    /**
     * Add Route in navigationMapRoute
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse
    (Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        Log.e("Generate Route", "on reponse route ok ");
        if (response.body() != null && !response.body().routes().isEmpty()) {

            List<DirectionsRoute> routes = response.body().routes();
            this.routes = routes;
            navigationMapRoute.addRoute(routes.get(0));
        }
    }

    // Affiche la route ou non en fonction du clic sur le bouton d'affichage du trajet + recentrage
    @OnClick(R.id.btn_affichageTrajet)
    public void displayRoute() {
        if (routeIsDisplayed == true) {
            routeIsDisplayed = false;
            navigationMapRoute.updateRouteVisibilityTo(false);
            navigationMapRoute.updateRouteArrowVisibilityTo(false);
        } else {
            routeIsDisplayed = true;
            navigationMapRoute.updateRouteVisibilityTo(true);
            navigationMapRoute.updateRouteArrowVisibilityTo(true);
            zoomOnOriginAndDestination();
        }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
        Timber.e(t);
    }

    ////////////////////////////////////////////////////////////////////FILTER METHOD////////////////////////////////////////////////////////////////////

    //Creation du menu pour filtrage
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_pois, menu);
        return true;
    }

    //Selection du filtre
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_parameter:
                showParameterDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showParameterDialog() {

        parameterDialog = new Dialog(MapActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.parameter_dialog, null);

        parameterDialog.setContentView(dialogView);
        parameterDialog.create();
        Button validationParameter = dialogView.findViewById(R.id.btn_validation_parametre);
        Button cancelParameter = dialogView.findViewById(R.id.btn_cancel_parametre);

        switchLigneEdf = dialogView.findViewById(R.id.switch_ligne_edf);
        switchTransfoEdf = dialogView.findViewById(R.id.switch_Tranfo_edf);
        switchLigneGRDF = dialogView.findViewById(R.id.switch_ligne_grdf);
        switchDetendeurGRDF = dialogView.findViewById(R.id.switch_detendeur_grdf);

        switchLigneEdf.setChecked(lineEdfIsActive);
        switchTransfoEdf.setChecked(transfoEdfIsActive);
        switchLigneGRDF.setChecked(lineGdfIsActive);
        switchDetendeurGRDF.setChecked(detendeurGdfIsActive);

        validationParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLayer(lineEdfLayer, switchLigneEdf);
                toggleLayer(transfoEdfSymbolLayer, switchTransfoEdf);
                toggleLayer(lineGazLayer, switchLigneGRDF);
                toggleLayer(gazDetendeurSymbolLayer, switchDetendeurGRDF);

                lineEdfIsActive = switchLigneEdf.isChecked();
                transfoEdfIsActive = switchTransfoEdf.isChecked();
                lineGdfIsActive = switchLigneGRDF.isChecked();
                detendeurGdfIsActive = switchDetendeurGRDF.isChecked();

                parameterDialog.dismiss();
            }
        });

        cancelParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterDialog.cancel();
            }
        });
        parameterDialog.show();
    }

    //Methode gestion Filtre
    private void toggleLayer(Layer layer, Switch switchToCheck) {
        if (layer != null) {
            if (switchToCheck.isChecked()) {
                layer.setProperties(visibility(VISIBLE));
            } else {
                layer.setProperties(visibility(NONE));
            }
        }
    }


    ////////////////////////////////////////////////////////////////////LAYER MAP STYLE METHOD////////////////////////////////////////////////////////////////////

    /**
     * Layer Open method
     */
    @OnClick(R.id.btn_filter)
    public void filterMenu() {
        if (!isFilterOpen) {
            showLayerMenu();
        } else {
            closeLayerMenu();
        }
    }

    //Show Layer method
    private void showLayerMenu() {
        isFilterOpen = true;
        buttonNormalMap.animate().translationY((buttonNormalMap.getHeight()) + 5);
        buttonOrthoPhotoMap.animate().translationY((buttonOrthoPhotoMap.getHeight() * 2) + 10);
        buttonForestMap.animate().translationY((buttonForestMap.getHeight() * 3) + 15);
    }

    //Close Layer method
    private void closeLayerMenu() {
        isFilterOpen = false;
        buttonNormalMap.animate().translationY(0);
        buttonOrthoPhotoMap.animate().translationY(0);
        buttonForestMap.animate().translationY(0);
    }

    // Map Street select
    @OnClick(R.id.btn_normal_map)
    public void normalMapActivation() {
        if (progressBar.getVisibility() == View.INVISIBLE && textLoading.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            textLoading.setVisibility(View.VISIBLE);
        }
        map.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSourceData();
                initDefaultInformationLayer();

                map.addOnMapClickListener(MapActivity.this);
                progressBar.setVisibility(View.INVISIBLE);
                textLoading.setVisibility(View.INVISIBLE);

            }

        });
        closeLayerMenu();
    }

    // Map Outdoors select
    @OnClick(R.id.btn_forest_map)
    public void outDoorMapActivation() {
        if (progressBar.getVisibility() == View.INVISIBLE && textLoading.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            textLoading.setVisibility(View.VISIBLE);
        }
        this.map.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSourceData();
                initDefaultForestLayer();

                map.addOnMapClickListener(MapActivity.this);
                progressBar.setVisibility(View.INVISIBLE);
                textLoading.setVisibility(View.INVISIBLE);

            }
        });
        closeLayerMenu();
    }

    //Map ortho-photo select
    @OnClick(R.id.btn_ortho_photo_map)
    public void orthotPhotoMapActivation() {
        if (progressBar.getVisibility() == View.INVISIBLE && textLoading.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            textLoading.setVisibility(View.VISIBLE);
        }
        this.map.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSourceData();
                initDefaultInformationLayer();
                map.addOnMapClickListener(MapActivity.this);
                progressBar.setVisibility(View.INVISIBLE);
                textLoading.setVisibility(View.INVISIBLE);
            }
        });
        closeLayerMenu();
    }
    /////////////////////////////////////////////////////////////////SCALE METHOD/////////////////////////////////////////////////////////////////////////////////

    public enum ScaleUnit {
        MILE("mile", 1609.344f),
        NM("nm", 1852.0f),
        KM("km", 1000.0f);

        ScaleUnit(String unit, float ratio) {
            this.unit = unit;
            this.ratio = ratio;
        }

        String unit;
        float ratio;
    }

    ////////////////////////////////////////////////////////////////////CAMERA FOCUSING METHOD////////////////////////////////////////////////////////////////////

    /**
     * Zoom Method
     */
    private void zoomOnOriginAndDestination() {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(originPositionPoint.latitude(), originPositionPoint.longitude()))
                .include(new LatLng(destinationPosition.latitude(), destinationPosition.longitude()))
                .build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));

    }

    /**
     * set Camera Position
     *
     * @param location
     */
    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude())
                , 16), 2000);
    }

    private void setCameraPosition(Point pos) {
        Log.e("Camera position : ", pos.toString());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.latitude(), pos.longitude()), 16), 2000);
    }


    /**
     * Destination Focusing
     *
     * @param v
     */
    @OnClick(R.id.recentrageDestination)
    public void recentrageIntervention(View v) {
        setCameraPosition(destinationPosition);
    }

    /**
     * User Focusing
     *
     * @param v
     */
    @OnClick(R.id.btn_recentrageUser)
    public void recentrageUser(View v) {
        setCameraPosition(actualLocation);
    }
///////////////////////////////////////////////////////////////////////////////SCALE METHOD////////////////////////////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////MAPBOX METHOD DEFAULT////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Activez le GPS pour vous localiser sur la carte", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }
        if (locationComponent != null) {
            locationComponent.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }
        mapView.onDestroy();
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/* Deprecated since 7.0 no use of marker option with cluster

    // SHOW ETARE MARKER OLD VERSION
    private void afficheEtareMarker() {
        //Affichage Marker Etare sur Carte
        /*String geoJsonEtare = GeoJsonMngr.loadGeoJson(this,R.raw.etares);

        Source sourceEtare = new GeoJsonSource("sourceEtare",geoJsonEtare);
        etareSymbolLayer =(SymbolLayer) map.getLayer("layerEtare");
        etareSymbolLayer.setProperties(PropertyFactory.iconImage("ic_marker_etare"));
        map.addLayer(etareSymbolLayer);

        //etareMarker = marker.setPositionEtare(etareIcon, etareMgr);
        for (int i = 0; i < etareMgr.etaresLength(); i++) {
            Log.d("Geometry obj : ", ""+etareMgr.getEtareById(i).getGeometry().coordinates());
            etareMarker.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng(etareMgr.getEtareById(i).getGeometry().latitude(),  etareMgr.getEtareById(i).getGeometry().longitude()))
                    .title(etareMgr.getEtareById(i).getNom())
                    .snippet(etareMgr.getEtareById(i).getCommune())
                    .icon(etareIcon)
            ));
        }

    }
    // SHOW PEI MARKER OLD VERSION
    private void affichePeiMarker() {
        //Affichage Marker Point d'eau sur Carte
        for (int i = 0; i < peiMgr.peiLength(); i++) {
            peiMarker.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng( peiMgr.getPeiById(i).getGeometry().latitude(), peiMgr.getPeiById(i).getGeometry().longitude()))
                    .title("Ref : " + peiMgr.getPeiById(i).getAutreNom() + " Commune : " +peiMgr.getPeiById(i).getCommune())
                    .snippet("Volume : " + peiMgr.getPeiById(i).getVolume() + " Débit : " + peiMgr.getPeiById(i).getDebit1b())
                    .icon(peiIcon)
            ));
        }
        /*peiMarker = marker.setPositionPei(peiIcon, peiMgr);
        for (int i = 0; i < peiMarker.size(); i++) {
            map.addMarker(peiMarker.get(i));
        }

    }

 // SHOW VEHICULE CONSTRAINT MARKER OLD VERSION
    private void afficheFptMarker() {
        //Affichage Marker Fpt sur Carte
        for (int i = 0; i < fptMgr.fptLength(); i++) {
            fptMarker.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng(fptMgr.getInaccessibleFptEpaById(i).getGeometry().latitude(),fptMgr.getInaccessibleFptEpaById(i).getGeometry().longitude()))
                    .title("Ref : " + fptMgr.getInaccessibleFptEpaById(i).getNom())
                    .icon(fptIcon)
            ));
        }
    }

// SHOW GAZ MARKER OLD VERSION
    private void afficheGazMarker() {
        //Affichage Marker Gaz sur Carte
        gazMarker = marker.setPositionGaz(gazIcon, gazMgr);
        for (int i = 0; i < gazMarker.size(); i++) {
            map.addMarker(gazMarker.get(i));
        }
    }*/


/*   Deprecetated with SDK 7.x
//  MAPBOX DIRECTION BUILDER FOR NAVIGATIONROUTE OLD VERSION
        MapboxDirections.builder()
        NavigationRoute.builder(this)
                .accessToken(getString(R.string.access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e("Route calculation ", "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e("Route calculation ", "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e("Route calculation ", "Error: " + throwable.getMessage());
                    }
                });*/


/**
 * Deprecated with mapBox 7.x
 **/

// OLD VERSION LOCATION CHANGED
    /*
    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates(request, this, getMainLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            actualLocation = location;
            Log.e("onloc change loc: ", location.toString());
            Log.e("onloc actualloc : ", actualLocation.toString());

        }
    }*/


//DELETE METHOD XYNTHIA
 /*
    private void afficheXynthiaLayer() {
        String geoJsonXynthia = GeoJsonMngr.loadGeoJson(this, R.raw.xynthia);

        Source sourceXynthia = new GeoJsonSource("sourceXynthia", geoJsonXynthia);
        xynthia = new FillLayer("layerXynthia", "sourceXynthia");

        map.getStyle().addSource(sourceXynthia);
        map.getStyle().addLayer(xynthia);

        xynthia = (FillLayer) map.getStyle().getLayer("layerXynthia");
        xynthia.setProperties(fillColor(Color.rgb(0, 0, 255)));
        //xynthia.setProperties(rasterOpacity((float) 0.25));
    }
    */
// ICON FACTORY DECLARATION

/*
`

    private IconFactory iconFactory;
    private Icon etareIcon;
    private Icon gazIcon;
    private Icon fptIcon;
    private Icon batimentRemarquableIcon;

 */

/*
LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
                    float span[] = new float[1];
                    Location.distanceBetween(latLngBounds.getLatSouth(), latLngBounds.getLonEast(),
                            latLngBounds.getLatSouth(), latLngBounds.getLonWest(), span);

                    float totalWidth = span[0] / scaleUnit.ratio;
                    // calculate an initial guess at step size
                    float tempStep = totalWidth * labelWidth;

                    // get the magnitude of the step size
                    float mag = (float) Math.floor(Math.log10(tempStep));
                    float magPow = (float) Math.pow(10, mag);

                    // calculate most significant digit of the new step size
                    float magMsd = (int) (tempStep / magPow + 0.5);

                    // promote the MSD to either 1, 2, or 5
                    if (magMsd > 5.0f)
                        magMsd = 10.0f;
                    else if (magMsd > 2.0f)
                        magMsd = 5.0f;
                    else if (magMsd > 1.0f)
                        magMsd = 2.0f;
                    float length = magMsd * magPow;
                    if (length >= 1f)
                        scaleText.setText(String.format(Locale.US, "%.0f %s", length, scaleUnit.unit));
                    else
                        scaleText.setText(String.format(Locale.US, "%.2f %s", length, scaleUnit.unit));
                    // set the total width to the appropriate fraction of the display
                    //Width change
                    int width = Math.round(labelWidth * length / totalWidth);
                    FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    layoutParams.bottomMargin = 4;
                    scaleText.setLayoutParams(layoutParams);
 */
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

