package com.example.pt6_joel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TeamActivity extends AppCompatActivity {

    private ImageView teamImageView;
    private TextView teamNameTextView;
    private TextView teamAbbreviationTextView;
    private TextView teamTitlesTextView;
    private TextView teamStadiumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamImageView = findViewById(R.id.teamImageView);
        teamNameTextView = findViewById(R.id.teamNameTextView);
        teamAbbreviationTextView = findViewById(R.id.teamAbbreviationTextView);
        teamTitlesTextView = findViewById(R.id.teamTitlesTextView);
        teamStadiumTextView = findViewById(R.id.teamStadiumTextView);

        // Obtener los datos pasados desde NovaActivitat
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String lligaSeleccionada = extras.getString("LLIGA_SELECCIONADA");
            String teamName = extras.getString("TEAM_NAME");
            String teamAbbreviation = extras.getString("TEAM_ABBREVIATION");

            // Establecer los datos en los TextViews
            teamNameTextView.setText("Team Name: " + teamName);


            // Cargar la imagen desde la URL
            String imageUrl = "https://www.vidalibarraquer.net/android/sports/"+lligaSeleccionada+"/"+teamAbbreviation.toLowerCase()+".png";
            new DownloadImageTask().execute(imageUrl);
            System.out.println(imageUrl);

            // Cargar y mostrar el contenido JSON del equipo
            String jsonUrl = "https://www.vidalibarraquer.net/android/sports/"+lligaSeleccionada+"/" + teamAbbreviation.toLowerCase()+".json";
            new LoadJSONTask().execute(jsonUrl);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                teamImageView.setImageBitmap(bitmap);
            }
        }
    }

    private class LoadJSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String jsonUrl = strings[0];
            StringBuilder result = new StringBuilder();
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(jsonUrl).openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    String titles = dataObject.getString("titles");
                    String stadium = dataObject.getString("team_stadium");

                    teamTitlesTextView.setText("Titles: " + titles);
                    teamStadiumTextView.setText("Stadium: " + stadium);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
