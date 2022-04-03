package org.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import io.restassured.path.json.JsonPath;
import org.junit.Test;

public class AppTestGet {
    private final String apiKey = "403374500dbd4d1bb8587033294d5bad";

    @Test
    public void test1Positive() {
        JsonPath response = given()
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch?" +
                        "cuisine=italian&apiKey=" + apiKey)
                .body()
                .jsonPath();
        assertThat(response.get("number"), equalTo(10));
        assertThat(response.get("offset"), equalTo(0));
    }

    @Test
    public void test2Positive() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("nutrient", "protein")
                .queryParam("target", "10")
                .queryParam("unit", "OZ")
                .when()
                .get("https://api.spoonacular.com/food/ingredients/9266/amount")
                .body()
                .jsonPath();
        assertThat(response.get("amount"), equalTo(35.27F));
        assertThat(response.get("unit"), equalTo("OZ"));
    }

    @Test
    public void test3Positive() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/100/information")
                .body()
                .jsonPath();
        assertThat(response.get("extendedIngredients.id[0]"), equalTo(15002));
        assertThat(response.get("vegetarian"), is(false));

    }

    @Test
    public void test4Negative() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("diet", "vegan")
                .when()
                .get("https://api.spoonacular.com/recipes/0/tasteWidget.json")
                .body()
                .jsonPath();
        assertThat(response.get("status"), equalTo("failure"));
        assertThat(response.get("code"), is(404));
        assertThat(response.get("message"), equalTo("A recipe with the id 0 does not exist."));
    }

    @Test
    public void test5Negative() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("query", "melon")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch1")
                .then()
                .statusCode(404)
                .contentType("text/html;charset=utf-8");
    }
}
