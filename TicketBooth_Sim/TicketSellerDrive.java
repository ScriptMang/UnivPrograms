import java.util.Queue;
import java.util.LinkedList;

public class TicketSellerDrive {

	public static void main(String[] args) {
		int ticketBuyers = 10;
		Queue<TicketBuyer> q = genTicketBuyersLine(ticketBuyers);

		for (int i = 1; i < 11; i++) {
			System.out.printf("\n\nrunning simulation for %d ticket buyers, %d cashiers\n", ticketBuyers, i);
			int simTime = ticketCounterSimulation(i, copyQueue(q));
			System.out.printf("time to process line = %d secs, average time to process ticket buyer = %d secs", simTime,
					simTime / ticketBuyers);
			}
	}
	
	public static <E> Queue<E> copyQueue(Queue<E> q) {
		return new LinkedList<E>(q);
	}
	
	public static Queue<TicketBuyer> genTicketBuyersLine(int buyersCount) {
		if (buyersCount < 1) return null;
		
		// Initiate a time pool to generate customers with random arrival times
		// that satisfy the 15 second average arrival time for all customers
		int timePool = buyersCount * 15;
		Queue<TicketBuyer> ticketBuyers = new LinkedList<TicketBuyer>();
		int timeCounter = 0;
		for (int i = 0; i < buyersCount; i++) {
			// an arrival time of zero is possible for customers
			int randArrivalTime = (int) (Math.random() * timePool * i / buyersCount);
			timePool -= randArrivalTime;
			timeCounter += randArrivalTime;
			ticketBuyers.add(new TicketBuyer(timeCounter));

		}
		// for (int i = 0; i < ticketBuyers.size(); i++) {
		// System.out.println(ticketBuyers.poll().getArrivalTime());
		// }
		return ticketBuyers;
	}

	public static int earliestProcessingCashier(Cashier[] c) {
		int timeToProcess = -1;
		int index = Integer.MIN_VALUE;
		for (int i = 0; i < c.length; i++) {
			if (i == 0) {
				index = 0;
				timeToProcess = c[0].timeToProcessLine();
				continue;
			}
			if (c[i].timeToProcessLine() < timeToProcess) {
				index = i;
				timeToProcess = c[i].timeToProcessLine();
			}
		}
		return index;
	}
	
	public static int ticketCounterSimulation(int cashiersCount, Queue<TicketBuyer> buyersLine) {
		Cashier[] cashiers = new Cashier[cashiersCount];
		int[] processTimes = genCashierProcessingTimes(buyersLine.size());

		for (int i = 1; i < cashiersCount + 1; i++) {
			Cashier c = new Cashier(i);
			cashiers[i-1] = c;
		}

		while (!buyersLine.isEmpty()) {
			int processTimesIndex = (int) (Math.random() * processTimes.length);

			int ecIndex = earliestProcessingCashier(cashiers);
			if (ecIndex == Integer.MIN_VALUE) return Integer.MIN_VALUE;
			cashiers[ecIndex].processTicketBuyer(buyersLine.poll(), processTimes[processTimesIndex]);
		}
		int timeToProcessLine = -1;
		for (int i = 0; i < cashiers.length; i++) {
			if (i == 0) {
				timeToProcessLine = cashiers[0].timeToProcessLine();
				continue;
			}

			if (cashiers[i].timeToProcessLine() > timeToProcessLine) {
				timeToProcessLine = cashiers[i].timeToProcessLine();
			}
		}

		return timeToProcessLine;
	}

	public static int[] genCashierProcessingTimes(int buyers) {
		if (buyers < 1) return null;
		
		int[] processingTimes = new int[buyers];
		for (int i = 0; i < buyers; i++) {
			int processingtime;
			if (i <= .75 * buyers) {
				processingtime = (int) (Math.random() * 121 + 60);
			} else if (i <= .85 * buyers) {
				processingtime = (int) (Math.random() * 46 + 15);
			} else {
				processingtime = (int) (Math.random() * 401 + 200);
			}
			processingTimes[i] = processingtime;
		}
		return processingTimes;
	}
}
