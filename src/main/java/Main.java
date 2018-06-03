
import Business.CrawlerImpl;
import DAO.MongoDB;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;



public class Main {




public static void main(String[] args) throws InterruptedException {

   int read_times=0;


   MongoDB myMongo = new MongoDB();

   myMongo.initialize();
   CrawlerImpl crawler=new CrawlerImpl();

   crawler.extractDataFromSikayetvar(read_times,myMongo);


            }
    }