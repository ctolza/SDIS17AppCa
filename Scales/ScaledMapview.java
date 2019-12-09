package e.kiosque.appca.Scales;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.Locale;

import e.kiosque.appca.R;

/**
 * Created by clyde on 2/10/2016.
 * This class extends the Mapbox Mapview to add a scale at the bottom
 */

public class ScaledMapview extends MapView implements MapboxMap.OnCameraMoveListener, OnMapReadyCallback {
    private TextView scaleText;
    private MapboxMap mapboxMap;
    private OnMapReadyCallback callback;
    private ScaleUnit scaleUnit = ScaleUnit.KM;
    private float labelWidth = 0.33f;

    public float getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(float labelWidth) {
        if(labelWidth > 1f)
            labelWidth = 1f;
        else if(labelWidth < 0.1f)
            labelWidth = 0.1f;
        this.labelWidth = labelWidth;
    }

    public ScaleUnit getScaleUnit() {
        return scaleUnit;
    }

    public void setScaleUnit(ScaleUnit scaleUnit) {
        this.scaleUnit = scaleUnit;
    }

    @Override
    public void onCameraMove() {

        if (scaleText != null) {
            // compute the horizontal span in metres of the bottom of the map
            LatLngBounds latLngBounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
            float span[] = new float[1];
            Location.distanceBetween(latLngBounds.getLatSouth(), latLngBounds.getLonEast(),
                    latLngBounds.getLatSouth(), latLngBounds.getLonWest(), span);

            float totalWidth = span[0] / scaleUnit.ratio;
            // calculate an initial guess at step size
            float tempStep = totalWidth * labelWidth;

            // get the magnitude of the step size
            float mag = (float)Math.floor(Math.log10(tempStep));
            float magPow = (float)Math.pow(10, mag);

            // calculate most significant digit of the new step size
            float magMsd = (int)(tempStep / magPow + 0.5);

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
            int width = Math.round(getWidth() * length / totalWidth);
            LayoutParams layoutParams =
                    new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            layoutParams.bottomMargin = 4;
            scaleText.setLayoutParams(layoutParams);
        }
    }

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

    public ScaledMapview(@NonNull Context context) {
        super(context);
    }

    public ScaledMapview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaledMapview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScaledMapview(@NonNull Context context, @Nullable MapboxMapOptions options) {
        super(context, options);
    }

    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        this.callback = callback;
        super.getMapAsync(this);
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        // if the owner of this view is listening for the map, pass it through. If not, we must
        // listen for camera events ourselves.
        if (callback != null)
            callback.onMapReady(mapboxMap);
        else
        onCameraMove();
    }
}
