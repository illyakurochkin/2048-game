package pack;

public class Game {
	
	static {
		System.out.println("test");
	}
	public static void main(String[] args) {
		
		new Thread(new Runnable(){
			public void run(){
				new Window();
			}
		}).start();
	}
}
