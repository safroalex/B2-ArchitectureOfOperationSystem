module com.safroalex.simulation {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                        
    opens com.safroalex.simulation to javafx.fxml;
    exports com.safroalex.simulation;
}