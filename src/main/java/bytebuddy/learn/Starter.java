package bytebuddy.learn;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import bytebuddy.models.Foo;
import bytebuddy.models.FooDelegate;
import bytebuddy.models.FooSuperDelegate;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.ClassFileLocator.ForJarFile;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.pool.TypePool;

public class Starter {

	public static void main(String[] args) throws Exception {
		learnSubclassBasicStaticSuperMethodDelegation();
	}

	/**
	 * To learn how subclassing works.
	 * 
	 * @throws Exception
	 */
	public static void learnSubclassBasic() throws Exception {
		Foo foo = new Foo();
		System.out.println(foo.saySomething());

		Foo hybridFoo = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar"))//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

		System.out.println("\n\n\n**** METHOD CALLING ****");
		System.out.println(hybridFoo.saySomething());
		System.out.println(foo.saySomething());

		System.out.println("\n\n\n**** CLASS NAME ****");
		System.out.println(foo.getClass().getSimpleName());
		System.out.println(hybridFoo.getClass().getSimpleName()); // random name

		System.out.println("\n\n\n**** CLASS LOADER ****");
		ClassLoader fooLoader = foo.getClass().getClassLoader();
		System.out.println(fooLoader);
		ClassLoader hybridLoader = hybridFoo.getClass().getClassLoader();
		System.out.println(hybridLoader); // a new wrapped class loader
		System.out.println(hybridLoader.getParent()); // wrapper

		System.out.println("\n\n\n**** CLASS LOADED BY NEW LOADER DELEGATES TO WRAPPED ****");
		Class<?> fooLoadedByHybrid = hybridLoader.loadClass("bytebuddy.models.Foo");
		System.out.println(fooLoadedByHybrid == foo.getClass());
	}

	public static void learnSubclassBasicStaticMethodDelegation() throws Exception {

		Foo hybridFoo = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(MethodDelegation.to(FooDelegate.class))//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

		hybridFoo.saySomething();
	}
	
	/**
	 * See usage of @Super annotation.
	 * <p>
	 * A new auxilary type is created
	 * @throws Exception
	 */
	public static void learnSubclassBasicStaticSuperMethodDelegation() throws Exception {

		Foo hybridFoo = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(MethodDelegation.to(FooSuperDelegate.class))//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

		hybridFoo.saySomething();
	}

	/**
	 * To learn how redefinition works.
	 * 
	 * 
	 * <p>
	 * 
	 * First foo will print and then bar, object is same.
	 * 
	 * <p>
	 * CRAZY!!
	 * 
	 * @throws Exception
	 */
	public static void learnRedefintionBasic() throws Exception {
		Foo foo = new Foo();
		System.out.println(foo.saySomething());

		ByteBuddyAgent.install();
		new ByteBuddy()//
				.redefine(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar"))//
				.make()//
				.load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

		System.out.println(foo.saySomething());

	}

	/**
	 * Creatig two sub classes and hence two loaders.
	 * <p>
	 * Ask one to load other will fail.
	 * <p>
	 * Try using injection
	 * 
	 * @throws Exception
	 */

	public static void subclassingTwice() throws Exception {
		Foo foo = new Foo();
		System.out.println(foo.saySomething());

		Foo hybridFoo1 = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar1"))//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

		Foo hybridFoo2 = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar2"))//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

		System.out.println("\n\n\n**** METHOD CALLING ****");
		System.out.println(hybridFoo2.saySomething());
		System.out.println(hybridFoo1.saySomething());
		System.out.println(foo.saySomething());

		System.out.println("\n\n\n**** CLASS NAME ****");
		System.out.println(foo.getClass().getSimpleName());
		System.out.println(hybridFoo1.getClass().getSimpleName());
		System.out.println(hybridFoo2.getClass().getSimpleName());// random name

		// two different class loaders
		System.out.println("\n\n\n**** CLASS LOADER ****");
		ClassLoader fooLoader = foo.getClass().getClassLoader();
		System.out.println(fooLoader);
		ClassLoader hybridLoader1 = hybridFoo1.getClass().getClassLoader();
		System.out.println(hybridLoader1); // a new wrapped class loader
		System.out.println(hybridLoader1.getParent()); // wrapper
		ClassLoader hybridLoader2 = hybridFoo2.getClass().getClassLoader();
		System.out.println(hybridLoader2); // a new wrapped class loader
		System.out.println(hybridLoader2.getParent()); // wrapper

		Class<?> hybrid2LoadedByLoader1 = hybridLoader1.loadClass(hybridFoo2.getClass().getCanonicalName());
		System.out.println(hybrid2LoadedByLoader1);

	}

	/**
	 * This will pass because we are using injection. No new loader, injecting in
	 * old
	 * 
	 * @throws Exception
	 */
	public static void subclassingTwiceWithInjection() throws Exception {
		Foo foo = new Foo();
		System.out.println(foo.saySomething());

		Foo hybridFoo1 = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar1"))//
				.make()//
				.load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)//
				.getLoaded()//
				.newInstance();

		Foo hybridFoo2 = new ByteBuddy()//
				.subclass(Foo.class)//
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("bar2"))//
				.make()//
				.load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)//
				.getLoaded()//
				.newInstance();

		System.out.println("\n\n\n**** METHOD CALLING ****");
		System.out.println(hybridFoo2.saySomething());
		System.out.println(hybridFoo1.saySomething());
		System.out.println(foo.saySomething());

		System.out.println("\n\n\n**** CLASS NAME ****");
		System.out.println(foo.getClass().getSimpleName());
		System.out.println(hybridFoo1.getClass().getSimpleName());
		System.out.println(hybridFoo2.getClass().getSimpleName());// random name

		// two different class loaders
		System.out.println("\n\n\n**** CLASS LOADER ****");
		ClassLoader fooLoader = foo.getClass().getClassLoader();
		System.out.println(fooLoader);
		ClassLoader hybridLoader1 = hybridFoo1.getClass().getClassLoader();
		System.out.println(hybridLoader1); // a new wrapped class loader
		System.out.println(hybridLoader1.getParent()); // wrapper
		ClassLoader hybridLoader2 = hybridFoo2.getClass().getClassLoader();
		System.out.println(hybridLoader2); // a new wrapped class loader
		System.out.println(hybridLoader2.getParent()); // wrapper

		Class<?> hybrid2LoadedByLoader1 = hybridLoader1.loadClass(hybridFoo2.getClass().getCanonicalName());
		System.out.println(hybrid2LoadedByLoader1);

	}

	/**
	 * To learn how redefinition works before actually loading the class
	 * <p>
	 * BROKEN right now, need to fix it.
	 * 
	 * @throws Exception
	 */
	public static void learnRedefineBasic() throws Exception {

		TypePool typePool = TypePool.Default.of(ForJarFile.ofClassPath());
		new ByteBuddy()//
				.redefine(typePool.describe("bytebuddy.models.Bar").resolve(), ForJarFile.ofClassPath())//
				.defineField("qux", String.class).make()//
				.load(Starter.class.getClassLoader())//
				.getLoaded()//
				.newInstance();

	}

	public static void implementInterfaceImproperly() throws Exception {

		Class<?> klazz = new ByteBuddy()//
				.subclass(Object.class)//
				.implement(Eatable.class)//
				.make()//
				.load(Foo.class.getClassLoader())//
				.getLoaded();

		System.out.println(Modifier.isAbstract(klazz.getModifiers()));
		System.out.println(klazz.getCanonicalName());
		System.out.println(Arrays.asList(klazz.getInterfaces()));

	}
}
