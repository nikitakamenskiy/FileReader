package file.reader;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.ClipboardConditions.content;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileReaderTest {

    ClassLoader cl = FileReaderTest.class.getClassLoader();

    @Test
    public void testReadFile() throws Exception {


      try (InputStream inputStream = cl.getResourceAsStream("Archives.zip");

        ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
          ZipEntry zipEntry;
          while ((zipEntry = zipInputStream.getNextEntry()) != null) {


              if (zipEntry.getName().endsWith(".csv")) {
                  CSVReader reader = new CSVReader(new InputStreamReader(zipInputStream));
                  List<String[]> content = reader.readAll();
                  assertThat(content.get(0)[1]).contains("Doe");

              } else if (zipEntry.getName().endsWith(".xlsx")) {
                  XLS content = new XLS(zipInputStream);
                  assertThat(
                  content.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue()).contains("1.1. Финансовые операции (S1)");

              } else if (zipEntry.getName().endsWith(".pdf")) {
                  PDF content = new PDF(zipInputStream);
                  assertThat(content.text).contains("Windows & Linux keymap");
                  
              }


          }

      }

    }
}
