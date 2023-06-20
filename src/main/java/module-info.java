module com.project.zuev.pharmacymangementsystem {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.project.zuev.pharmacymangementsystem to javafx.fxml;
    exports com.project.zuev.pharmacymangementsystem;
}