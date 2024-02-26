//********************************************************************
//  Staff.java      
//
//  Represents the personnel staff of a particular business.
//********************************************************************
import java.util.ArrayList;
import java.util.Scanner;

public class Staff
{
   private StaffMember[] staffList;
   Scanner scanf= new Scanner(System.in);
   ArrayList<StaffMember> eList= new ArrayList<>();
   ArrayList<StaffMember> RList= new ArrayList<>() ;
   ArrayList<StaffMember> ModList= new ArrayList<>() ;

   //-----------------------------------------------------------------
   //  Constructor: Sets up the list of staff members.
   //-----------------------------------------------------------------
   public Staff ()
   {
	   
      staffList = new StaffMember[6];
   
      staffList[0] = new Executive ("Sam", "123 Main Line",
         "555-0469", "123-45-6789", 2423.07);

      staffList[1] = new Employee ("Carla", "456 Off Line",
         "555-0101", "987-65-4321", 1246.15);
      staffList[2] = new Employee ("Woody", "789 Off Rocker",
         "555-0000", "010-20-3040", 1169.23);

      staffList[3] = new Hourly ("Diane", "678 Fifth Ave.",
         "555-0690", "958-47-3625", 10.55);

      staffList[4] = new Hourly ("Norm", "987 Suds Blvd.",
         "555-8374","456-78-9000", 11.2);
         
      staffList[5] = new Volunteer ("Cliff", "321 Duds Lane",
         "555-7282");
         
      ((Executive)staffList[0]).awardBonus (500.00);

      ((Hourly)staffList[3]).addHours (40);
      ((Hourly)staffList[4]).addHours (30);
      
      for(int i=0;  i < staffList.length; i++)
      {
    	     eList.add(staffList[i]) ;
    	     ModList.add(staffList[i]);
      }
    
  
 
      
   }

   //-----------------------------------------------------------------
   //  Pays all staff members.
   //-----------------------------------------------------------------
   public void payday ()
   {
      double amount;

      for (int count=0; count < staffList.length; count++)
      {
         
    	  
    	  System.out.println (staffList[count]);

         amount = staffList[count].pay();  // polymorphic

         if (amount == 0.0)
            System.out.println ("Thanks!");
         else
            System.out.println ("Paid: " + amount);

         System.out.println ("-----------------------------------");
      }
   }
   
   public StaffMember getStaff(int i)
   {
	 StaffMember st =  staffList[i];
	 return st;
   }
   
   public int getSize()
   {
	  int sz = staffList.length;
	  return sz;
   
   }

  
   
   public void delete(String staff)
   {
	   
	   
	   for(int x=0; x < ModList.size(); x++) 
	   {
		  
		   String staffData = ModList.get(x).name;
		   System.out.println("X= " + x);
		   
		   if(staffData.equals(staff)) // if statement
		   {
			   System.out.println(staffData +" is removed from the Employee Records:");
			   ModList.remove(x);
			   System.out.println("X= "+ x);
			   x--;
			   System.out.println("Next employee in index: "+ ModList.get(x));

		   }
		  
			  
				   
       }
		   
		   
		   
	   }
	   
	   
   
   public void Modify(String ename)
   {
	   for(int x=0; x < ModList.size(); x++) 
	   {
		  
		   String staffData = ModList.get(x).name;
		   System.out.println("X= " + x);
		  
		   if(staffData.equals(ename)) // if statement
		   {
			   System.out.println( " Out of the choices below what would you like to modify about " +staffData+"'s Employee Record:");
			   
			   
			   System.out.println("Enter the Employee's new name: ");
			   String nname= scanf.nextLine();
			   
			     
			   System.out.println("Enter Employee's new address: ");
			   String nAddress= scanf.nextLine();
			 
			   
			   System.out.println("Enter Employee's new phone number: ");
			   String nPhone= scanf.nextLine();
			   
			   
			   System.out.println("Enter Employee's new Soical");
			   String Social= scanf.nextLine();
			  
			   
			   System.out.println("Enter Employee's new pay ");
			   Double nPay= scanf.nextDouble();
			   
			   ModList.get(x).name = nname; 
			   ModList.get(x).address = nAddress;
			   ModList.get(x).phone= nPhone;
			   
			  
			   Employee w= new Employee(nname, nAddress, nPhone, Social, nPay);
			   System.out.println(staffData + "'s New record reads: ");
			   w.socialSecurityNumber= Social;
			   w.payRate = nPay;
			   System.out.println(w);
			   System.out.println( w.toString());
			   System.out.println(w.pay());
		   } 
					
			   
			    }
			  

		   }
		  
			  
				   
       
	   
	   
   }
	   
	   
	   
	   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
	   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
	   
   
   
   

  
 

