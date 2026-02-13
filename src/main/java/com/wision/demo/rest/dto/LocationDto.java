package com.wision.demo.rest.dto;

import com.wision.demo.model.Location;
import com.wision.demo.model.LocationState;

public class LocationDto {

    private Long id;
    private String locationName;
    private String address;
    private String zipCode;
    private String state;
    private String stateLabel;

    public LocationDto() {
    }

    public static LocationDto fromEntity(Location loc) {
        LocationDto dto = new LocationDto();
        dto.setId(loc.getId());
        dto.setLocationName(loc.getLocationName());
        dto.setAddress(loc.getAddress());
        dto.setZipCode(loc.getZipCode());
        if (loc.getState() != null) {
            dto.setState(loc.getState().name());
            dto.setStateLabel(loc.getState().getLabel());
        }
        return dto;
    }

    public Location toEntity() {
        Location loc = new Location();
        loc.setId(this.id);
        loc.setLocationName(this.locationName);
        loc.setAddress(this.address);
        loc.setZipCode(this.zipCode);
        if (this.state != null) {
            loc.setState(LocationState.valueOf(this.state));
        }
        return loc;
    }

    public void applyTo(Location loc) {
        loc.setLocationName(this.locationName);
        loc.setAddress(this.address);
        loc.setZipCode(this.zipCode);
        if (this.state != null) {
            loc.setState(LocationState.valueOf(this.state));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }
}
