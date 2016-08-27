
package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.entity.Email;
import com.alexstyl.specialdates.entity.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * I'm an idiot
 *
 * @deprecated
 */
final public class MegaContact extends DeviceContact {

    public MegaContact(Long id, String displayName, String lookupKey) {
        super(id, DisplayName.from(displayName), lookupKey);
        phones = new ArrayList<Phone>();
        emails = new ArrayList<Email>();
    }

    private List<Phone> phones;
    private List<Email> emails;

    /**
     * Returns whether this contact contains at least one phone number
     */
    public boolean hasPhones() {
        return !phones.isEmpty();
    }

    /**
     * Returns whether this contact contains at least one mail address
     */
    public boolean hasEmails() {
        return !emails.isEmpty();
    }

    public void addPhone(String number, Integer type, String label) {
        this.phones.add(new Phone(number, type, label));

    }

    public void addEmail(String address, Integer type, String label) {
        this.emails.add(new Email(address, type, label));

    }

    public List<Phone> getPhones() {
        return this.phones;
    }

    public List<Email> getEmails() {
        return this.emails;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

}
