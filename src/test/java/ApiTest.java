import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;

public class ApiTest {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend/";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given()
                .log().all()
                .when()
                .get("todo")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured.given()
                .log().all()
                .body("{\n" +
                        "    \"task\": \"Teste via api\",\n" +
                        "    \"dueDate\": \"" + LocalDate.now() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("todo")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .log().all();
    }

    @Test
    public void naoDeveAdicionarTarefaComDataPassada() {
        RestAssured.given()
                .log().all()
                .body("{\n" +
                        "    \"task\": \"Teste via api\",\n" +
                        "    \"dueDate\": \"" + LocalDate.now().minusDays(1) + "\"\n" +
                        "  }")
                .contentType(ContentType.JSON)
                .when()
                .post("todo")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", CoreMatchers.is("Due date must not be in past"))
                .log().all();
    }
}
