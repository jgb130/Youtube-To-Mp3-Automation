
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.io.IOException;  

public class BillBoardMusicManager
   {
    public ArrayList<String> getBillboardRankings(int numberOfRankings, boolean display) throws Exception
       {
        if(numberOfRankings > 75)
           {
            numberOfRankings = 75;
           }

        Document doc = Jsoup.connect("https://www.billboard.com/charts/hot-100/").get();  
        Elements billboardRawList = doc.select("#title-of-a-story");
        //Elements secondRaw = doc.select("li.o-chart-results-list__item");
        //System.out.println(secondRaw);

        ArrayList<String> billboardRankings = new ArrayList<String>();
        int songCounter = 1; 
        for(int index = 6; (index - 6) < (numberOfRankings * 4); index++)
           {
            if(index >= 6 && ((index - 6) % 4) == 0)
               {
                String song = billboardRawList.get(index).text();
                billboardRankings.add(song);

                if(display)
                   {
                    String counterAndSongString = + songCounter + " - " + song;
                     
                    //counterAndSongString = StringUtils.leftPad(counterAndSongString, 20);
                    counterAndSongString = StringUtils.rightPad(counterAndSongString, 35);

                    System.out.printf("-----------------------------------------\n");
                    System.out.printf("|  %s  |\n", counterAndSongString);

                    if(songCounter == numberOfRankings)
                       {
                        System.out.printf("-----------------------------------------\n");
                       }
                   }

                songCounter++;
               }
           }
        return billboardRankings;
       }
 
   }
