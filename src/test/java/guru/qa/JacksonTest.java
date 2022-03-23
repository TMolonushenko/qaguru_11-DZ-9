package guru.qa;

import guru.qa.domain.Student;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JacksonTest {
    String jsonFile = "src/test/resources/files/simple.json";

    @Test
    void jsonJacksonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(jsonFile);
        assertTrue(file.exists());
        Student student = objectMapper.readValue(file, Student.class);
        assertThat(student.device).isEqualTo("phone");
        assertThat(student.vendor).isEqualTo("xiaomi");
        assertThat(student.model).contains("mi8", "mi8pro");
        assertThat(student.soft.system).isEqualTo("sms");
    }
}
