package bytebuddy.models;

public class FooDelegate {

	public static String saySomething(@net.bytebuddy.implementation.bind.annotation.This Foo foo) {
		System.out.println(foo.getClass().getName());
		return "foo";
	}
}
