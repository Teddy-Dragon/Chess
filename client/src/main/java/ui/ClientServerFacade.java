package ui;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.UUID;


public class ClientServerFacade {
    private final String serverURl;
    private UUID authorization;

    public ClientServerFacade(String url){
        serverURl = url;
    }
    public UUID getAuth(){
        return authorization;
    }

    public AuthData registerUser(UserData registerData) {
        var path = "/user";
        try {
            AuthData response = makeRequest("POST", path, registerData, AuthData.class);
            if(response == null){
                return null;
            }
            authorization = response.authToken();
            return response;
        }
        catch(Exception e){
            return null;
        }

    }
    public AuthData loginUser(UserData loginData){
        var path = "/session";
        try{
            AuthData response = makeRequest("POST", path, loginData, AuthData.class);
            if(response == null){
                return null;
            }
            authorization = response.authToken();
            return response;
        }catch(Exception e){
            return null;
        }
    }

    public void logoutUser() throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
        authorization = null;
    }

    public GameData createGame(GameData gameData) throws Exception {
        var path = "/game";
        GameData response = makeRequest("POST", path, gameData, GameData.class);
        return response;
    }


    public void joinGame(JoinRequest request) throws Exception {
        var path = "/game";
        makeRequest("PUT", path, request, null);
    }

    public ListModel listGames() throws Exception {
        var path = "/game";
        //do map.class for the response type
        ListModel response = makeRequest("GET", path, null, ListModel.class);

        return response;

    }
    public GameData getGameByID(int gameID) throws Exception {
        var path = "/play";
        GameData response = makeRequest("POST", path, gameID, GameData.class);
        return response;
    }
    public void clearAll() throws Exception {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }
    public void removePlayer(JoinRequest request) throws Exception{
        var path = "/play";
        makeRequest("PUT", path, request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        HttpURLConnection http = null;
        try{
            URL url = (new URI(serverURl + path)).toURL();
            http = (HttpURLConnection) url.openConnection(); //Sets up an empty request
            http.setRequestMethod(method); //sets request method
            if(authorization != null){
                //set authorization header to UUID
                http.addRequestProperty("authorization", String.valueOf(authorization));
            }
            http.setDoOutput(true); // lets us know to expect things in return
            writeBody(request, http); //sets request body
            http.connect(); //actually connects with this now not empty request
            throwIfNotSuccessful(http);

            return readBody(responseClass, http);
        }
        catch(Exception e){
            if(http != null){

                String formattedResponse = new ExceptionReader().responseReader(http);
                System.out.println(formattedResponse);
            }

        }
        throw new Exception("");
    }

    private void writeBody(Object request, HttpURLConnection http){
        String body = new Gson().toJson(request);
        http.addRequestProperty("Content-Type", "application/json");
        if(request == null){
            return;
        }
        try(OutputStream reqBody = http.getOutputStream()){
            reqBody.write(body.getBytes()); //connection wants a stream for the body, so we give it a stream
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (status / 100 != 2) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new Exception();
                }
            }

            throw new Exception();
        }
    }

    //client reading from server

    private <T> T readBody(Class<T> responseClass, HttpURLConnection http) throws IOException{
        T response = null;
        if (http.getContentLength() < 0) { // if the content length header is an unknown number or too big
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                    return response;
                }
            }
        }
        else{
            System.out.println(http.getContentLength() + " <- content length header field number");
        }
        return response;

    }

}
