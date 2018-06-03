package Business;

import DAO.MongoDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Mongo;
import entity.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrawlerImpl {

    /*
    * checks if new user entry already exist or not
    * */
    public boolean isExist(int sourceId, ArrayList<Integer> sources){



        if((sources.contains(sourceId))){
            return false;
        }

        else {
            return true;
        }
    }


    public void extractDataFromSikayetvar(int read_times, MongoDB myMongo) throws InterruptedException {

        /*
        * list_of_complains stores all complains objects
        * list_of_sourcesId stores all complains text id in order to check data has been existed or not
        * */
        ArrayList<Application> list_of_complains = new ArrayList<Application>();
        ArrayList<Integer> list_of_sourcesId= new ArrayList<>();

        Interaction interaction = new Interaction();
        Language language = new Language();
        Source source = new Source();
        Author author = new Author();
        Interactions interactions = new Interactions();
        Application application = new Application();


        while (true) {

            /*
            * max_size is used for max. page can user see in the page
            * index used for control list index
            * */
            int max_size = 2;
            int index = 0;




            for (int page_number = 1; page_number < max_size; page_number++) {

                String url_site = "https://www.sikayetvar.com/vodafone?page=" + page_number;
                System.setProperty("webdriver.chrome.driver", "/home/eren/Downloads/chromedriver");
                WebDriver driver = new ChromeDriver();

                driver.get(url_site);
                TimeUnit.SECONDS.sleep(1);

                /*
                * Main directory of each complains cards
                * */
                List<WebElement> complains = driver.findElements(By.xpath("//div[@class='complaint-card']"));


                for (WebElement complain_Search : complains) {


                    List<WebElement> element_texts = complain_Search.findElements((By.xpath(".//div[@class='media-body']")));


                    //some of situation has been deleted by web site, in order to prevent bug check element_text is null or not
                    if (element_texts != null) {


                        for (WebElement element_text : element_texts) {

                            WebElement element_author = complain_Search.findElement(By.xpath(".//div[@class='item-footer']"));

                            /*authors*/
                            String authorName=(element_author.findElement(By.xpath(".//span[@class='name']")).getText()).trim();
                            author.setUserName(authorName);

                            String authorUserId=(element_author.findElement(By.xpath(".//span[@class='name']")).getAttribute("data-memberid"));
                            author.setUserId(authorUserId);

                            String user_link = "https://www.sikayetvar.com/uye/detay/" + author.getUserId();
                            author.setUserLink(user_link);

                            /*sources*/
                            int sourceId=Integer.parseInt(element_text.findElement(By.xpath(".//a[@data-id]")).getAttribute("data-id"));
                            source.setId(sourceId);

                            list_of_sourcesId.add(source.getId());


                            /*interactions*/
                            String interactionsContent=complain_Search.findElement(By.xpath(".//p[@class='complaint-summary']")).getText();
                            interactions.setContent(interactionsContent);


                            interactions.setLink(url_site);
                            interactions.setAuthor(author);

                            /*interaction*/
                            interaction.setInteractions(interactions);
                            interaction.setSource(source);

                            /*application*/
                            application.setLanguageObject(language);
                            application.setInteractionObject(interaction);


                            /*
                            * In a real-time, new entries are checking if already exist or not
                            *read_times used for understand that which cycle we are in.
                            * for example; In the 1st cycle all pages have read so, read_times=1
                            * In the 2nd cycle we extract the data till the last data we got in 1st cycle.
                            * So, read_times remind that we are in 2nd cycle and we have to check in order to avoid extract same data.
                            * */

                            if (read_times!=0&&!isExist(source.getId(),list_of_sourcesId)) {
                                System.out.println(source.getId()+" "+"Already exists");
                                break;



                            }//if ends
                            else {

                                list_of_complains.add(index, application);
                                Gson gsonObj = new GsonBuilder().disableHtmlEscaping().create();

                                String jsonStr = gsonObj.toJson(list_of_complains.get(index));


                                myMongo.insertData(jsonStr);
                                //ElasticDB elastic_insert=new ElasticDB();
                                //elastic_insert.insertDataToElastic(jsonStr);
                                System.out.println(jsonStr);



                                index++;
                            }//else ends

                        }
                    }
                }


                List<WebElement> pages = driver.findElements(By.xpath("//div[@class='text-center']"));

                /* in order to increase page number and specify last page user can see.
                *
                * */
                for (WebElement page : pages) {
                    WebElement page_size = page.findElement(By.xpath(".//a"));
                    List<String> myList = new ArrayList<String>(Arrays.asList(page.getText().split(" ")));

                    if ((myList.size() - 1) != -1) {
                        max_size = Integer.parseInt(myList.get(myList.size() - 1));
                    }


                }

                driver.close();
            }

            /*
            * When all page are read, in order to avoid banned from web site wait 1 min.
            * 1st cycle finished increase read_times
            * */
            TimeUnit.MINUTES.sleep(1);
            read_times++; }//while ends
    }//method ends
}//class ends
