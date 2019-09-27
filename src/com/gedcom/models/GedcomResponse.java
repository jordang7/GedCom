package com.gedcom.models;

import java.util.List;

public class GedcomResponse {
    private List<String> validLines;
    private List<String> erroLines;

    public GedcomResponse(List<String> validLines, List<String> erroLines) {
        this.validLines = validLines;
        this.erroLines = erroLines;
    }

    public List<String> getValidLines() {
        return validLines;
    }

    public void setValidLines(List<String> validLines) {
        this.validLines = validLines;
    }

    public List<String> getErroLines() {
        return erroLines;
    }

    public void setErroLines(List<String> erroLines) {
        this.erroLines = erroLines;
    }
}
