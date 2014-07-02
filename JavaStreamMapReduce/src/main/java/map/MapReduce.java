package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MapReduce {

	public static void main(String[] args) {

		new MapReduce().run();
	}

	public void run() {

		int loopMax = 1000 * 1000 * 1;

		for (int retry = 0; retry < 4; retry++) {

			System.out.println("Try: " + retry);

			{
				Date date3 = new Date();
				for (int i = 0; i < loopMax; i++) {
					List<String> result = testLoopStyle();
				}
				Date date4 = new Date();
				System.out.println("testLoopStyle result: " + (date4.getTime() - date3.getTime()));
			}

			{
				Date date5 = new Date();
				for (int i = 0; i < loopMax; i++) {
					List<String> result = testMapReduce();
				}
				Date date6 = new Date();
				System.out.println("testMapReduce result: " + (date6.getTime() - date5.getTime()));
			}

			{
				Date date7 = new Date();
				for (int i = 0; i < loopMax; i++) {
					List<String> result = testMapReduceLambda();
				}
				Date date8 = new Date();
				System.out.println("testMapReduceLambda result: " + (date8.getTime() - date7.getTime()));
			}

			{
				Date date9 = new Date();
				for (int i = 0; i < loopMax; i++) {
					List<String> result = testMapReduceLambdaAsMethod();
				}
				Date date10 = new Date();
				System.out.println("testMapReduceLambdaAsMethod result: " + (date10.getTime() - date9.getTime()));
			}
			
		}

	}

	private static List<String> newList() {

		return new LinkedList<String>();
	}

	private final String[] storageIDs = new String[] { "1", "2", "3", "4", "5" };

	private List<String> testLoopStyle() {

		List<String> result = new LinkedList<String>();

		for (String storageId : storageIDs) {

			List<String> linkedList = new LinkedList<String>();

			linkedList.add(storageId + "_1");
			linkedList.add(storageId + "_2");
			linkedList.add(storageId + "_3");
			linkedList.add(storageId + "_4");
			linkedList.add(storageId + "_5");

			result.addAll(linkedList);
		}

		return result;
	}

	private List<String> testMapReduce() {

		Function<String, List<String>> mapFunction = new Function<String, List<String>>() {

			public List<String> apply(String storageId) {

				List<String> linkedList = new LinkedList<>();

				linkedList.add(storageId + "_1");
				linkedList.add(storageId + "_2");
				linkedList.add(storageId + "_3");
				linkedList.add(storageId + "_4");
				linkedList.add(storageId + "_5");

				return linkedList;
			}
		};

		BinaryOperator<List<String>> reduceFunction = new BinaryOperator<List<String>>() {

			@Override
			public List<String> apply(List<String> t, List<String> u) {

				t.addAll(u);
				return t;
			}
		};

		return (List<String>) Arrays.stream(storageIDs).map(mapFunction).reduce(new LinkedList<>(), reduceFunction);

	}

	// --------------------------------------------------------------------------------------------------------------------------

	private List<String> testMapReduceLambda() {

		return (List<String>) Arrays.stream(storageIDs)

		.map(storageId -> {

			List<String> linkedList = new LinkedList<>();

			linkedList.add(storageId + "_1");
			linkedList.add(storageId + "_2");
			linkedList.add(storageId + "_3");
			linkedList.add(storageId + "_4");
			linkedList.add(storageId + "_5");

			return linkedList;
		})

		.reduce(new LinkedList<>(), (List<String> t, List<String> u) -> {
			t.addAll(u);
			return t;
		});

	}

	// --------------------------------------------------------------------------------------------------------------------------

	private static List<String> mapMethod(String storageId) {

		List<String> linkedList = new LinkedList<>();

		linkedList.add(storageId + "_1");
		linkedList.add(storageId + "_2");
		linkedList.add(storageId + "_3");
		linkedList.add(storageId + "_4");
		linkedList.add(storageId + "_5");

		return linkedList;
	}

	private static List<String> reduceMethod(List<String> t, List<String> u) {

		t.addAll(u);
		return t;
	}

	private List<String> testMapReduceLambdaAsMethod() {

		return (List<String>) Arrays.stream(storageIDs).map(MapReduce::mapMethod).reduce(new LinkedList<>(), MapReduce::reduceMethod);
	}
}
