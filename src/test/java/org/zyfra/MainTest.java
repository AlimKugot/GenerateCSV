package org.zyfra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zyfra.model.CSVObject;

import java.io.File;

public class MainTest {
    private CSVObject csvObject;
    private Main main;

    @BeforeEach
    public void init() {
        main = new Main();
        csvObject = CSVObject.builder()
                .header(new String[]{"header1", "header2", "header3"})
                .dataFirstLine(new String[]{"data1", "data2", "data3"})
                .dataFirstLineRepeatTimes(5L)
                .build();
    }

    @Test
    public void initVariables_PassNumber() {
        CSVObject csvObjectActual = main.initCSVVariables("","110");
        Assertions.assertEquals(110, csvObjectActual.getDataFirstLineRepeatTimes());
    }

    @Test
    public void initVariables_DonNotPassNumber() {
        CSVObject csvObjectActual = main.initCSVVariables("","");
        Assertions.assertNotNull(csvObjectActual.getDataFirstLineRepeatTimes());
    }

    @Test
    public void readCSVLine_ReadHeader() {
        File testReadFile = new File("src/test/resources/test-data-to-read.csv");

        String header = "_Name;_Select(x);_ValueType;_Delete(x);_NewName;Accuracy;Archive;CalcAggregates;CalculationAlgorithmExpression;Compression;CompressionDeviation;CompressionTimeDeadBand;CompressionTimespan;CompressionType;Convers;Description;DictId;DictSource;EngUnits;Hi;InstrumentTag;Interpolation;Output;PointSource;Reception;SaveAddTs;SaveQuality;Scan;ScanClass;SecurityGroups;SourceCompression;SourceCompressionDeviation;SourceCompressionTimeDeadBand;SourceCompressionTimespan;SourceCompressionType;SourceTag;Span;SquareRoot;TTL;TotalCode;Zero";
        String[] headerExpected = header.split(";");
        String[] headerActual = main.readCSVLine(testReadFile, 0);

        Assertions.assertArrayEquals(headerExpected, headerActual);
    }

    @Test
    public void readCSVLine_IncorrectFilePath() {
        File testReadFile = new File("/not/exists.file");
        Assertions.assertDoesNotThrow(() -> main.readCSVLine(testReadFile, 0));
    }
}
