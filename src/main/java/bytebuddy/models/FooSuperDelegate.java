package bytebuddy.models;

import net.bytebuddy.implementation.bind.annotation.Super;

public class FooSuperDelegate {

	public static String saySomething(@Super Foo foo) {
		System.out.println(foo.getClass().getName());
		System.out.println(foo.saySomething());
		return "FooSuperDelegate";
	}
}
