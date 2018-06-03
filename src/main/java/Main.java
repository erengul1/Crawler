
import DAO.MongoDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Main {

    public boolean isExist(int sourceId, ArrayList<Integer> sources){



        if((sources.contains(sourceId))){
            return false;
        }

        else {
        return true;
            }
    }


public static void main(String[] args) throws InterruptedException {

   int read_times=0;
   Main main=new Main();

   MongoDB myMongo = new MongoDB();

   myMongo.initialize();

    ArrayList<Application> list_of_complains = new ArrayList<Application>();
    ArrayList<Integer> list_of_sourcesId= new ArrayList<>();

    while (true) {
        int max_size = 2;
        int index = 0;

        /*  in order to get all pages
         *  max_size has used as a maximum page can be seen in page
         *
         * */

        for (int page_number = 1; page_number < max_size; page_number++) {

            String url_site = "https://www.sikayetvar.com/vodfone?page=" + page_number;

            System.setProperty("webdriver.chrome.driver", "/home/eren/Downloads/chromedriver");
            WebDriver driver = new ChromeDriver();


            driver.get(url_site);
            TimeUnit.SECONDS.sleep(1);

            List<WebElement> complains = driver.findElements(By.xpath("//div[@class='complaint-card']"));

            Interaction Interaction = new Interaction();
            Language language = new Language();
            Source source = new Source();
            Author Author = new Author();
            Interactions Interactions = new Interactions();
            Application application = new Application();

            for (WebElement complain_Search : complains) {


                List<WebElement> element_texts = complain_Search.findElements((By.xpath(".//div[@class='media-body']")));


                //some of situation has been deleted by web site, in order to prevent bug check element_text is null or not
                if (element_texts != null) {

                    for (WebElement element_text : element_texts) {

                        WebElement element_author = complain_Search.findElement(By.xpath(".//div[@class='item-footer']"));

                        Author.setUserName((element_author.findElement(By.xpath(".//span[@class='name']")).getText()).trim());
                        Author.setUserId(element_author.findElement(By.xpath(".//span[@class='name']")).getAttribute("data-memberid"));
                        String user_link = "https://www.sikayetvar.com/uye/detay/" + Author.getUserId();
                        Author.setUserLink(user_link);

                        source.setId(Integer.parseInt(element_text.findElement(By.xpath(".//a[@data-id]")).getAttribute("data-id")));
                        list_of_sourcesId.add(source.getId());


                        Interactions.setContent(complain_Search.findElement(By.xpath(".//p[@class='complaint-summary']")).getText());
                        Interactions.setLink(url_site);
                        Interactions.setAuthor(Author);
                        Interaction.setInteractions(Interactions);
                        Interaction.setSource(source);

                        application.setLanguageObject(language);
                        application.setInteractionObject(Interaction);

                        if (read_times!=0&&main.isExist(source.getId(),list_of_sourcesId)==false) {
                            System.out.println(source.getId()+" "+"Already exists");
                            break;



                        } else {
                            list_of_complains.add(index, application);
                            Gson gsonObj = new GsonBuilder().disableHtmlEscaping().create();

                            String jsonStr = gsonObj.toJson(list_of_complains.get(index));


                            myMongo.insertData(jsonStr);
                            //ElasticDB elastic_insert=new ElasticDB();
                            //elastic_insert.insertDataToElastic(jsonStr);
                            System.out.println(jsonStr);


                            index++;
                        }

                    }
                }
            }


            List<WebElement> pages = driver.findElements(By.xpath("//div[@class='text-center']"));

            /* in order to increase page number*/
            for (WebElement page : pages) {
                WebElement page_size = page.findElement(By.xpath(".//a"));
                List<String> myList = new ArrayList<String>(Arrays.asList(page.getText().split(" ")));

                if ((myList.size() - 1) != -1) {
                    max_size = Integer.parseInt(myList.get(myList.size() - 1));
                }


            }

            driver.close();
        }


        TimeUnit.MINUTES.sleep(1);
        read_times++; }

}
}