
public class Cashier {

	private int timeToProcessLine = 0;
	private int cashierId = 1;
	
	public Cashier (int cId) {
		cashierId = cId;
	}
	
	public void processTicketBuyer(TicketBuyer b, int timeToProcess) {
		System.out.printf(
				"cashier %d processing buyer with arrival time %d secs, processing time %d secs, timeToProcessLine = %d\n",
				cashierId, b.getArrivalTime(), timeToProcess, timeToProcessLine);
		if (b.getArrivalTime() <= timeToProcessLine) {
			timeToProcessLine += timeToProcess;
		} else {
			timeToProcessLine += (b.getArrivalTime() - timeToProcessLine) + timeToProcess;
		}

		b.setDepartureTime(timeToProcessLine);
	}

	public boolean isAvailable(TicketBuyer b) {
		if (b.getArrivalTime() >= timeToProcessLine) {
			return true;
		}
		return false;
	}

	public int timeToProcessLine() {
		return timeToProcessLine;
	}

	public int cashierId() {
		return cashierId;
	}
}
