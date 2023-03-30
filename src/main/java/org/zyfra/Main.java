package org.zyfra;

import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zyfra.exception.LocalException;
import org.zyfra.model.CSVObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Класс с точкой входа и алгоритмом
 */
public class Main {

    /**
     * Логер. Его настройки прописаны в resources/logback.xml
     */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * Откуда берутся данные из csv файла
     */
    public static String csvInputFilePath = System.getenv("INPUT_CSV_FILE");

    /**
     * Куда сохраняется вывод работы программы
     */
    public static String csvOutputFilePath = System.getenv("OUTPUT_CSV_FILE");


    /**
     * Сколько раз повторяем первую строчку.
     * String переменная парсится потом в Long (2^64-1)
     */
    public static String repeatFirstLineTimes = System.getenv("REPEAT_CSV_LINE_TIMES");

    /**
     * Точка входа
     *
     * @param args рудименты (аргументы для старта из командной строки), можно игнорить
     */
    public static void main(String[] args) {
        logger.info("Starting");
        try {
            Main main = new Main();
            CSVObject csvObject = main.initCSVVariables(csvInputFilePath, repeatFirstLineTimes);
            if (csvObject != null) {
                main.writeToCSVFile(csvObject, csvOutputFilePath);
            }

            logger.info("Ending");
        } catch (LocalException le) {
            logger.error("LocalException", le);
        }
    }

    /**
     * Инициализирует переменные для CSVObject
     *
     * @see CSVObject
     */
    public CSVObject initCSVVariables(String csvInputFilePath, String dataRepeatString) {
        logger.info("Initialising variables for {} {} times", csvInputFilePath, dataRepeatString);
        if (csvInputFilePath == null || csvInputFilePath.isEmpty()) {
            throw new LocalException("CsvInputPath is null or empty");
        }

        File csvFile = new File(csvInputFilePath);
        String[] header = readCSVLine(csvFile, 0);
        String[] data = readCSVLine(csvFile, 1);
        long dataRepeat;

        try {
            dataRepeat = Long.parseLong(dataRepeatString);
        } catch (NumberFormatException nfe) {
            throw new LocalException("incorrect number format for data repeat: " + dataRepeatString);
        }

        return CSVObject.builder()
                .file(csvFile)
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
             var reader = new CSVReaderBuilder(fr)
                     .withCSVParser(new CSVParserBuilder()
                             .withSeparator(';')
                             .build())
                     .build()) {

            String[] line = null;
            for (int i = 0; i <= lineIndex; i++) {
                line = reader.readNextSilently();
            }

            return line;
        } catch (IOException e) {
            throw new LocalException("Error reading csv file " + file.getAbsolutePath());
        }
    }

    /**
     * Выводит результат работы в указанный файл
     * @param csvObject ооп объект с данными из csv
     * @param outputFilePath файл, где будет лежать вывод
     */
    public void writeToCSVFile(CSVObject csvObject, String outputFilePath) {
        try (var fw = new FileWriter(outputFilePath);
             var writer = new CSVWriter(fw,
                     ';',
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.NO_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            logger.info("starting writing to csv {}", csvObject);

            // add header
            writer.writeNext(csvObject.getHeader());
            logger.info("added header");

            // repeat data
            String[] line = csvObject.getDataFirstLine();
            // convert to "[""data""]"
            // todo: убрать этот костыль
            line[22] = line[22].substring(2, line[22].length() - 2);
            line[28] = line[28].substring(2, line[28].length() - 2);
            line[22] = "\"[\"\"" + line[22] + "\"\"]\"";
            line[28] = "\"[\"\"" + line[28] + "\"\"]\"";

            logger.info("starting adding first line {} times to csv", csvObject.getDataFirstLineRepeatTimes());
            for (long i = 1; i <= csvObject.getDataFirstLineRepeatTimes(); i++) {
                // Name
                line[0] = line[0].replaceAll("\\d+$", String.valueOf(i));
                // Name in source
                line[19] = line[19].replaceAll("\\d+$", String.valueOf(i));
                writer.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Failed to write to file {}", csvObject.getFile(), e);
        }
    }
}