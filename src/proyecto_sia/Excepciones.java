package proyecto_sia;

public class Excepciones {
	
	public static class RutInvalidoException extends Exception {
		private static final long serialVersionUID = 1L;
        public RutInvalidoException(String mensaje) {
            super(mensaje);
        }
    }
	
	public static class CapitalInvalidoException extends Exception {
	    private static final long serialVersionUID = 1L;
	    public CapitalInvalidoException(String mensaje) { 
	    	super(mensaje); 
	    }
	}

}
