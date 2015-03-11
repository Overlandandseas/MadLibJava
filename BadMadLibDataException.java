public class BadMadLibDataException extends Exception{
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_RESET = "\u001B[0m";

  public BadMadLibDataException() throws Exception{
    throw new Exception("You threw a bad Exception. Is this too meta?");
  }

    public BadMadLibDataException(String message)
    {
      //super(ANSI_RED + message + ANSI_RESET);
    	super(message);
    }
}
