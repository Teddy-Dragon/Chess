package ui;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ExceptionReader {

    public String responseReader(HttpURLConnection http){
        try {
            if(http.getResponseCode() == 403){
                return "Already Taken!";
            }
            if(http.getResponseCode() == 400){
                return "Bad request- try again!";
            }
            if(http.getResponseCode() == 401){
                return "Unauthorized!";
            }
            if(http.getResponseCode() == 404){
                return "Does not exist";
            }
            else{
                System.out.println(http.getResponseCode());
                return "Unknown error code";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
