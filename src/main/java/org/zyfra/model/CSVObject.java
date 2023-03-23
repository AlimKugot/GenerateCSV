package org.zyfra.model;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class CSVObject {

    private File file;
    private String[] header;
    private String[] dataFirstLine;
    private Long dataFirstLineRepeatTimes;
}
