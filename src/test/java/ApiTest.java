import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.zubova.pojo.Entity;
import org.zubova.util.PropertiesLoader;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
import static org.zubova.service.ApiService.*;

class ApiTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = PropertiesLoader.load().getProperty("api.url");
    }

    @AfterAll
    static void tearDown() {
        List<Entity> allEntities = given()
                .when()
                .get(GET_ALL_PATH)
                .then()
                .extract()
                .body()
                .jsonPath().getList("entity", Entity.class);

        if (allEntities.isEmpty()) return;

        for (var entity : allEntities) {
            given()
                    .when()
                    .delete(DELETE_PATH, entity.getId())
                    .then()
                    .statusCode(SC_NO_CONTENT);
        }
    }

    @Test
    @DisplayName("Тест на создание сущности")
    void createEntityTest() {
        var entity = Entity.builder().build();
        var id = createAndGetId(entity);

        Assertions.assertTrue(id > 0, "Сущность не была создана.");
    }

    @Test
    @DisplayName("Тест на удаление сущности")
    void deleteEntityTest() {
        var entity = Entity.builder().build();
        var id = createAndGetId(entity);

        given()
                .when()
                .delete(DELETE_PATH, id)
                .then()
                .statusCode(SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Тест на получение сущности по id")
    void getEntityTest() {
        var entity = Entity.builder().build();
        var id = createAndGetId(entity);

        var resultEntity = getEntityBy(id);

        Assertions.assertEquals(entity.getTitle(), resultEntity.getTitle(),
                "Поле title не совпадает с ожидаемым значением");
        Assertions.assertEquals(entity.getVerified(), resultEntity.getVerified(),
                "Поле verified не совпадает с ожидаемым значением");
        Assertions.assertEquals(entity.getAddition().getAdditionalInfo(), resultEntity.getAddition().getAdditionalInfo(),
                "Поле additionalInfo не совпадает с ожидаемым значением");
        Assertions.assertEquals(entity.getAddition().getAdditionalNumber(), resultEntity.getAddition().getAdditionalNumber(),
                "Поле additionalNumber не совпадает с ожидаемым значением");
        Assertions.assertTrue(resultEntity.getImportantNumbers().containsAll(entity.getImportantNumbers()),
                "Поле importantNumbers не совпадает с ожидаемым значением");
    }

    @Test
    @DisplayName("Тест на получение всех существующих сущностей.")
    void getAllEntitiesTest() {
        var entity1 = Entity.builder().build();
        var entity2 = Entity.builder().build();
        createAndGetId(entity1);
        createAndGetId(entity2);

        given()
                .when()
                .get(GET_ALL_PATH)
                .then()
                .statusCode(SC_OK)
                .assertThat()
                .body("entity", notNullValue());
    }

    @Test
    @DisplayName("Тест на обновление сущности")
    void patchEntity() {
        var entity = Entity.builder().build();
        var id = createAndGetId(entity);

        var entityForPatch = getEntityBy(id);
        var patchedTitle = "Новый заголовок";
        entityForPatch.setTitle(patchedTitle);

        given()
                .contentType(ContentType.JSON)
                .body(entityForPatch)
                .when()
                .patch(PATCH_PATH, id)
                .then()
                .statusCode(SC_NO_CONTENT);
    }
}
