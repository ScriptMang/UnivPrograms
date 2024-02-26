//*******************************************************************
//  Customer.java        
//*******************************************************************

public class Customer
{
   private int id;

   //----------------------------------------------------------------
   //  Creates a customer with the specified id number.
   //----------------------------------------------------------------
   public Customer (int number)
   {
      id = number;
   }

   //----------------------------------------------------------------
   //  Returns a string description of this customer.
   //----------------------------------------------------------------
   public String toString()
   {
      return "Customer " + id;
   }
}
