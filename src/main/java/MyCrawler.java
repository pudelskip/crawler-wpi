import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {


    private static final Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && (href.startsWith("http://www.computerworld.com/article/")
                || href.startsWith("https://www.computerworld.com/article/"));

    }

    @Override
    public void visit(Page page) {

        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData && url.startsWith("https://www.computerworld.com/article/")) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.DIRNAME+"/" + page.getWebURL().getDocid()+".txt"));
                writer.write(url+"\n");


                Document doc = Jsoup.parse(htmlParseData.getHtml());
                Elements elements =doc.select("div#drr-container");
                if(elements.size()>0)
                    writer.write(elements.get(0).text());
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            //System.out.println(htmlParseData.getText());
        }
    }

}
