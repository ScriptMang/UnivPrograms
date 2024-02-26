//*******************************************************************
//  Line.java       
// 
//*******************************************************************

import java.util.LinkedList;
public class Line
{
   LinkedList queue;

   //----------------------------------------------------------------
   //  Creates a new line based on a queue.
   //----------------------------------------------------------------
   public Line()
   {
      queue = new LinkedList();
   }

   //----------------------------------------------------------------
   //  Removes a customer from the head of the queue.
   //----------------------------------------------------------------
   public Customer nextCustomer()
   {
      return (Customer)queue.removeFirst();
   }

   //----------------------------------------------------------------
   //  Adds a customer to the tail of the queue.
   //----------------------------------------------------------------
   public void addCustomer(Customer person)
   {
      queue.addLast(person);
   }

   //----------------------------------------------------------------
   //  Indicates whether the queue is empty.
   //----------------------------------------------------------------
   public boolean isEmpty()
   {
      return queue.isEmpty();
   }

   //----------------------------------------------------------------
   //  Returns the number of customers in the queue.
   //----------------------------------------------------------------
   public int size()
   {
      return queue.size();
   }
}
