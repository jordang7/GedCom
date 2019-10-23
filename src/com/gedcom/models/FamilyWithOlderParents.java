package com.gedcom.models;

import java.util.List;
import java.util.Optional;

/**
 * Created by Sri on 10/23/2019.
 */
public class FamilyWithOlderParents {
    Family family;
    Optional<Individual> olderHusband;
    Optional<Individual> olderWife;

    public FamilyWithOlderParents() {
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Optional<Individual> getOlderHusband() {
        return olderHusband;
    }

    public void setOlderHusband(Optional<Individual> olderHusband) {
        this.olderHusband = olderHusband;
    }

    public Optional<Individual> getOlderWife() {
        return olderWife;
    }

    public void setOlderWife(Optional<Individual> olderWife) {
        this.olderWife = olderWife;
    }
}
