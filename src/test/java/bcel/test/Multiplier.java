package bcel.test;

public class Multiplier {

	private int multiplyer;

	public Multiplier(int multiplyer) {
		this.multiplyer = multiplyer;
	}

	public int doIt(int num) {
		return num * multiplyer;
	}

	public void displ() {
		System.out.println("Hi " + multiplyer + " number");
	}
}
