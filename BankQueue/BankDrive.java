public class BankDrive {

	public static void main(String[] args) {
		simulation();
	}

	public static void simulation() {
		Line b = new Line();
		int customerSeqId = 0;

		for (int i = 0; i < 10; i++) {
			System.out.printf("\nrunning simulation %d\n", i + 1);
			int newCustomersCount = (int) (Math.random() * 5) + 1;
			int tellersCount = (int) (Math.random() * 4) + 1;

			int workingTellers;
			int customersToLineCount = newCustomersCount;
			int totalCustomers = newCustomersCount + b.queue.size();
			if (tellersCount > totalCustomers) {
				workingTellers = totalCustomers;
			} else {
				workingTellers = tellersCount;
			}

			for (int j = 0; j < workingTellers; j++) {
				Customer c = null;
				if (!b.isEmpty()) {
					c = b.nextCustomer();
				} else {
					customersToLineCount--;
					c = new Customer(++customerSeqId);
				}
				System.out.println(c.toString() + " is being served");
			}

			if (customersToLineCount < 0) customersToLineCount = 0;
			if (customersToLineCount > 0) {
				for (int j = 0; j < customersToLineCount; j++) {
					Customer c = new Customer(++customerSeqId);
					b.addCustomer(c);
					System.out.println(c.toString() + " joins the line");
				}
			}
			
			if ((tellersCount - workingTellers) > 0) {
				System.out.println("Teller waiting");
			}
			
			if (i == 9) {
				int lineSize = b.queue.size();
				for (int j = 0; j < lineSize; j++) {
					System.out.println(b.nextCustomer().toString() + " is being served");
				}
			}
		}
	}
}
