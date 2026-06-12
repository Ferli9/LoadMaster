module com.mycompany.softwarechido {
    requires javafx.controls;
    requires javafx.fxml;
requires java.sql;
    opens com.mycompany.softwarechido to javafx.fxml;
    exports com.mycompany.softwarechido;
    requires java.net.http;
    requires org.apache.pdfbox;
   
    opens com.loadmaster.model to javafx.base;
    

    opens com.loadmaster.gui to javafx.fxml;
}
