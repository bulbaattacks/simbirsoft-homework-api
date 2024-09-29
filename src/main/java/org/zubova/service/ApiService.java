package org.zubova.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.zubova.pojo.Entity;

import static io.restassured.RestAssured.given;

public class ApiService {
    public static final String CREATE_PATH = "/api/create";
    public static final String DELETE_PATH = "/api/delete/{id}";
    public static final String GET_PATH = "/api/get/{id}";
    public static final String GET_ALL_PATH = "/api/getAll";
    public static final String PATCH_PATH = "/api/patch/{id}";

    public static Integer createAndGetId(Entity entity) {
        RestAssured.registerParser("text/plain", Parser.JSON);
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.TEXT)
                .body(entity)
                .when()
                .post(CREATE_PATH)
                .then()
                .extract()
                .as(Integer.class);
    }

    public static Entity getEntityBy(Integer id) {
        return given()
                .when()
                .get(GET_PATH, id)
                .then()
                .extract()
                .as(Entity.class);
    }

    private ApiService() {throw new IllegalStateException("Helper class");}
}
