package server;

import spark.*;

import server.handlers.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", new UserHandler()); //register
        Spark.post("/session", new SessionHandler()); //login
        Spark.delete("/session", new SessionHandler()); //logout
        Spark.get("/game", new GameHandler()); //list games
        Spark.post("/game", new GameHandler()); //create game
        Spark.put("/game", new GameHandler()); //join game
        Spark.delete("/db", new ClearHandler()); //nuke it all


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

