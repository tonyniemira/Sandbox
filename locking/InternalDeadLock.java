/****************************************************************************************
 * 
 * A simple example to show how taking locks out of order will result in a deadlock.
 * 
 ****************************************************************************************
 */

package locking;

public class InternalDeadLock {
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	public void instanceMethod1() {
		synchronized (lock1) {
			System.out.println("instanceMethod1 - Lock 1 taken");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			synchronized (lock2) {
				System.out.println("instanceMethod1 - Lock 2 taken");
				// critical section guarded first by lock1 and then by lock2
				System.out.println("Inside the guts of instanceMethod1");
			}
		}
	}

	public void instanceMethod2() {
		synchronized (lock2) {
			System.out.println("instanceMethod2 - Lock 2 taken");
			synchronized (lock1) {
				System.out.println("instanceMethod2 - Lock 1 taken");
				// critical section guarded first by lock2 and then by lock1
				System.out.println("Inside the guts of instanceMethod2");
			}
		}
	}

	public void doTest() {
		new Thread(new Runnable() {
			public void run() {
				instanceMethod1();
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				instanceMethod2();
			}
		}).start();

	}

	public static void main(String args[]) {
		// Begin the example ....
		System.out.println("An example of deadlocking");
		InternalDeadLock idl = new InternalDeadLock();
		idl.doTest();
	}
}
