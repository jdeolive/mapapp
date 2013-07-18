package org.opengeo.mapapp;

import java.io.File;
import java.io.IOException;

import org.geodroid.map.MapFragment;
import org.jeo.android.GeoDataRegistry;
import org.jeo.android.geopkg.GeoPackage;
import org.jeo.carto.Carto;
import org.jeo.data.Dataset;
import org.jeo.data.Query;
import org.jeo.data.VectorData;
import org.jeo.data.Workspace;
import org.jeo.map.Map;
import org.jeo.map.Style;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

public class MapActivity extends Activity implements MapFragment.Callback {

    Map map;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        // set the content view, initially empty until the map loads
        setContentView(R.layout.activity_map);

        // load the map in the background
        new LoadMap().execute();
    }

    @Override
    public Map getMap() {
        return map;
    }

    /**
     * Create the map to render.
     */
    Map createMap() throws IOException {
        File geodata = GeoDataRegistry.directory();

        // load a geopackage workspace
        Workspace ne = GeoPackage.open(new File(geodata, "ne.gpkg"));

        // load a base tile set
        Dataset tiles = ne.get("tiles");

        // load a vector overlay
        Dataset places = (VectorData) ne.get("populated_places");

        // load the style
        Style style = Carto.parse(new File(geodata, "style.css"));

        // build the map
        return Map.build().layer(tiles).layer(places).style(style).map();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // clean up map resources
        map.close();
    }

    /**
     * Report an error via dialog. 
     */
    void error(int title, Throwable t) {
        AlertDialog.Builder db = new AlertDialog.Builder(this);

        if (title != -1) {
            db.setTitle(title);
        }

        db.setIcon(android.R.drawable.ic_dialog_alert);
        db.setMessage(t.getMessage());
        db.setPositiveButton(R.string.report, null);
        db.setNegativeButton(android.R.string.ok, null);
        db.show();
    }

    /**
     * Background task that loads the map.
     */
    class LoadMap extends AsyncTask<Void, Void, Map> {

        Throwable err;

        @Override
        protected Map doInBackground(Void... params) {
            try {
                return createMap();
            } catch (Exception e) {
                // track the error to report
                err = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Map map) {
            if (err != null) {
                // error occurred loading map
                error(R.string.err_map_load, err);
            }
            else {
                // map ready to go, start the map fragment 
                MapActivity.this.map = map;
                getFragmentManager().beginTransaction().replace(
                    R.id.view_map, new MapFragment()).commit();
            }
        }
    }
}
