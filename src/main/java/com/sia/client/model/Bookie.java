package com.sia.client.model;

import java.util.Objects;

public class Bookie implements java.io.Serializable {

    private final int bookie_id;
    String name;
    String shortname;
    String website;
    String mlbmoneytype;

    public Bookie(int bookie_id, String name, String shortname, String website, String mlbmoneytype) {

        this.bookie_id = bookie_id;
        this.name = name;
        this.shortname = shortname;
        this.website = website;
        this.mlbmoneytype = mlbmoneytype;
    }
    public int getBookie_id() {
        return bookie_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getMlbmoneytype() {
        return mlbmoneytype;
    }

    public void setMlbmoneytype(String mlbmoneytype) {
        this.mlbmoneytype = mlbmoneytype;
    }

    public String toString() {
        return getName();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookie bookie = (Bookie) o;
        return bookie_id == bookie.bookie_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookie_id);
    }
}