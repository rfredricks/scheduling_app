package model;

/** The FirstLevelDivision class represents first level divisions.
    @author Rebecca Fredricks */
public class FirstLevelDivision {

    private int divisionId;
    private String division;
    private int countryId;
    public Country country;

    public FirstLevelDivision(int divid, String divname, Country co){
        this.divisionId = divid;
        this.division = divname;
        this.country = co;
        this.countryId = co.getCountry_ID();
    }

    /** Get division id.
        @return the division id */
    public int getDivisionId() {
        return divisionId;
    }

    /** Set division id.
        @param divisionId the division id */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Get division name.
        @return the division name */
    public String getDivision() {
        return division;
    }

    /** Set division name.
        @param division the division name */
    public void setDivision(String division) {
        this.division = division;
    }

    /** Get country id.
        @return the country id */
    public int getCountryId(){
        return countryId;
    }

    /** Set country id.
        @param id the country id */
    public void setCountryId(int id){ this.countryId = id; }

    /** Get country.
        @return the country */
    public Country getCountry() { return country; }

    /** Set country.
        @param co the country */
    public void setCountry(Country co) { this.country = co; }

    /** To String method for first level division.
        @return first level division as string */
    @Override
    public String toString(){
        return (this.division);
    }
}
