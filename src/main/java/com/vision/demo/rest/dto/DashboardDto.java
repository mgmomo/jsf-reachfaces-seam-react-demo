package com.vision.demo.rest.dto;

import java.util.List;

public class DashboardDto {

    private long totalPersons;
    private long totalLocations;
    private long activeLocations;
    private long inactiveLocations;
    private long personsWithNoLocations;
    private double avgLocationsPerPerson;
    private List<StateCount> locationStateDistribution;
    private List<LocationPersonCount> personsPerLocation;
    private List<PersonDto> recentPersons;
    private List<LocationDto> recentLocations;

    public DashboardDto() {
    }

    public long getTotalPersons() {
        return totalPersons;
    }

    public void setTotalPersons(long totalPersons) {
        this.totalPersons = totalPersons;
    }

    public long getTotalLocations() {
        return totalLocations;
    }

    public void setTotalLocations(long totalLocations) {
        this.totalLocations = totalLocations;
    }

    public long getActiveLocations() {
        return activeLocations;
    }

    public void setActiveLocations(long activeLocations) {
        this.activeLocations = activeLocations;
    }

    public long getInactiveLocations() {
        return inactiveLocations;
    }

    public void setInactiveLocations(long inactiveLocations) {
        this.inactiveLocations = inactiveLocations;
    }

    public long getPersonsWithNoLocations() {
        return personsWithNoLocations;
    }

    public void setPersonsWithNoLocations(long personsWithNoLocations) {
        this.personsWithNoLocations = personsWithNoLocations;
    }

    public double getAvgLocationsPerPerson() {
        return avgLocationsPerPerson;
    }

    public void setAvgLocationsPerPerson(double avgLocationsPerPerson) {
        this.avgLocationsPerPerson = avgLocationsPerPerson;
    }

    public List<StateCount> getLocationStateDistribution() {
        return locationStateDistribution;
    }

    public void setLocationStateDistribution(List<StateCount> locationStateDistribution) {
        this.locationStateDistribution = locationStateDistribution;
    }

    public List<LocationPersonCount> getPersonsPerLocation() {
        return personsPerLocation;
    }

    public void setPersonsPerLocation(List<LocationPersonCount> personsPerLocation) {
        this.personsPerLocation = personsPerLocation;
    }

    public List<PersonDto> getRecentPersons() {
        return recentPersons;
    }

    public void setRecentPersons(List<PersonDto> recentPersons) {
        this.recentPersons = recentPersons;
    }

    public List<LocationDto> getRecentLocations() {
        return recentLocations;
    }

    public void setRecentLocations(List<LocationDto> recentLocations) {
        this.recentLocations = recentLocations;
    }

    public static class StateCount {
        private String state;
        private String label;
        private long count;

        public StateCount() {
        }

        public StateCount(String state, String label, long count) {
            this.state = state;
            this.label = label;
            this.count = count;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    public static class LocationPersonCount {
        private String locationName;
        private long personCount;

        public LocationPersonCount() {
        }

        public LocationPersonCount(String locationName, long personCount) {
            this.locationName = locationName;
            this.personCount = personCount;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public long getPersonCount() {
            return personCount;
        }

        public void setPersonCount(long personCount) {
            this.personCount = personCount;
        }
    }
}
