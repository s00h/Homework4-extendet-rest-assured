package org.example;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AppTestPost {
    private final String apiKey = "403374500dbd4d1bb8587033294d5bad";

    @Test
    public void post1() {
        String id = given()
                .queryParam("hash", "dc9601b2a37227f264b53fe6bfe8261d1415f14d")
                .queryParam("apiKey", apiKey)
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
                .post("https://api.spoonacular.com/mealplanner/myExample/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        System.out.println("ID is: " + id);
        given()
                .queryParam("hash", "dc9601b2a37227f264b53fe6bfe8261d1415f14d")
                .queryParam("apiKey", apiKey)
                .delete("https://api.spoonacular.com/mealplanner/myExample/items/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("status", is("success"));
    }

    @Test
    public void post2() {
        given()
                .queryParam("language", "en")
                .queryParam("includeNutrition", "false")
                .queryParam("includeTaste", "false")
                .queryParam("apiKey", apiKey)
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
                .post("https://api.spoonacular.com/recipes/analyze")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("vegetarian", is(false));
    }

    @Test
    public void post3() {
        given()
                .queryParam("title", "Pork roast with green beans")
                .queryParam("ingredientList", "3 oz pork shoulder")
                .queryParam("language", "en")
                .queryParam("apiKey", apiKey)
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
                .post("https://api.spoonacular.com/recipes/cuisine")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("cuisine", is("Mediterranean"));
    }
    @Test
    public void post4() {
        given()
                .queryParam("language", "en")
                .queryParam("apiKey", apiKey)
                .body("{\n"
                        + " \"ingredients\": [ \n"
                        + "         \"2 apples\",\n"
                        + "        \"1 cup cheese\",\n"
                        + "        \"1 glasses of wine\",\n"
                        + " ], \n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/food/ingredients/glycemicLoad")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("status", is("success"));

    }
@Test
    public void post5Negative(){
given()
        .queryParam("apiKey", apiKey)
        .queryParam("locale", "en_US")
        .body( "[ \n"
                + " { \n"
                + " \"title\": \"Kroger Vitamin A & D Reduced Fat 2% Milk\" , \n"
                + " \"upc\": \" \", \n"
                + " \"plu_code\": \" \", \n"
                + " },\n"
        + "]")
        .when()
        .post("https://api.spoonacular.com/food/products/classify1Batch")
        .then()
        .assertThat()
        .statusCode(405);
   }
}
