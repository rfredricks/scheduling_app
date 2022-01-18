package model;

/** The Contact class represents contacts.
    @author Rebecca Fredricks */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    public Contact(int id, String name, String email){
        this.contactId = id;
        this.contactName = name;
        this.contactEmail = email;
    }

    /** Get contact id.
        @return the contact id */
    public int getContactId() {
        return contactId;
    }

    /** Set contact id.
        @param contactId the contact id */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Get contact name.
        @return contact name */
    public String getContactName() {
        return contactName;
    }

    /** Set contact name.
        @param contactName the contact name */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Get contact email.
        @return contact email */
    public String getContactEmail() {
        return contactEmail;
    }

    /** Set contact email.
        @param contactEmail the contact email */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /** To String method for contacts.
        @return contact as String */
    @Override
    public String toString(){
        return contactName;
    }
}
