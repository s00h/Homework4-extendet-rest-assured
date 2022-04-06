package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class AppTestGet {
    private final String apiKey = "403374500dbd4d1bb8587033294d5bad";
    private final String baseUri = "https://api.spoonacular.com/";
    ResponseSpecification responseSpecification = null;
    RequestSpecification requestSpecification = null;

    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(7000L))
                .build();

        RestAssured.responseSpecification = responseSpecification;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void getComplexSearch() {
        given().spec(requestSpecification)
                .when()
                .get(baseUri + "recipes/complexSearch?" +
                        "cuisine=italian&apiKey=" + apiKey).prettyPeek()
                .then()
                .spec(responseSpecification)
                .and()
                .body("number", equalTo(10))
                .body("offset", equalTo(0));
    }

    @Test
    public void getFoodIngredientsId() {
        given().spec(requestSpecification)
                .queryParam("nutrient", "protein")
                .queryParam("target", "10")
                .queryParam("unit", "OZ")
                .when()
                .get(baseUri + "food/ingredients/9266/amount")
                .then()
                .spec(responseSpecification)
                .and()
                .body("amount", equalTo(35.27F))
                .body("unit", equalTo("OZ"));
    }

    @Test
    public void getRecipesInformationId() {
        given().spec(requestSpecification)
                .queryParam("includeNutrition", "false")
                .when()
                .get(baseUri + "recipes/100/information").prettyPeek()
                .then()
                .spec(responseSpecification)
                .and()
                .body("extendedIngredients.id[0]", equalTo(15002))
                .body("vegetarian", is(false));
    }

    @Test
    public void getTasteWidgetId() {
        given().spec(requestSpecification)
                .queryParam("normalize", "true")
                .when()
                .get(baseUri + "recipes/69095/tasteWidget.json").prettyPeek()
                .then()
                .spec(responseSpecification)
                .and()
                .body("sweetness", equalTo(48.15F))
                .body("saltiness", equalTo(45.29F))
                .body("bitterness", equalTo(19.17F))
                .body("savoriness", equalTo(26.45F))
                .body("fattiness", equalTo(100.0F))
                .body("spiciness", equalTo(0.0F));
    }

    @Test
    public void getRecipesConvert() {
        given().spec(requestSpecification)
                .queryParam("ingredientName", "water")
                .queryParam("sourceAmount", "2.5")
                .queryParam("sourceUnit", "glasses")
                .queryParam("targetUnit", "grams")
                .when()
                .get(baseUri + "recipes/convert")
                .then()
                .spec(responseSpecification)
                .and()
                .body("sourceUnit", equalTo("glasses"))
                .body("targetAmount", equalTo(590.0F))
                .body("type", equalTo("CONVERSION"));
    }
}
