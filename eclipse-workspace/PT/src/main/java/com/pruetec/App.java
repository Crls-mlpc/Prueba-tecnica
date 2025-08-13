package com.pruetec;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class App {
    public static void main(String[] args) {
    	

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Javalin app = Javalin.create().start(8081);

        app.post("/users", ctx -> {
            try {
                DataRequest req = gson.fromJson(ctx.body(), DataRequest.class);
                User created = UserService.createUser(req);
                ctx.status(HttpStatus.CREATED).result(gson.toJson(created));
            } catch (IllegalArgumentException e) {
                ctx.status(HttpStatus.BAD_REQUEST).result("{\"error\":\"" + e.getMessage() + "\"}");
            } catch (Exception e) {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("{\"error\":\"Error interno\"}");
            }
        });

        app.get("/users", ctx -> {
            String sortBy = ctx.queryParam("sortBy");
            String field = ctx.queryParam("field");
            String operator = ctx.queryParam("op");
            String value = ctx.queryParam("value");

            List<DataResponse> users = UserService.getUsers(sortBy, field, operator, value);
            ctx.result(gson.toJson(users));
        });

        app.get("/health", ctx -> ctx.result("{\"status\":\"ok\"}"));
    }
}