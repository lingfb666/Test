import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Test001 {

	private static Queue<Object> queue = new ConcurrentLinkedQueue<Object>();
	private static int capacity = 4;

	static T1 t1 = new T1();
	static T2 t2 = new T2();

	public static synchronized Boolean add(Object object) {
		return queue.offer(object);
	}

	public static synchronized Object get() {
		Object object = queue.poll();
		return object;
	}

	public static class T1 extends Thread {

		@Override
		public void run() {

			synchronized (queue) {
				int i = 1;
				while (true) {
					boolean isEmpty = false;
					if(i <= capacity){
						isEmpty = add("元素" + i);
					}
					if (isEmpty == false) {
						i = 1;
						try {
							System.out.println("元素已满" + Thread.currentThread().getName() + "线程进入等待!nodify get");
							queue.notify();
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							System.out.println("新增元素:" + i);
							i++;
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		}
	}

	public static class T2 extends Thread {

		@Override
		public void run() {

			synchronized (queue) {

				while (true) {
					Object result = get();
					if (null == result) {
						try {
							System.out.println("元素为空！" + Thread.currentThread().getName() + "线程进入等待!notify add");
							queue.notify();
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							System.out.println("获取元素:" + result.toString());
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		t2.start();
	}

}
