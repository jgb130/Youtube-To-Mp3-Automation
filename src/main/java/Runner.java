import java.util.ArrayList;

public class Runner
   {
    public static void main(String[]args)
       {
        System.out.println("Hello, Runner!\n\n");

        String apiKey = "AIzaSyCMff0JjmH7U11y9poDjX6jziX_QvDXIg8";

        boolean useBillboard = true;
        String searchPhrase = "Cinema - Skrillex";
        int numberOfBBResults = 5;
        int numberOfYTResults = 1;
        boolean consoleDisplay = true;
        boolean downloadContent = false;

        try 
           {
            YoutubeManager ytManager = new YoutubeManager();
            ArrayList<String> availableURLS = new ArrayList<String>();
            
            if(useBillboard)
               {
                BillBoardMusicManager bbManager = new BillBoardMusicManager();
                ArrayList<String> billboardRankings = bbManager.getBillboardRankings(numberOfBBResults, consoleDisplay);

                int indexOne;
                for(indexOne = 0; indexOne < billboardRankings.size(); indexOne++)
                   {
                    availableURLS = ytManager.listVideosBySearchAPI(billboardRankings.get(indexOne), numberOfYTResults, apiKey, consoleDisplay);
                   }
               }
            else
               {
                availableURLS = ytManager.listVideosBySearchAPI(searchPhrase, numberOfYTResults, apiKey, consoleDisplay); 
               }

            if(downloadContent)
               {
                YoutubeToMp3Downloader downloader = new YoutubeToMp3Downloader(availableURLS.get(0));
                downloader.start();
               }
           }
        catch(Exception exc)
           {
            exc.printStackTrace();
           }
       }
   }