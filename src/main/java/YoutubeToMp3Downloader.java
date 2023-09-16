import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class YoutubeToMp3Downloader extends Thread
   {
    private String youtubeURL;
    private ChromeDriver chromeDriver;

    public YoutubeToMp3Downloader(String youtubeURL)
       {
        configureDriverSettings();

        this.youtubeURL = youtubeURL;
        this.chromeDriver = new ChromeDriver();
       }


    public void configureDriverSettings()
       {
        //System.setProperty("webdriver.chrome.driver", YoutubeToMp3Downloader.class.getResource("chromedriver").getPath());
        System.setProperty("webdriver.chrome.silentOutput", "true"); 
        Logger logger = Logger.getLogger(""); 
        logger.setLevel(Level.OFF); 
       }

    public void run() 
       {
        chromeDriver.get("https://en.onlymp3.to/TCG/");

        WebElement input = chromeDriver.findElement(By.xpath("//*[@id=\"txtUrl\"]"));
        WebElement convertButton = chromeDriver.findElement(By.xpath("//*[@id=\"btnSubmit\"]/span"));
        WebElement downloadButton = null;
        
        input.sendKeys(this.youtubeURL);

        try
           { 
            convertButton.click();
           }
        catch(Exception exc) 
           { 
            chromeDriver.quit(); 
            exc.printStackTrace();
           }

        while(downloadButton == null)
           {
            try 
               {
                Thread.sleep(10000);
                downloadButton = chromeDriver.findElement(By.xpath("//*[@id=\"btn192\"]/button[1]/a"));
                downloadButton.click();

                Thread.sleep(45000);
                chromeDriver.quit();
               }
            catch(Exception exc ) 
               {
                downloadButton = null;
               }
           }
       }
   }