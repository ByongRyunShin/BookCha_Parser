import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	 public static void main(String[] args) throws FileNotFoundException
	   {
		 PrintWriter pw=new PrintWriter("engusa.txt");
		 try {
			BufferedReader brr=new BufferedReader(new InputStreamReader(new FileInputStream("list3.txt"), "euc-kr"));
			String line=null;
			String contentstitle=null, contentsdetail=null, genre=null, country=null, isbn=null, bookname=null, author=null, publisher=null;
			while((line=brr.readLine())!=null){
				int flag=0;
				if(line.startsWith("한국") || line.startsWith("영미") || line.startsWith("일본")){
					String[] arr=line.split(" ");
					
					if(arr[0].equals("한국")) country="1";
					else if(arr[0].equals("영미")) country="2";
					else if(arr[0].equals("일본")) country="3";
					
					if(arr[1].equals("SF_과학")) genre="1";
					else if(arr[1].equals("가족_성장")) genre="2";
					else if(arr[1].equals("공포_스릴러")) genre="3";
					else if(arr[1].equals("로맨스")) genre="4";
					
				}
				else{
				//System.out.println(genre + " " + country);
				String url=line;
				Document doc = Jsoup.connect(url).get();
	            Elements input = doc.getElementsByTag("input");
	            
	            //ISBN 파싱\
	            for(Element ss : input){
	            	if(ss.attr("name").equals("barcode")) isbn=ss.attr("value");
	            	else if(ss.attr("name").equals("authorNm")) author=ss.attr("value");
	            	else if(ss.attr("name").equals("pubNm")) publisher=ss.attr("value");
	            	else if(ss.attr("name").equals("bookNm")) bookname=ss.attr("value");
	            }
	            //설명
	            if(doc.select("div.box_detail_content h3").size()==1) flag=1;
	            if(flag==0){
	            Element h3=doc.select("div.box_detail_content h3").get(1);
	            contentstitle=h3.text();
	            }
	           
	            Elements h31=doc.select("div.box_detail_article");
	            contentsdetail=h31.get(0).text();
	            
	            //이미지
	            String image = doc.select("meta[property=rb:itemimage]").attr("content");
	            //System.out.println(image);
	            //image=image.substring(0, image.indexOf("large"))+"xlarge"+image.substring(image.indexOf("large")+5);
	            //image=image.substring(0, 52)+"x"+image.substring(53);
	            ImageDownload imageDownload = new ImageDownload(image, isbn);
	            
	            pw.write(isbn);
	            pw.write("\b");
	            pw.write(bookname);
	            pw.write("\b");
	            pw.write(author);
	            pw.write("\b");
	            pw.write(publisher);
	            pw.write("\b");
	            if(flag==0) pw.write(contentstitle);
	            else pw.write(" ");
	            pw.write("\b");
	            pw.write(contentsdetail);
	            pw.write("\b");
	            pw.write(genre);
	            pw.write("\b");
	            pw.write(country);
	            pw.write("\r\n");
	            pw.flush();
	            }
				
				
			}
			pw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
}

class ImageDownload 
{
   public ImageDownload(String _url, String _fileName)
   {
      String fileName = _fileName + ".jpg";
      
      try 
      {
         URL url = new URL(_url);
         BufferedImage img = ImageIO.read(url);
         File file = new File(fileName);
         ImageIO.write(img, "jpg", file);
      } 
      catch (Exception e) 
      {
         // TODO Auto-generated catch block
    	  System.out.println("안된다고 병시나");
    	  System.out.println(_url);
    	  System.out.println(fileName);
         e.printStackTrace();
      }
   }
}
