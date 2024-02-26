import java.util.Scanner;

public class TestDriveEmployeeR {

	
	public static void main(String[] args) 
	{
		
		Staff p = new Staff();
		Scanner scanf= new Scanner (System.in);
		System.out.println("Enter a Employee name: ");
		String name=scanf.nextLine();
		
		
		
	    for( StaffMember cell: p.eList)
	    {
	    	if(name.equalsIgnoreCase(cell.name) )
	    	{
	    		
	    		if (cell  instanceof Executive)
	    			System.out.println("Executive");
	    		else
	    			if (cell  instanceof Hourly)
	    				System.out.println("Hourly");                //Enter the name of a Staff member//
                                                                     // to see their Employee record which includes//
                                                                    // each of their individual pay rates//
	    			else
	    				if (cell  instanceof Volunteer)
	    					System.out.println("Volunteer");
	    				else
	    					if (cell  instanceof Employee)
	    						System.out.println("Employee");
	    
	    		System.out.println(cell);
	    		System.out.println("PayRate: " + cell.pay());
	    		
	    		
	    		// When user inputs the word Remove 
                // or Modify it changes the ArrayList
	    	    System.out.println("Do you wish to Remove or Modify " +name+"'s  Record:");    
	    	    String rf=scanf.nextLine();
	    	    System.out.printf("You Chose --> '%s'\n", rf);
	    		
	    	   if(rf.equalsIgnoreCase("Remove"))
	    	   {
	    			p.delete(name);
	    			scanf.close();
	           }
	    	
	    	   
	    	   if(rf.equalsIgnoreCase("Modify"))
	    	   {
	    		   p.Modify(name);
	    		   scanf.close();
	    	   }
	    		
	    		
	    	}
	    
	     
}
	    
	    for( StaffMember cell: p.ModList)
	    {
	    	System.out.println(cell);
	    	System.out.println("---------------------------------");
	    }
	
		    	   
     }
		       
	
}
		  


