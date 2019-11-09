package cglib.learn;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;

public class Starter {
	public static void main(String[] args) {
		fixedValue();
	}

	private static void fixedValue() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(SampleClass.class);
		enhancer.setCallback(new FixedValue() {

			public Object loadObject() throws Exception {
				return "Hello cglib!";
			}
		});
		SampleClass proxy = (SampleClass) enhancer.create();

		enhancer.setCallback(new FixedValue() {

			public Object loadObject() throws Exception {
				return "Hello cglib2222!";
			}
		});
		System.out.println(proxy.test("hi hi"));
		
		SampleClass proxy2 = (SampleClass) enhancer.create();
		System.out.println(proxy2.test("hi hi"));
	}

}
