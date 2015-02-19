public class BadMadLibDataException extends Exception{

  public BadMadLibDataException() throws Exception{
    throw new Exception("You threw a bad Exception. Is this too meta?");
  }

    public BadMadLibDataException(String message)
    {
      super(message);
    }
}
