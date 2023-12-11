package com.example.pt6_joel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NovaActivitat extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Team> teamsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_activitat);

        recyclerView = findViewById(R.id.recyclerViewTeams);
        teamsList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String lligaSeleccionada = extras.getString("LLIGA_SELECCIONADA");
            String urlLliga = "https://www.vidalibarraquer.net/android/sports/" + lligaSeleccionada + ".json";

            new DescarregarDades().execute(urlLliga);
        }
    }

    private class DescarregarDades extends AsyncTask<String, Void, ArrayList<Team>> {
        @Override
        protected ArrayList<Team> doInBackground(String... urls) {
            try {
                String result = descarregarDadesDeURL(urls[0]);
                return parseJson(result);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Team> equips) {
            if (equips != null) {
                teamsList = equips;
                mostrarDatosEnRecyclerView();
            }
        }
    }

    private void mostrarDatosEnRecyclerView() {
        // Aquí se crea e inicializa el adaptador
        TeamAdapter adapter = new TeamAdapter(NovaActivitat.this, teamsList);

        // Establece un LinearLayoutManager al RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Establece el adaptador en el RecyclerView
        recyclerView.setAdapter(adapter);

        // Establece el listener para los clics en el adaptador
        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Team selectedTeam = teamsList.get(position);
                Intent intent = new Intent(NovaActivitat.this, TeamActivity.class);
                intent.putExtra("TEAM_NAME", selectedTeam.getTeamName());
                intent.putExtra("TEAM_ABBREVIATION", selectedTeam.getTeamAbbreviation());

                // Obtener el valor de LLIGA_SELECCIONADA
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String lligaSeleccionada = extras.getString("LLIGA_SELECCIONADA");
                    // Agregar LLIGA_SELECCIONADA a los extras del Intent
                    intent.putExtra("LLIGA_SELECCIONADA", lligaSeleccionada);
                }

                startActivity(intent);
            }
        });
    }

    private String descarregarDadesDeURL(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            jsonString = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonString;
    }

    private ArrayList<Team> parseJson(String jsonString) throws JSONException {
        ArrayList<Team> teamList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray teamsArray = jsonObject.getJSONArray("teams");

            for (int i = 0; i < teamsArray.length(); i++) {
                JSONObject teamObject = teamsArray.getJSONObject(i);
                int teamId = teamObject.getInt("team_id");
                String teamName = teamObject.getString("team_name");
                String teamAbbreviation = teamObject.getString("team_abbreviation");

                Team team = new Team(teamId, teamName, teamAbbreviation);
                teamList.add(team);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return teamList;
    }
}