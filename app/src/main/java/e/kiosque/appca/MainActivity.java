package e.kiosque.appca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

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

    private double doubleLat;
    private double doubleLng;

    private LambertPoint ptWGS84;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        goButton.setActivated(false);
        lngErrorTxt.setVisibility(View.INVISIBLE);
        latErrorTxt.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.GoButton)
    public void onClickGoButton(View v) {
        if (latTxtOk && lngTxtOk) {
            Intent goToMapIntent = new Intent(MainActivity.this, MapActivity.class);
            goToMapIntent.putExtra(MapActivity.DESTINATION_EXTRA_KEY, ptWGS84);
            startActivity(goToMapIntent);
        }
        //Toast.makeText(this, "Naviguer au point : latitude :" + ptWGS84.getY() + " longitude :" + ptWGS84.getX(), Toast.LENGTH_LONG).show();
    }


    @OnTextChanged(R.id.lat)
    public void onTextChangeLatitude() {
        if (checktTxtLenghtAndNumber(LAT_LENGTH, latitude.getText().toString())) {
            latTxtOk = true;
            latErrorTxt.setVisibility(View.INVISIBLE);
            onTextsChanges();
        } else {
            latTxtOk = false;
            latErrorTxt.setVisibility(View.VISIBLE);

        }
    }

    @OnTextChanged(R.id.lng)
    public void onTextChangeLongitude() {
        if (checktTxtLenghtAndNumber(LNG_LENGTH, longitude.getText().toString())) {
            lngTxtOk = true;
            lngErrorTxt.setVisibility(View.INVISIBLE);
            onTextsChanges();
        } else {
            lngTxtOk = false;
            lngErrorTxt.setVisibility(View.VISIBLE);
        }
    }

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

    private void activateGoButton() {
        if (lngTxtOk && latTxtOk) {
            goButton.setActivated(true);
        } else if (!lngTxtOk) {
            lngErrorTxt.setVisibility(View.VISIBLE);
        } else if (!latTxtOk) {
            latErrorTxt.setVisibility(View.VISIBLE);
        }
    }

    private boolean checktTxtLenghtAndNumber(int length, String txtToCheck) {
        if (txtToCheck.length() == length && txtToCheck.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }
}
