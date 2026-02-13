package com.vision.demo.model;

public enum LocationState {

    ACTIVE("Active"),
    NOT_ACTIVE("Not Active");

    private final String label;

    LocationState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
