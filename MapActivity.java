package e.kiosque.appca;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e.kiosque.appca.GeoJsonManager.GeoJsonMngr;
import e.kiosque.appca.Lambert_conversion.LambertPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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

    //Variable pour Historique dans la saisie d'itinéraire
    public static final String PREFS_NAME = "PingBusPrefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private SharedPreferences settings;
    private Set<String> history;

    //Variable d'échelle
    private TextView scaleText;
    //private ScaleUnit scaleUnit = ScaleUnit.KM;
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
    private Source sourceItin;
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

    private Dialog parameterDialog;
    private Dialog researchDialog;
    private Dialog autoCompleteDialog;

    private static final String DIRECTIONS_RESPONSE = "directions-route.json";

    public static final String DESTINATION_EXTRA_KEY = "DESTINATION_EXTRA_KEY";

    ////////////////////////////////////////////////////////////////////INITIALISATION DE LA MAP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        this.map.setStyle(Style.MAPBOX_STREETS, style -> {

            //Appels Méthodes Itinéraire
            enableLocation();
            setDestinationPoint();
            setNavigationRouteObject();
            generateRoute(originPositionPoint, destinationPosition);
            setMissionMarker();

            //Appels Méthodes Données
            initSourceData();
            initDefaultInformationLayer();

            setCameraPositionDestination(destinationPosition);
            map.addOnMapClickListener(MapActivity.this);
            //Stop Progressbar when map is loaded.

            progressBar.setVisibility(View.INVISIBLE);
            textLoading.setVisibility(View.INVISIBLE);
        });
        map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {

            @Override
            public boolean onMapClick(@NonNull LatLng point) {

                if (map.getStyle() != null) {
                    final SymbolLayer selectedSymbolLayer = (SymbolLayer) map.getStyle().getLayer("selected-marker-layer");
                    PointF screenPoint = map.getProjection().toScreenLocation(point);
                    RectF rectf = new RectF(screenPoint.x - 30, screenPoint.y - 30, screenPoint.x + 30, screenPoint.y + 30);

                    List<Feature> featuresPei = map.queryRenderedFeatures(rectf, "unclusteredPei");
                    List<Feature> featuresER = map.queryRenderedFeatures(rectf, "erSymbolLayer");
                    List<Feature> featuresEtare = map.queryRenderedFeatures(rectf, "etareSymbolLayer");
                    List<Feature> selectedFeature = map.queryRenderedFeatures(rectf, "selected-marker-layer");

                    //si on clique sur quelque chose deja
                    if (selectedFeature.size() > 0 && markerSelected) {
                        return false;
                    }
                    //en cas de clic sur le marker déjà selectionné on a une deselection
                    if (featuresPei.isEmpty()) {
                        if (markerSelected) {
                            deselectLayer(selectedSymbolLayer);
                        }
                        if (featuresER.isEmpty()) {
                            if (markerSelected) {
                                deselectLayer(selectedSymbolLayer);
                            }
                            if (featuresEtare.isEmpty()) {
                                if (markerSelected) {
                                    deselectLayer(selectedSymbolLayer);
                                }
                                return false;
                            }
                        }
                    }

                    //Liaison entre la source existante des peis avec la source du pei selectionné
                    GeoJsonSource source = map.getStyle().getSourceAs("selected-marker");
                    if (source != null && featuresPei.size() > 0) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromGeometry(featuresPei.get(0).geometry())}
                        ));
                    } else if (source != null && featuresER.size() > 0) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromGeometry(featuresER.get(0).geometry())}
                        ));
                    } else if (source != null && featuresEtare.size() > 0) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromGeometry(featuresEtare.get(0).geometry())}
                        ));
                    }
                    //Si déjà selectionné, on le déselectionne
                    if (markerSelected) {
                        deselectLayer(selectedSymbolLayer);
                    }


                    //Selection du Layer
                    if (featuresPei.size() > 0) {
                        String debit = "";
                        String pression = "";
                        String title = featuresPei.get(0).getStringProperty("autrenom");
                        try {
                            if (featuresPei.get(0).getStringProperty("debit_1b") != null) {
                                debit = featuresPei.get(0).getStringProperty("debit_1b");
                            }

                            if (featuresPei.get(0).getStringProperty("pression") != null) {
                                pression = featuresPei.get(0).getStringProperty("pression");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MapActivity.this, title + " \n débit : " + debit + " \n pression : " + pression, Toast.LENGTH_LONG).show();
                        selectLayer(selectedSymbolLayer);

                        // GESTION CLIC SUR BATIMENT ER
                    } else if (featuresER.size() > 0) {
                        String id = "";
                        String nom = featuresER.get(0).getStringProperty("NOM");
                        String adresse = featuresER.get(0).getStringProperty("ADRESSE");
                        try {
                            if (featuresER.get(0).getStringProperty("ID_KEY") != null) {
                                id = featuresER.get(0).getStringProperty("ID_KEY");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MapActivity.this, nom + "\n" + adresse, Toast.LENGTH_LONG).show();
                        selectLayer(selectedSymbolLayer);

                        //GESTION CLIC BATIMENT ETARE
                    } else if (featuresEtare.size() > 0) {
                        String id = "";
                        String nom = featuresEtare.get(0).getStringProperty("NOM");
                        String adresse = featuresEtare.get(0).getStringProperty("ADRESSE_AD");
                        try {

                            if (featuresEtare.get(0).getStringProperty("ID_KEY") != null) {
                                id = featuresEtare.get(0).getStringProperty("ID_KEY");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MapActivity.this, nom + "\n" + adresse, Toast.LENGTH_LONG).show();
                        Intent goToDocIntent = new Intent(MapActivity.this, DocumentActivity.class);
                        goToDocIntent.putExtra(DocumentActivity.DOCUMENT_EXTRA_KEY, featuresEtare.get(0).getStringProperty("HTTP_ETARE"));
                        startActivity(goToDocIntent);
                        selectLayer(selectedSymbolLayer);
                    }
                }
                return true;
            }
        });
        map.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //Log.e("ZOOM LVL : " , "" + map.getCameraPosition().zoom);
                modifZoom();
            }
        });
    }

    /////////////////////////////////////////////////////////////////INITIALISATION ITINÉRAIRE////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({"MissingPermission"})
    /**
     * PERMISSION INITIALISATION
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
     * LOCALISATION COMPONENT INITIALISATION
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
        locationComponent.setRenderMode(RenderMode.NORMAL);
    }

    /**
     * LOCALISATION ENGINE INITIALISATION
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
     * INTERVENTION LOCALISATION INITIALISATION
     */
    private void setDestinationPoint() {
        //RECUPERATION DES COORDONNEES DU MAIN ACTIVITY
        LambertPoint dest = (LambertPoint) getIntent().getSerializableExtra(DESTINATION_EXTRA_KEY);
        destinationPosition = Point.fromLngLat(dest.getX(), dest.getY());
    }

    /**
     * INTERVENTION MARKER INITIALISATION
     */
    private void setMissionMarker() {
        IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
        com.mapbox.mapboxsdk.annotations.Icon fireIcon = iconFactory.fromResource((R.drawable.ic_fire));
        destinationMarker = map.addMarker(new MarkerOptions().position(new LatLng(destinationPosition.latitude(), destinationPosition.longitude())).icon(fireIcon));
    }

    /**
     * USER LOCALISATION INITIALISATION
     */
    private void setOriginPoint() {
        if (actualLocation != null) {
            originPositionPoint = Point.fromLngLat(actualLocation.getLongitude(), actualLocation.getLatitude());
        }
    }

    /**
     * NAVIGATION INITIALISATION
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
     * ROUTES INITIALISATION
     *
     * @param origin
     * @param destination
     */
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
     * ROUTES DRAWING
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

    /**
     * ROUTES FAILURE
     */
    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
        Timber.e(t);
    }

    /////////////////////////////////////////////////////////////////CHARGEMENT DES DONNEES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * DATA INITIALISATION
     */
    private void initSourceData() {
        initSourceAndLayerERSurface();
        initSourceAndLayerEtareSurface();
        initSourceAndLayerEdf();
        initSourceAndLayerEdfLine();
        initSourceAndLayerPisteCyclable();
        initSourceAndLayerGazDetendeur();
        initSourceAndLayerGazLine();
        initSourceAndLayerFptPoint();
        initSourceAndLayerPKSNCF();
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

    /**
     * DEFAULT LAYER FOR PLAN MAP
     */
    private void initDefaultInformationLayer() {
        lineGazLayer.setProperties(visibility(NONE));
        lineEdfLayer.setProperties(visibility(NONE));
        lineCyclePiste.setProperties(visibility(VISIBLE));
        linePisteForestLayer.setProperties(visibility(NONE));
        transfoEdfSymbolLayer.setProperties(visibility(NONE));
        gazDetendeurSymbolLayer.setProperties(visibility(NONE));
        lineForestLayerIsActive = false;
    }

    /**
     * DEFAULT LAYER FOR FOREST MAP
     */
    private void initDefaultForestLayer() {
        initDefaultInformationLayer();
        linePisteForestLayer.setProperties(visibility(VISIBLE));
        lineForestLayerIsActive = true;
    }

    /**
     * ICON LAYER INITIALISATION
     */
    private void initLayerIcons() {

        map.getStyle().addImage("itinMarker",
                BitmapFactory.decodeResource(
                        MapActivity.this.getResources(), R.drawable.map_marker_dark));

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
     * PEIS SOURCE INITIALISATION
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
     * PEI CLUSTERING INITIALISATION
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

    /**
     * ICONES CONTRAINTES VEHICULES SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerFptPoint() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.inaccessible_fpt_epa);
        sourceContraintesVehicules = new GeoJsonSource("contraintesVehiculesJson", geoJsonString);

        map.getStyle().addSource(sourceContraintesVehicules);
        contraintesVehiculesSymbolLayer = new SymbolLayer("Contraintes Vehicules", "contraintesVehiculesJson");
        contraintesVehiculesSymbolLayer.setProperties(iconImage("contraintes-vehicules"), visibility(NONE), circleRadius(5f));
        map.getStyle().addLayer(contraintesVehiculesSymbolLayer);

    }

    /**
     * PK SNCF SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerPKSNCF() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.sncf);
        sourcePKSncf = new GeoJsonSource("pkSncf", geoJsonString);

        map.getStyle().addSource(sourcePKSncf);
        pkSncfSymbolLayer = new SymbolLayer("PK SNCF", "pkSncf");
        pkSncfSymbolLayer.setProperties(iconImage("pk-sncf"), visibility(NONE), circleRadius(5f));
        map.getStyle().addLayer(pkSncfSymbolLayer);
    }

    /**
     * PK CYCLABLE SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerPKCyclable() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.jalon_piste_cycle);
        sourcePKCyclable = new GeoJsonSource("pkCyclable", geoJsonString);

        map.getStyle().addSource(sourcePKCyclable);
        pkCyclableSymbolLayer = new SymbolLayer("PK Cyclable", "pkCyclable");
        pkCyclableSymbolLayer.setProperties(iconImage("pk-cyclable"), visibility(NONE), circleRadius(5f));
        map.getStyle().addLayer(pkCyclableSymbolLayer);
    }

    /**
     * PISTE CYCLABLE SOURCE AND LAYER INITIALISATION
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
     * DETENDEUR GAZ SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerGazDetendeur() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.poste_detente_gaz);
        sourceGazDetendeur = new GeoJsonSource("posteGazJson", geoJsonString);
        map.getStyle().addSource(sourceGazDetendeur);

        gazDetendeurSymbolLayer = new SymbolLayer("Poste Detendeur Gaz", "posteGazJson");
        gazDetendeurSymbolLayer.setProperties(iconImage("gaz-detendeur"), visibility(NONE), circleRadius(5f));
        map.getStyle().addLayer(gazDetendeurSymbolLayer);
    }

    /**
     * LIGNE GAZ SOURCE AND LAYER INITIALISATION
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
     * SURFACE ETARE SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerEtareSurface() {
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

    /**
     * ICONE ETARE SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerETARE() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.etare_v2);
        sourceEtare = new GeoJsonSource("sourceETARE", geoJsonString);
        map.getStyle().addSource(sourceEtare);
        etareSymbolLayer = new SymbolLayer("etareSymbolLayer", "sourceETARE");
        etareSymbolLayer.setProperties(visibility(NONE));
        etareSymbolLayer.withProperties(PropertyFactory.iconImage("Etare"), circleRadius(5f));

        map.getStyle().addLayer(etareSymbolLayer);
    }

    /**
     * SURFACE ERP SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerERSurface() {
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

    /**
     * ICONE ERP SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerSymbolER() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.etablissement_v2);
        sourceER = new GeoJsonSource("sourceER", geoJsonString);

        map.getStyle().addSource(sourceER);
        erSymbolLayer = new SymbolLayer("erSymbolLayer", "sourceER");
        erSymbolLayer.withProperties(PropertyFactory.iconImage("{SOUS-TYPE}"), visibility(NONE), circleRadius(5f));

        map.getStyle().addLayer(erSymbolLayer);
    }

    /**
     * LIGNE EDF SOURCE AND LAYER INITIALISATION
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
     * TRANSFO EDF SOURCE AND LAYER INITIALISATION
     */
    private void initSourceAndLayerEdfLine() {
        String geoJsonString = GeoJsonMngr.loadGeoJson(this, R.raw.ligne_ht_edf_lineaire);
        sourceEdfLine = new GeoJsonSource("ligneHtJson", geoJsonString);
        map.getStyle().addSource(sourceEdfLine);
        lineEdfLayer = new LineLayer("Line HT ERDF", "ligneHtJson");
        map.getStyle().addLayer(lineEdfLayer);
        lineEdfLayer.setProperties(lineColor(getResources().getColor(R.color.ligneEdf)), visibility(NONE));
    }

    /**
     * PISTE FORESTIERE SOURCE AND LAYER INITIALISATION
     */
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

////////////////////////////////////////////////////////////////////CAMERA POSITION////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * ZOOM ORIGIN DESTINATION
     */
    private void zoomOnOriginAndDestination(Point origin, Point dest) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(origin.latitude(), origin.longitude()))
                .include(new LatLng(dest.latitude(), dest.longitude()))
                .build();
        map.moveCamera(CameraUpdateFactory
                .newLatLngBounds(latLngBounds, 50)
        );
        modifZoom();

    }

    /**
     * SET USER CAMERA POSITION
     *
     * @param location
     */
    private void setCameraPositionOrigin(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude())
                , 16), 2000);
    }

    /**
     * USER METHOD USER
     *
     * @param v
     */
    @OnClick(R.id.btn_recentrageUser)
    public void focusUser(View v) {
        if (actualLocation != null) {
            setCameraPositionOrigin(actualLocation);
        } else
            Toast.makeText(this, " Veuillez activer l'emplacement GPS", Toast.LENGTH_LONG).show();
    }

    /**
     * SET INTERVENTION CAMERA POSITION
     *
     * @param pos
     */
    private void setCameraPositionDestination(Point pos) {
        Log.e("Camera position : ", pos.toString());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pos.latitude(), pos.longitude()), 16), 2000);
    }


    /**
     * FOCUS METHOD INTERVENTION
     *
     * @param v
     */
    @OnClick(R.id.recentrageDestination)
    public void focusDestination(View v) {
        setCameraPositionDestination(destinationPosition);
    }

    /**
     * METHODE GESTION DU ZOOM ET AFFICHAGE DES ELEMENTS EN FONCTION
     */
    private void modifZoom() {
        if (map.getCameraPosition().zoom > 12) {
            if (peiUnclSymbolLayer != null) {
                peiUnclSymbolLayer.setProperties(visibility(VISIBLE));
            }
        } else {
            if (peiUnclSymbolLayer != null) {
                peiUnclSymbolLayer.setProperties(visibility(NONE));
            }
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

    /////////////////////////////////////////////////////////////////BOUTONS INTERACTIFS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * SHOW/HIDE ROUTES BUTTONS
     */
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
            zoomOnOriginAndDestination(originPositionPoint, destinationPosition);
        }
    }

    /**
     * FILTRE MENU CREATION
     *
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_pois, menu);
        return true;
    }

    /**
     * FILTRE SELECTED (PARAMETER / NEW ROUTE)
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_parameter:
                showParameterDialog();
                break;
            case R.id.btnResearch:
                showResearchDialog();

                break;


        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * FILTER PARAMETER METHOD
     */
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

        validationParameter.setOnClickListener(v -> {
            toggleLayer(lineEdfLayer, switchLigneEdf);
            toggleLayer(transfoEdfSymbolLayer, switchTransfoEdf);
            toggleLayer(lineGazLayer, switchLigneGRDF);
            toggleLayer(gazDetendeurSymbolLayer, switchDetendeurGRDF);

            lineEdfIsActive = switchLigneEdf.isChecked();
            transfoEdfIsActive = switchTransfoEdf.isChecked();
            lineGdfIsActive = switchLigneGRDF.isChecked();
            detendeurGdfIsActive = switchDetendeurGRDF.isChecked();

            parameterDialog.dismiss();
        });

        cancelParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameterDialog.cancel();
            }
        });
        parameterDialog.show();
    }

    /**
     * SHOW/HIDE STYLE BUTTONS
     */
    @OnClick(R.id.btn_filter)
    public void filterMenu() {
        if (!isFilterOpen) {
            showLayerMenu();
        } else {
            closeLayerMenu();
        }
    }

    /**
     * SHOW STYLE BUTTONS METHOD
     */
    private void showLayerMenu() {
        isFilterOpen = true;
        buttonNormalMap.animate().translationY((buttonNormalMap.getHeight()) + 5);
        buttonOrthoPhotoMap.animate().translationY((buttonOrthoPhotoMap.getHeight() * 2) + 10);
        buttonForestMap.animate().translationY((buttonForestMap.getHeight() * 3) + 15);
        new CountDownTimer(10000, 1000) { //10000 milli seconds is total time, 1000 milli seconds is time interval

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (isFilterOpen) closeLayerMenu();

            }
        }.start();
    }

    /**
     * HIDE STYLE BUTTONS METHOD
     */
    private void closeLayerMenu() {
        isFilterOpen = false;
        buttonNormalMap.animate().translationY(0);
        buttonOrthoPhotoMap.animate().translationY(0);
        buttonForestMap.animate().translationY(0);
    }

    /**
     * STREET STYLE SELECT
     */
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

    /**
     * OUTDOORS STYLE SELECT
     */
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

    /**
     * ORTHO-PHOTO STYLE SELECT
     */
    @OnClick(R.id.btn_ortho_photo_map)
    public void orthoPhotoMapActivation() {
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


    /**
     * FILTER PARAMETER CHOICE
     *
     * @param layer
     * @param switchToCheck
     */
    private void toggleLayer(Layer layer, Switch switchToCheck) {
        if (layer != null) {
            if (switchToCheck.isChecked()) {
                layer.setProperties(visibility(VISIBLE));
            } else {
                layer.setProperties(visibility(NONE));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////METHODE NOUVEL ITINERAIRE////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * AFFICHAGE DE LA FENETRE DE SAISIE D'UN NOUVEL ITINERAIRE
     */
    private void showResearchDialog() {

        //Création d'un Dialog
        researchDialog = new Dialog(MapActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.research_dialog, null);

        researchDialog.setContentView(dialogView);
        researchDialog.create();

        //Activation des éléments graphiques pour la saisie itinéraire
        Button itinValResearch = dialogView.findViewById(R.id.btnOkResearch);
        Button itinCancelResearch = dialogView.findViewById(R.id.btnCancelResearch);
        AutoCompleteTextView itinSaisieResearch = dialogView.findViewById(R.id.saisieResearchItin);

        //Ajout d'un TextChangedListener pour afficher les propositions d'adresses lors de la saisie
        itinSaisieResearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Création du Géocoding en récupérant la chaine de caractères saisie
                if (itinSaisieResearch.length() > 0) {

                    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                            .accessToken(Mapbox.getAccessToken())
                            .autocomplete(true)
                            .query(itinSaisieResearch.getText().toString())
                            .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                            .limit(5)
                            .bbox(-1.562509, 45.112976, 0.02050, 46.369369)
                            .proximity(originPositionPoint)
                            .build();
                    mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                        @Override
                        public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                            //Récupération de la liste de résultats lié à la chaine saisie (par adresse)
                            List<CarmenFeature> results = response.body().features();
                            if (results.size() > 0) {
                                //Traitement de la liste des resultats pour remplissage dans la liste de l'autoCompletion
                                String[] adresse = new String[results.size()];
                                for (int i = 0; i < results.size(); i++) {
                                    adresse[i] = results.get(i).placeName();
                                }
                                //Ajout des propositions dans une liste
                                itinSaisieResearch.clearListSelection();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, adresse);
                                itinSaisieResearch.setAdapter(adapter);
                                try {
                                    //Affichage de la liste des données en dessous du champs de saisie.
                                    itinSaisieResearch.showDropDown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d("ITIN E :", "onResponse : not found");
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Méthode quand on clique sur GO
        itinValResearch.setOnClickListener(v -> {
            final String adress = itinSaisieResearch.getText().toString();
            addSearchInput(adress);
            getNewItineraire(adress);
        });
        itinCancelResearch.setOnClickListener(v -> {
            researchDialog.cancel();
        });


        researchDialog.show();

        //instantiation des variables pour l'historique
        settings = getSharedPreferences(PREFS_NAME, 0);
        history = settings.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>());


        Rect rect = new Rect();
        Window window = MapActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);

        //Adaptation de la fenetre en fonction de l'activité
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(researchDialog.getWindow().getAttributes());
        wm.width = Integer.parseInt(String.valueOf(rect.width()));
        wm.height = Integer.parseInt(String.valueOf(WindowManager.LayoutParams.WRAP_CONTENT));
        researchDialog.getWindow().setAttributes(wm);
        //setAutoCompleteSource(itinSaisieResearch);
    }

    /**
     * CALCUL DU NOUVEL ITINERAIRE AVEC ENVOIE SUR LA METHODE D'AJOUT DU MARKER + ZOOM
     *
     * @param adress
     */
    private void getNewItineraire(String adress) {
        if (adress.length() > 0) {
            MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                    .accessToken(Mapbox.getAccessToken())
                    .autocomplete(true)
                    .query(adress)
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();
            mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    List<CarmenFeature> results = response.body().features();

                    if (results.size() > 0) {
                        Point firstResultPoint = results.get(0).center();
                        setNewMarker(firstResultPoint);
                        setCameraPositionDestination(firstResultPoint);
                        savePrefs();
                        researchDialog.dismiss();
                    } else {
                        Log.d("ITIN E :", "onResponse : not found");
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                    t.printStackTrace();
                    new AlertDialog.Builder(MapActivity.this)
                            .setTitle("Erreur")
                            .setMessage("Adresse introuvable")
                            .setNeutralButton("OK", (dialog, which) -> dialog.cancel())
                            .show();

                }
            });
        } else {
            new AlertDialog.Builder(MapActivity.this)
                    .setTitle("Erreur")
                    .setMessage("Champ Vide")
                    .setNeutralButton("OK", (dialog, which) -> dialog.cancel())
                    .show();
        }

    }

    /**
     * CREATION DU MARKER A LA NOUVELLE ADRESSE SAISIE
     *
     * @param point
     */
    private void setNewMarker(Point point) {

        Source source = map.getStyle().getSourceAs("sourceItin");
        if (source != null) {
            map.getStyle().removeSource("sourceInit");
            map.getStyle().removeLayer("itin-layer");
            ((GeoJsonSource) sourceItin).setGeoJson(Feature.fromGeometry(point));

        } else {
            sourceItin = new GeoJsonSource("sourceItin", Feature.fromGeometry(point));
            map.getStyle().addSource(sourceItin);
        }
        SymbolLayer symbolLayer = new SymbolLayer("itin-layer", "sourceItin");
        symbolLayer.withProperties(
                PropertyFactory.iconImage("itinMarker")
        );
        map.getStyle().addLayer(symbolLayer);
    }

    /**
     * AFFICHAGE DE L'HISTORIQUE DES ADRESSES SAISIES
     *
     * @param textView
     */
    private void setAutoCompleteSource(AutoCompleteTextView textView) {
        if (history != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
            textView.setAdapter(adapter);
            textView.showDropDown();
        }
    }

    /**
     * AJOUT DE L'ADRESSE SAISIE DANS L'HISTORIQUE
     *
     * @param input
     */
    private void addSearchInput(String input) {
        if (!history.contains(input)) {
            history.add(input);
        }
    }

    /**
     * SAUVEGARDE DE L'HISTORIQUE
     */
    private void savePrefs() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREFS_SEARCH_HISTORY, history);

        editor.commit();
    }

    /**
     * SELECTION DU LAYER (Non fonctionnel)
     *
     * @param iconLayer
     */
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


    /**
     * DESELECTION DU LAYER (Non fonctionnel)
     *
     * @param iconLayer
     */
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

    /**
     * FILE LOADING
     *
     * @param filename
     * @return
     */
    private String loadJsonFromAsset(String filename) {

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

    ///////////////////////////////////////////////////////////////////////////////MAPBOX METHOD DEFAULT////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

    /**
     * Location engine Callback
     *
     * @param result
     */
    @Override
    public void onSuccess(LocationEngineResult result) {
        actualLocation = result.getLastLocation();
        setOriginPoint();
    }

    /**
     * On failure location engine callback
     *
     * @param exception
     */
    @Override
    public void onFailure(@NonNull Exception exception) {

    }


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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez vous vraiment retourner au menu ?")
                .setPositiveButton("Oui", (dialog, which) -> MapActivity.this.finish())
                .setNegativeButton("Annulé", null)
                .show();
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
/* SCALE METHOD
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
}*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

