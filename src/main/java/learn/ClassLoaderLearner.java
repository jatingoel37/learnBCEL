package learn;

public class ClassLoaderLearner {

	public static void main(String[] args) {
		ClassLoader loader = ClassLoaderLearner.class.getClassLoader();
		printMeAndCallForParent(loader);

	}

	public static void printMeAndCallForParent(ClassLoader classLoader) {
		if (classLoader == null) {
			System.out.println("bootstrap");
			return;
		}

		System.out.println(classLoader);
		printMeAndCallForParent(classLoader.getParent());

	}
}
