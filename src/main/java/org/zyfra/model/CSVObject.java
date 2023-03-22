package org.zyfra.model;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class CSVObject {

    private final File file;
    private final String[] header;
    private final String[] firstLineData;
    private final Long repeatFirstLineDataTimes;
}
