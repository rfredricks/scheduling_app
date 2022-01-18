package model;

/** The customer class represents customers.
    @author Rebecca Fredricks */
public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    public FirstLevelDivision firstLevelDivision;

    public Customer(String customerName, String address, String postalCode, String phone, int divisionId){
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }
    public Customer(String customerName, String address, String postalCode, String phone, FirstLevelDivision fld){
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivision = fld;
        this.divisionId = fld.getDivisionId();
    }

    public Customer(int id, String customerName, String address, String postalCode, String phone, FirstLevelDivision fld){
        this.customerId = id;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivision = fld;
        this.divisionId = fld.getDivisionId();
    }

    /** Get customer id.
        @return the customer id */
    public int getCustomerId() {
        return customerId;
    }

    /** Set customer id.
        @param customerId the customer id */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Get customer name.
        @return the customer name */
    public String getCustomerName() {
        return customerName;
    }

    /** Set customer name.
        @param customerName the customer name */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Get address.
        @return the address */
    public String getAddress() {
        return address;
    }

    /** Set address.
        @param address the address */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Get postal code.
        @return the postal code */
    public String getPostalCode() {
        return postalCode;
    }

    /** Set postal code.
        @param postalCode the postal code */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Get phone.
        @return phone */
    public String getPhone() {
        return phone;
    }

    /** Set phone.
        @param phone the phone number */
    public void setPhone(String phone) {
        this.phone = phone;
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

    /** Get first level division.
        @return the first level division */
    public FirstLevelDivision getFirstLevelDivision(){
        return firstLevelDivision;
    }

    /** Get country.
        @return the country */
    public Country getCountry(){
        return firstLevelDivision.getCountry();
    }

    /** To String method for customers.
        @return customer as String */
    @Override
    public String toString(){
        return (this.customerName);
    }
}
