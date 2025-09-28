module packs.pack {
    requires javafx.controls;
    exports packs.pack;
    
    opens humanos to javafx.base;
    opens ventanas;
}
  
