package addressBook.data;

import addressBook.models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactsData {
    public static ObservableList<Contact> getContactsList() {
        Contact c1 = new Contact();
        c1.setName("Emma");
        c1.setSurname("Watson");
        c1.setPhone("299 12 12");
        c1.setCompany("Mail ru Group");
        c1.setPosition("Dev Ops");
        c1.setMobilePhone("+375 (23) 344 52 34");
        c1.setAddress("г. Минск, ул. Янки Лучины, д. 38");

        Contact c2 = new Contact();
        c2.setName("Jhon");
        c2.setSurname("Poul");
        c2.setPhone("299 42 53");
        c2.setCompany("Yandex");
        c2.setPosition("Sale manager");
        c2.setMobilePhone("+375 (23) 454 22 98");
        c2.setAddress("г. Минск, Арена Сити");

        Contact c3 = new Contact();
        c3.setName("Larry");
        c3.setSurname("Page");
        c3.setPhone("299 35 17");
        c3.setCompany("Yandex");
        c3.setPosition("Java Developer Lead");
        c3.setMobilePhone("+375 (23) 583 22 35");
        c3.setAddress("г. Минск, Уручье");

        Contact c4 = new Contact();
        c4.setName("Michal");
        c4.setSurname("Brown");
        c4.setPhone("299 84 92");
        c4.setCompany("IBM Group");
        c4.setPosition("Test department");
        c4.setMobilePhone("+375 (23) 968 22 36");
        c4.setAddress("г. Минск, ул. Голубева");

        Contact c5 = new Contact();
        c5.setName("Brian");
        c5.setSurname("Rellson");
        c5.setPhone("299 52 95");
        c5.setCompany("Wargaming");
        c5.setPosition("Design Engineer");
        c5.setMobilePhone("+375 (23) 863 954 204");
        c5.setAddress("г. Минск, ул. Петруся бровки");

        Contact c6 = new Contact();
        c6.setName("Colly");
        c6.setSurname("Istland");
        c6.setPhone("299 32 17");
        c6.setCompany("ITechArt");
        c6.setPosition("HR recruiter");
        c6.setMobilePhone("+375 (23) 995 23 11");
        c6.setAddress("г. Минск, пр. Независимости, д. 64");

        Contact c7 = new Contact();
        c7.setName("Dolly");
        c7.setSurname("Patterson");
        c7.setPhone("299 33 55");
        c7.setCompany("Insurance Agency");
        c7.setPosition("IT Specialist");
        c7.setMobilePhone("+375 (23) 345 35 34");
        c7.setAddress("г. Минск, Степянка");

        return FXCollections.observableArrayList(
            c1, c2, c3, c4, c5, c6, c7
        );
    }
}
