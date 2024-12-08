package com.forero.school.infraestructure.adapter;

import java.math.BigDecimal;

public final class ConstantsRepository {
    public static final String TITLE = "List of records";
    public static final String RECORDS = "records";
    public static final String SUBJECTS = "subjects";
    public static final String FILE = "file";
    public static final String REGISTERED = "registered";
    public static final String NOTE = "note";
    public static final BigDecimal TOTAL_NOTES = BigDecimal.valueOf(3);
    public static final String CONCATENATE = "_";
    public static final String DEFAULT_VALUE = "";
    public static final float TABLE_WIDTH = 500.0f;
    public static final int MINI_VALOR = 0;
    public static final int NOTE_ONE_CELL_INDEX = 1;
    public static final int NOTE_TWO_CELL_INDEX = 2;
    public static final int NOTE_THREE_CELL_INDEX = 3;
    public static final BigDecimal MAX_NOTE = BigDecimal.valueOf(100);
    public static final float HEADER_MARGIN_LEFT = 50.0f;
    public static final float SPACE_CELLS = 1;
    public static final float MARGIN_TOP = 680;
    public static final int DECIMAL_PRECISION = 2;
    public static final float MARGIN_TOP_MIN = 50;
    public static final int FRONT_SIZE = 10;
    public static final int ROW_HEIGHT = 20;
    public static final float HEADER_MARGIN_TOP = 700.0f;
    public static final float HEADER_FONT_SIZE = 12.0f;
    public static final int TITLE_FONT_SIZE = 16;
    public static final float TITLE_X_OFFSET = 220f;
    public static final float TITLE_Y_OFFSET = 750f;
    public static final float DOCUMENT_CELL_WIDTH_ADJUSTMENT_FACTOR = 1.01f;

    static final String[] HEADERS = {"Student", "Name", "Surname", "Document", "Note1", "Note2", "Note3",
            "Average"};

    private ConstantsRepository() {
    }

}
