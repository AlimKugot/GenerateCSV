package org.zyfra;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zyfra.model.CSVObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс с точкой входа и алгоритмом
 */
public class Main {

    /**
     * Логер. Его настройки прописаны в resources/logback.xml
     */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * Путь до csv файла
     */
    private static final String CSV_FILE_PATH = System.getenv("CSV_FILE_PATH");

    /**
     * Сколько раз повторяем первую строчку.
     * String переменная парсится потом в Long (2^64-1)
     */
    private static final String REPEAT_FIRST_LINE_TIMES = System.getenv("REPEAT_FIRST_LINE_TIMES");

    /**
     * Точка входа
     * @param args рудименты (аргументы для старта из командной строки), можно игнорить
     */
    public static void main(String[] args) {
        logger.info("Starting");
        CSVObject csvObject = initVariables();
        if (csvObject != null) {
            writeToCSVFile(csvObject);
        }
        logger.info("Ending");
    }

    /**
     * Инициализирует переменные для CSVObject
     * @see CSVObject
     */
    public static CSVObject initVariables() {
        if (CSV_FILE_PATH == null) {
            logger.error("CSV_FILE_PATH is null");
            return null;
        }

        File csvFile = new File(CSV_FILE_PATH);
        String[] header = readCSVHeader(csvFile);
        String[] data = readCSVLine(csvFile);
        long repeatFirstLineTimes = 1L;

         try {
            repeatFirstLineTimes = Long.parseLong(REPEAT_FIRST_LINE_TIMES);
        } catch (NumberFormatException nfe) {
            logger.warn("REPEAT_FIRST_LINE_TIMES=1 (by default)");
        }

        return CSVObject.builder()
                .file(csvFile)
                .header(header)
                .firstLineData(data)
                .repeatFirstLineDataTimes(repeatFirstLineTimes)
                .build();
    }

    private static String[] readCSVHeader(File file) {
        return null;
    }

    private static String[] readCSVLine(File file) {
        return null;
    }

    private static void writeToCSVFile(CSVObject csvObject) {
        if (csvObject == null) {
            logger.error("CSVObject is null");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(csvObject.getFile());
             CSVWriter writer = new CSVWriter(fileWriter)) {

            logger.info("starting logging");

            // add header
            writer.writeNext(csvObject.getHeader());
            logger.info("added header");

            // add data to csv
            writer.writeNext(csvObject.getFirstLineData());
            logger.info("added data");

            // repeat data
            logger.info("starting adding first line {} times to csv", csvObject.getRepeatFirstLineDataTimes());
            for (long i = 0; i < csvObject.getRepeatFirstLineDataTimes(); i++) {
                writer.writeNext(csvObject.getFirstLineData());
            }
            logger.info("success repeating");

        } catch (IOException ioException) {
            logger.error("Failed to write to file {}", csvObject.getFile(), ioException);
        }
    }
}