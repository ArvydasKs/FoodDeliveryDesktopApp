module com.example.courseprifs {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;
    requires java.naming;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires jbcrypt;

    opens com.example.courseprifs to javafx.fxml, org.hibernate.orm.core, jakarta.persistence;
    exports com.example.courseprifs;
    opens com.example.courseprifs.fxControllers to javafx.fxml;
    exports com.example.courseprifs.fxControllers;
    opens com.example.courseprifs.model to org.hibernate.orm.core;
    exports com.example.courseprifs.model;
}