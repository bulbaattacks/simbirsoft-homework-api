import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.*;
import org.zubova.pojo.Entity;
import org.zubova.util.PropertiesLoader;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

class ApiTest {
    private static final String CREATE_PATH = "/api/create";
    private static final String DELETE_PATH = "/api/delete/%s";
    private static final String GET_PATH = "/api/get/%s";
    private static final String GET_ALL_PATH = "/api/getAll";
    private static final String PATCH_PATH = "/api/patch/%s";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = PropertiesLoader.load().getProperty("api.url");
    }

    @AfterEach
    void tearDown() {
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
                    .delete(DELETE_PATH.formatted(entity.getId()))
                    .then()
                    .statusCode(204);
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
                .delete(DELETE_PATH.formatted(id))
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Тест на получение сущности по id")
    void getEntityTest() {
        var entity = Entity.builder().build();
        var id = createAndGetId(entity);

        var resultEntity = getEntityBy(id);

        Assertions.assertEquals(entity.getTitle(), resultEntity.getTitle());
        Assertions.assertEquals(entity.getVerified(), resultEntity.getVerified());
        Assertions.assertEquals(entity.getAddition().getAdditionalInfo(), resultEntity.getAddition().getAdditionalInfo());
        Assertions.assertEquals(entity.getAddition().getAdditionalNumber(), resultEntity.getAddition().getAdditionalNumber());
        Assertions.assertTrue(resultEntity.getImportantNumbers().containsAll(entity.getImportantNumbers()));
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
                .statusCode(200)
                .assertThat()
                .body("entity", hasSize(2));
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
                .patch(PATCH_PATH.formatted(id))
                .then()
                .statusCode(204);
    }

    private static Integer createAndGetId(Entity entity) {
        RestAssured.registerParser("text/plain", Parser.JSON);
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.TEXT)
                .body(entity)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(200)
                .extract()
                .as(Integer.class);
    }

    private static Entity getEntityBy(Integer id) {
        return given()
                .when()
                .get(GET_PATH.formatted(id))
                .then()
                .statusCode(200)
                .extract()
                .as(Entity.class);
    }
}
