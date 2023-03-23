package org.zyfra.model;

import lombok.*;

import java.io.File;

@Data
@Builder
public class CSVObject {

    private File file;
    private String[] header;
    private String[] dataFirstLine;
    private Long dataFirstLineRepeatTimes;
}
