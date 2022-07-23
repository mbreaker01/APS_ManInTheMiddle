module com.mycompany.aps_maninthemiddle {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.aps_maninthemiddle to javafx.fxml;
    exports it.unisa.diem.aps.aps_maninthemiddle;
}
