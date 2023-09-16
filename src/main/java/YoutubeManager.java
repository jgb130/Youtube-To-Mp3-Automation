/**
 * Sample Java code for youtube.search.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/code-samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

public class YoutubeManager 
   {
    private static final String CLIENT_SECRETS= "client_secret.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "Youtube-Manager";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = YoutubeManager.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .build();
        Credential credential =
            new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public ArrayList<String> listVideosBySearchOAuth(String searchPhrase, long numberOfResults, boolean display) throws Exception
       {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
            .list("snippet");
        SearchListResponse response = request.setMaxResults(numberOfResults)
            .setQ(searchPhrase)
            .execute();

        JSONObject jsonObject = new JSONObject(response);
        ArrayList<String>youtubeURLS = new ArrayList<String>();
        for(int index = 0; index < (int)numberOfResults; index++)
           {
            String kind = jsonObject.getJSONArray("items").getJSONObject(index).getJSONObject("id").getString("kind");
            if(kind.equals("youtube#video"))
               {
                String videoId = jsonObject.getJSONArray("items").getJSONObject(index).getJSONObject("id").getString("videoId");
                String youtubeURL = "https://www.youtube.com/watch?v=" + videoId;
                youtubeURLS.add(youtubeURL);

                if(display)
                   {
                    if(index == 0)
                       {
                        System.out.println("-----------------------------------------------");
                       }
                    System.out.println("| " + youtubeURL + " |\n-----------------------------------------------");
                   }
               }
           }
        return youtubeURLS;
       }

       public ArrayList<String> listVideosBySearchAPI(String searchPhrase, int numberOfResults, String apiKey, boolean display) throws Exception
          {
           String requestURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" + numberOfResults + "&q=" + searchPhrase + "&key=" + apiKey;
           String jsonResponse = Jsoup.connect(requestURL)
                                      .ignoreContentType(true).get().body().toString()
                                      .replace("<body>","")
                                      .replace("</body>","");

           JSONObject jsonObject = new JSONObject(jsonResponse);

           ArrayList<String>youtubeURLS = new ArrayList<String>();
           for(int index = 0; index < (int)numberOfResults; index++)
              {
               String kind = jsonObject.getJSONArray("items").getJSONObject(index).getJSONObject("id").getString("kind");
               if(kind.equals("youtube#video"))
                  {
                   String videoId = jsonObject.getJSONArray("items").getJSONObject(index).getJSONObject("id").getString("videoId");
                   String youtubeURL = "https://www.youtube.com/watch?v=" + videoId;
                   youtubeURLS.add(youtubeURL);

                   if(display)
                      {
                       if(index == 0)
                          {
                           System.out.println("-------------------------------------------------");
                          }
                       System.out.printf("|  %s  |\n-------------------------------------------------\n", youtubeURL);
                      }
                  }
              }
           return youtubeURLS;
          }
   }