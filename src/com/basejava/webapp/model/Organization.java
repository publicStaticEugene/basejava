package com.basejava.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final Link homePage;
    private final List<Position> positions;

    public Organization(String name, String url, List<Position> positions) {
        Objects.requireNonNull(positions, "positions must not be null");
        this.homePage = new Link(name, url);
        this.positions = positions;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(homePage, that.homePage) &&
                Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(homePage, positions);
    }

    @Override
    public String toString() {
        return String.format("Organization(%s, %s)", homePage, positions);
    }
}
