package com.vision.demo.rest.dto;

import com.vision.demo.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private List<LocationDto> locations;
    private List<Long> locationIds;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public PersonDto() {
    }

    public static PersonDto fromEntity(Person p) {
        PersonDto dto = new PersonDto();
        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        if (p.getDateOfBirth() != null) {
            dto.setDateOfBirth(new SimpleDateFormat(DATE_FORMAT).format(p.getDateOfBirth()));
        }
        List<LocationDto> locs = new ArrayList<LocationDto>();
        if (p.getLocations() != null) {
            for (com.vision.demo.model.Location loc : p.getLocations()) {
                locs.add(LocationDto.fromEntity(loc));
            }
        }
        dto.setLocations(locs);
        return dto;
    }

    public Person toEntity() {
        Person p = new Person();
        p.setId(this.id);
        p.setFirstName(this.firstName);
        p.setLastName(this.lastName);
        if (this.dateOfBirth != null && !this.dateOfBirth.isEmpty()) {
            try {
                p.setDateOfBirth(new SimpleDateFormat(DATE_FORMAT).parse(this.dateOfBirth));
            } catch (ParseException e) {
                // leave null if unparseable
            }
        }
        return p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }

    public List<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Long> locationIds) {
        this.locationIds = locationIds;
    }
}
