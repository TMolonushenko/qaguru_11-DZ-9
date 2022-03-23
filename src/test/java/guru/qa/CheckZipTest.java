package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.domain.Student;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckZipTest {
    ClassLoader classLoader = getClass().getClassLoader();
    private static final String
            pdfFileName = "Canon LEGRIA.pdf",
            xlsFileName = "price-list.xls",
            csvFileName = "transaction_statuses.csv";


    @Test
    void readZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/homework.zip");
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().contains("pdf")) {
                assertThat(entry.getName()).isEqualTo(pdfFileName);
                pdfTest(zipFile.getInputStream(entry));
            } else if (entry.getName().contains("xlsx")) {
                assertThat(entry.getName()).isEqualTo(xlsFileName);
                xlsTest(zipFile.getInputStream(entry));
            } else if (entry.getName().contains("csv")) {
                assertThat(entry.getName()).isEqualTo(csvFileName);
                csvTest(zipFile.getInputStream(entry));
            }
        }
    }


    void pdfTest(InputStream file) throws Exception {
        // ZipFile zipFile = new ZipFile("src/test/resources/files/homework1.zip");
        //  ZipEntry zipEntry = zipFile.getEntry("Canon LEGRIA.pdf");
        // InputStream inputStream = zipFile.getInputStream(zipEntry);
        PDF pdf = new PDF(file);
        assertThat(pdf.text).contains("В настоящем руководстве");
    }


    void xlsTest(InputStream file) throws Exception {
        // ZipFile zipFile = new ZipFile("src/test/resources/files/homework1.zip");
        // ZipEntry zipEntry = zipFile.getEntry("price-list.xls");
        //  InputStream inputStream = zipFile.getInputStream(zipEntry);
        XLS xls = new XLS(file);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(1)
                .getCell(1)
                .getStringCellValue()).contains("Мебельный щит");


    }


    void csvTest(InputStream file) throws Exception {
        //ZipFile zipFile = new ZipFile("src/test/resources/files/homework1.zip");
        // ZipEntry zipEntry = zipFile.getEntry("transaction_statuses.csv");
        //  InputStream inputStream = zipFile.getInputStream(zipEntry);
        try (CSVReader reader = new CSVReader(new InputStreamReader(file))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(1)).contains(
                    "uuid;code;created_at;updated_at;deleted_at;;");
        }
    }

    @Test
    void jsonCommonTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/simple.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            assertThat(jsonObject.get("device").getAsString()).isEqualTo("phone");
            assertThat(jsonObject.get("soft").getAsJsonObject().get("system").getAsString()).isEqualTo("sms");
        }
    }

    @Test
    void jsonTapeTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/simple.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Student jsonObject = gson.fromJson(json, Student.class);
            assertThat(jsonObject.device).isEqualTo("phone");
            assertThat(jsonObject.soft.system).isEqualTo("sms");
        }
    }

}
