package exceptions;

public class DuplicateAccountException extends Exception{
	
	public DuplicateAccountException(int accNo)
	{
		super("Account No "+accNo +"  Already Exist.");
	}

}
