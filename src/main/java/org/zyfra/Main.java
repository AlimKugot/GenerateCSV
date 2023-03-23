package org.zyfra;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zyfra.model.CSVObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Класс с точкой входа и алгоритмом
 */
@SpringBootApplication
public class Main {

    /**
     * Логер. Его настройки прописаны в resources/logback.xml
     */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * Путь до csv файла
     */
    public static String csvFilePath = System.getenv("CSV_FILE_PATH");

    /**
     * Сколько раз повторяем первую строчку.
     * String переменная парсится потом в Long (2^64-1)
     */
    public static String repeatFirstLineTimes = System.getenv("REPEAT_FIRST_LINE_TIMES");

    /**
     * Точка входа
     *
     * @param args рудименты (аргументы для старта из командной строки), можно игнорить
     */
    public static void main(String[] args) {
        logger.info("Starting");

        Main main = new Main();
        CSVObject csvObject = main.initVariables(csvFilePath, repeatFirstLineTimes);
        if (csvObject != null) {
            main.writeToCSVFile(csvObject);
        }

        logger.info("Ending");
    }

    /**
     * Инициализирует переменные для CSVObject
     *
     * @see CSVObject
     */
    public CSVObject initVariables(String csvFilePath, String dataRepeatString) {
        logger.info("Initialising variables for {} {} times", csvFilePath, dataRepeatString);
        File csvFile = new File(csvFilePath);
        String[] header = readCSVLine(csvFile, 0);
        String[] data = readCSVLine(csvFile, 1);
        long dataRepeat = 1L;

        try {
            dataRepeat = Long.parseLong(dataRepeatString);
        } catch (NumberFormatException nfe) {
            logger.warn("REPEAT_FIRST_LINE_TIMES=1 (by default)");
        }

        return CSVObject.builder()
                .header(header)
                .dataFirstLine(data)
                .dataFirstLineRepeatTimes(dataRepeat)
                .build();
    }

    /**
     * Позволяет прочитать строку из CSV файла
     *
     * @param file      Откуда читаем
     * @param lineIndex индекс с данными (нулевая линия - это header)
     * @return массив из значений в строке
     */
    public String[] readCSVLine(File file, int lineIndex) {
        try (var fr = new FileReader(file, StandardCharsets.UTF_8);
             var reader = new CSVReader(fr)) {

            String[] line = null;
            for (int i = 0; i <= lineIndex; i++) {
                line = reader.readNext();
            }
            return line;
        } catch (IOException e) {
            logger.error("Error reading csv file {}", file.getAbsolutePath(), e);
        } catch (CsvValidationException e) {
            logger.error("CSV format reading error", e);
        }
        return null;
    }

    public void writeToCSVFile(CSVObject csvObject) {
        try (var fw = new FileWriter(csvObject.getFile());
             var writer = new CSVWriter(fw)) {

            logger.info("starting writing to csv {}", csvObject);

            // add header
            writer.writeNext(csvObject.getHeader());
            logger.info("added header");

            // add data to csv
            writer.writeNext(csvObject.getDataFirstLine());
            logger.info("added data");

            // repeat data
            logger.info("starting adding first line {} times to csv", csvObject.getDataFirstLineRepeatTimes());
            for (long i = 0; i < csvObject.getDataFirstLineRepeatTimes(); i++) {
                writer.writeNext(csvObject.getDataFirstLine());
            }
        } catch (IOException e) {
            logger.error("Failed to write to file {}", csvObject.getFile(), e);
        }
    }
}