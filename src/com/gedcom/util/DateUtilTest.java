package com.gedcom.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {
    protected String dateBefore;
    protected  String dateAfter;
    protected String format;
    DateUtil dateUtil = new DateUtil();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        dateAfter = "2019-12-31";
        dateBefore = "2019-10-01";
        format = "yyyy-MM-dd";
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        dateBefore = null;
        dateAfter = null;
        format = null;
    }


    @Test
    void isDateBeforeCurrentDate() {
       assertEquals(true,dateUtil.isDateBeforeCurrentDate(dateBefore,format));
        assertEquals(false,dateUtil.isDateBeforeCurrentDate(dateAfter,format));

    }

}