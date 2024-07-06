module pls {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media; 
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires bcrypt;

	
	opens application to javafx.graphics, javafx.fxml;
    exports application;
}
