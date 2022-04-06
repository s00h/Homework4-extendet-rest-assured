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

public class AppTestPost {

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
    public void postMealplannerItems() {
        String id = given()
                .queryParam("hash", "dc9601b2a37227f264b53fe6bfe8261d1415f14d")
                .spec(requestSpecification)
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(baseUri + "mealplanner/myExample/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        System.out.println("ID is: " + id);
        given().spec(requestSpecification)
                .queryParam("hash", "dc9601b2a37227f264b53fe6bfe8261d1415f14d")
                .delete(baseUri + "mealplanner/myExample/items/" + id)
                .then()
                .spec(responseSpecification)
                .and()
                .body("status", is("success"));
    }

    @Test
    public void postRecipesAnalyze() {
        given().spec(requestSpecification)
                .queryParam("language", "en")
                .queryParam("includeNutrition", "false")
                .queryParam("includeTaste", "false")
                .body("{\n"
                        + " \"title\": \"Spaghetti Carbonara\",\n"
                        + " \"servings\": 2,\n"
                        + " \"ingredients\": [ \n"
                        + "         \"1 lb spaghetti\",\n"
                        + "        \"3.5 oz pancetta\",\n"
                        + "        \"2 Tbsps olive oil\",\n"
                        + "        \"1  egg\",\n"
                        + "        \"0.5 cup parmesan cheese\" \n"
                        + " ], \n"
                        + " \"instructions\": \"Bring a large pot of water to a boil and season generously with salt. Add the pasta to the water once boiling and cook until al dente. Reserve 2 cups of cooking water and drain the pasta.\" \n"
                        + "}")
                .when()
                .post(baseUri + "recipes/analyze")
                .then()
                .spec(responseSpecification)
                .and()
                .body("vegetarian", is(false));
    }

    @Test
    public void postRecipesCuisine() {
        given().spec(requestSpecification)
                .queryParam("title", "Pork roast with green beans")
                .queryParam("ingredientList", "3 oz pork shoulder")
                .queryParam("language", "en")
                .body("{\n"
                        + " \"cuisine\": \"Mediterranean\",\n"
                        + " \"cuisines\": [ \n"
                        + "         \"Mediterranean\",\n"
                        + "        \"European\",\n"
                        + "        \"Italian\",\n"
                        + " ], \n"
                        + " \"confidence\": \"0.0\" \n"
                        + "}")
                .when()
                .post(baseUri + "recipes/cuisine")
                .then()
                .spec(responseSpecification)
                .and()
                .body("cuisine", is("Mediterranean"));
    }

    @Test
    public void postFoodIngredients() {
        given().spec(requestSpecification)
                .queryParam("language", "en")
                .body("{\n"
                        + " \"ingredients\": [ \n"
                        + "         \"2 apples\",\n"
                        + "        \"1 cup cheese\",\n"
                        + "        \"1 glasses of wine\",\n"
                        + " ], \n"
                        + "}")
                .when()
                .post(baseUri + "food/ingredients/glycemicLoad")
                .then()
                .spec(responseSpecification)
                .and()
                .body("status", is("success"));

    }

    @Test
    public void postFoodDetect(){
        given().spec(requestSpecification)
                .when()
                .formParam("title", "I like to eat delicious tacos.")
                .post(baseUri + "food/detect")
                .then()
                .spec(responseSpecification);

    }
}
