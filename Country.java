package model;

/** The Country class represents countries.
    @author Rebecca Fredricks */
public class Country {

    private String country_name;
    private int country_ID;

    public Country(String name, int id){
        this.country_name = name;
        this.country_ID = id;
    }

    public String getCountry_name() {
        return country_name;
    }

    /** Set country name.
        @param country_name the country name */
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    /** Get country id.
        @return the country id */
    public int getCountry_ID() {
        return country_ID;
    }

    /** Set country id.
        @param country_ID the country id */
    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }

    /** To String method for Country.
        @return country as String */
    @Override
    public String toString(){
        return (this.country_name);
    }

}
