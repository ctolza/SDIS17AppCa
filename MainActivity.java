package e.kiosque.appca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import e.kiosque.appca.Lambert_conversion.Lambert;
import e.kiosque.appca.Lambert_conversion.LambertPoint;
import e.kiosque.appca.Lambert_conversion.LambertZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lng)
    EditText longitude;
    @BindView(R.id.lat)
    EditText latitude;
    @BindView(R.id.GoButton)
    Button goButton;
    @BindView(R.id.latErrorTxt)
    TextView latErrorTxt;
    @BindView(R.id.lngErrorTxt)
    TextView lngErrorTxt;
    @BindView(R.id.adressTxt)
    TextView adressTxt;

    private static final int LAT_LENGTH = 6;
    private static final int LNG_LENGTH = 7;

    private boolean latTxtOk;
    private boolean lngTxtOk;

    private String coordLngAuto;
    private String coordLatAuto;

    private double doubleLat;
    private double doubleLng;

    private LambertPoint ptWGS84;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        goButton.setActivated(false);

        try {
            getCoordFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lngErrorTxt.setVisibility(View.INVISIBLE);
        latErrorTxt.setVisibility(View.INVISIBLE);
    }

    /**
     * CHANGEMENT ACTIVITY, ENVOIE SUR LA MAP
     * @param v
     */
    @OnClick(R.id.GoButton)
    public void onClickGoButton(View v) {
        if (latTxtOk && lngTxtOk) {
            Intent goToMapIntent = new Intent(MainActivity.this, MapActivity.class);
            goToMapIntent.putExtra(MapActivity.DESTINATION_EXTRA_KEY, ptWGS84);
            startActivity(goToMapIntent);
        }
    }

    //////////////////////////////////////SAISIE COORDONNEES AUTO///////////////////////////////////////
    /**
     * RECUP COORD DEPUIS FICHIER TXT
     * appel dans le onCreate()
     * @throws IOException
     */
    public void getCoordFile() throws IOException {

        //Récup File
        InputStream fileInputStream = getAssets().open("test.txt");
        String path = "C:.../Desktop/test2.txt";

        //taille
        int size = fileInputStream.available();
        byte[] buffer = new byte[size];

        //lecture
        fileInputStream.read(buffer);
        fileInputStream.close();

        //Stockage des données
        String data = new String(buffer);

        //Split pour avoir les coords
        String coord = String.valueOf(data);
        String[] separate = coord.split("//");

        //Parsing String to Double pour coords
        coordLngAuto = separate[1];
        coordLatAuto = separate[0];

        //Affichage dans les champs de texte
        if (coordLatAuto.length() == LAT_LENGTH && coordLngAuto.length() == LNG_LENGTH) {
            latitude.setText(coordLatAuto);
            longitude.setText(coordLngAuto);
        }
    }

    //////////////////////////////////////VERIF CONTRAINTES SAISIE//////////////////////////////////


    /**
     * VERIF DU NOMBRE DE CARACTERES ET SI C'EST BIEN QUE DES CHIFFRES POUR LONGITUDE
     */
    @OnTextChanged(R.id.lat)
    public void onTextChangeLatitude() {
        if (checkTxtLengthAndNumber(LAT_LENGTH, latitude.getText().toString())) {
            latTxtOk = true;
            latErrorTxt.setVisibility(View.INVISIBLE);
            onTextsChanges();
        } else {
            latTxtOk = false;
            latErrorTxt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * VERIF DU NOMBRE DE CARACTERES ET SI C'EST BIEN QUE DES CHIFFRES POUR LATITUDE
     */
    @OnTextChanged(R.id.lng)
    public void onTextChangeLongitude() {
        if (checkTxtLengthAndNumber(LNG_LENGTH, longitude.getText().toString())) {
            lngTxtOk = true;
            lngErrorTxt.setVisibility(View.INVISIBLE);
            onTextsChanges();
        } else {
            lngTxtOk = false;
            lngErrorTxt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * RECUP DE L'ADRESSE COMPLETE
     */
    public void onTextsChanges() {
        if (lngTxtOk && latTxtOk) {
            doubleLng = Double.parseDouble(longitude.getText().toString());
            doubleLat = Double.parseDouble(latitude.getText().toString());
            ptWGS84 = Lambert.convertToWGS84Deg(doubleLat, doubleLng, LambertZone.LambertIIExtended);

            MapboxGeocoding reverseGeocoding = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(Point.fromLngLat(ptWGS84.getX(), ptWGS84.getY()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();
            reverseGeocoding.enqueueCall(new Callback<GeocodingResponse>() {

                String firstResultPlaceName = "";

                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    List<CarmenFeature> results = response.body().features();

                    if (results.size() > 0) {
                        //Log the first results Adress.
                        firstResultPlaceName = results.get(0).placeName();
                        adressTxt.setText(firstResultPlaceName);
                        Log.d("reponse point :", "onResponse: " + firstResultPlaceName);
                        activateGoButton();
                    } else {
                        //No result for your request were found.
                        Log.d("no response :", "onResponse : No result found");
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    /**
     * VERIF LONGUEUR ET CARACTERES
     * @param length
     * @param txtToCheck
     * @return
     */
    private boolean checkTxtLengthAndNumber(int length, String txtToCheck) {
        if (txtToCheck.length() == length && txtToCheck.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ACTIVATION BOUTON GO SI CONDITION OK
     */
    private void activateGoButton() {
        if (lngTxtOk && latTxtOk) {
            goButton.setActivated(true);
        } else if (!lngTxtOk) {
            lngErrorTxt.setVisibility(View.VISIBLE);
        } else if (!latTxtOk) {
            latErrorTxt.setVisibility(View.VISIBLE);
        }
    }


}
