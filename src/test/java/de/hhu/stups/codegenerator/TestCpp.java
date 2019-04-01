package de.hhu.stups.codegenerator;


import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by fabian on 30.08.18.
 */

public class TestCpp {

	public static void writeInputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
		int size;
		byte[] buffer = new byte[1024];
		while ((size = inputStream.read(buffer)) != -1)
			outputStream.write(buffer, 0, size);
	}

	public static void writeInputToSystem(InputStream inputStream) throws IOException {
		writeInputToOutput(inputStream, System.err);
	}


	public void testCpp(String machine) throws Exception {
		Path mchPath = Paths.get(CodeGenerator.class.getClassLoader()
				.getResource("de/hhu/stups/codegenerator/" + machine + ".mch").toURI());
		CodeGenerator codeGenerator = new CodeGenerator();
		List<Path> cppFilePaths = codeGenerator.generate(mchPath, GeneratorMode.CPP, false, true, null);
		cppFilePaths.forEach(path -> {
			try {
				Process process = Runtime.getRuntime()
                        .exec("g++ -std=c++14 -O2 -march=native -g -DIMMER_NO_THREAD_SAFETY -c " + path.toFile().getAbsoluteFile().toString());
				writeInputToSystem(process.getErrorStream());
				writeInputToOutput(process.getErrorStream(), process.getOutputStream());
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});

		Set<File> oFiles = cppFilePaths.stream()
				.map(path -> new File(path.getParent().toFile(), machine + ".o"))
				.collect(Collectors.toSet());

		//javaFilePaths.forEach(path -> cleanUp(path.toString()));
		//classFiles.forEach(path -> cleanUp(path.getAbsolutePath().toString()));
		//cFilePaths.forEach(path -> cleanUp(path.toString()));
	}

	@Test
	public void testExample() throws Exception {
		testCpp("Example");
	}

	@Test
	public void testOperation() throws Exception {
		testCpp("Operation");
	}

	@Test
	public void testLocalDeclaration() throws Exception {
		testCpp("LocalDeclaration");
	}

	@Ignore
	@Test
	public void testRefinement() throws Exception {
		// TODO VAR-Node
		testCpp("RefinementMachine");
	}

	@Test
	public void testEnumSets() throws Exception {
		testCpp("EnumSets");
	}

	@Test
	public void testNameCollision() throws Exception {
		testCpp("NameCollision");
	}

	@Test
	public void testWhile() throws Exception {
		testCpp("While");
	}

	@Test
	public void testInterval() throws Exception {
		testCpp("Interval");
	}


	@Test
	public void testPair() throws Exception {
		testCpp("Pair");
	}

	@Test
	public void testIfAndPredicates() throws Exception {
		testCpp("IfAndPredicates");
	}

	@Test
	public void testImplies() throws Exception {
		testCpp("Implies");
	}

	@Test
	public void testEquivalence() throws Exception {
		testCpp("Equivalence");
	}

	@Test
	public void testBooleanPredicate() throws Exception {
		testCpp("BooleanPredicate");
	}

	@Ignore
	@Test
	public void testRecords() throws Exception {
		testCpp("Records");
	}

	@Ignore
	@Test
	public void testNondeterminism() throws Exception {
		testCpp("Nondeterminism");
	}

	@Test
	public void testMapFunction() throws Exception {
		testCpp("MapFunction");
	}

	@Test
	public void testRelationImage() throws Exception {
		testCpp("RelationImage");
	}

	@Test
	public void testEmptySet() throws Exception {
		testCpp("EmptySet");
	}

	@Test
	public void testSetUnion() throws Exception {
		testCpp("SetUnion");
	}


	@Test
	public void testCounter() throws Exception {
		testCpp("Counter");
	}

	@Test
	public void testBakery0() throws Exception {
		testCpp("Bakery0");
	}

	@Ignore
	@Test
	public void testGCD() throws Exception {
		// TODO
		testCpp("GCD");
	}

	@Test
	public void testACounter() throws Exception {
		testCpp("ACounter");
	}

	@Test
	public void testLift() throws Exception {
		testCpp("Lift");
	}

	@Test
	public void testTravelAgency() throws Exception {
		testCpp("TravelAgency");
	}

	@Ignore
	@Test
	public void testPhonebook() throws Exception {
		// TODO
		testCpp("phonebook");
	}

	@Ignore
	@Test
	public void testPhonebook6() throws Exception {
		// TODO
		testCpp("phonebook6");
	}

	@Ignore
	@Test
	public void testSum() throws Exception {
		testCpp("Sum");
	}

	@Ignore
	@Test
	public void testRecursion() throws Exception {
		//Correct exception
		testCpp("recursion/Sum1");
	}

	@Test
	public void testProject() throws Exception {
		testCpp("project1/A");
	}

	@Test
	public void testLiftBenchmarks() throws Exception {
		testCpp("liftbenchmarks/LiftExec");
	}


	@Test
	public void testSieveBenchmarks() throws Exception {
		testCpp("sievebenchmarks/Sieve");
	}

	@Test
	public void testTrafficLightBenchmarks() throws Exception {
		testCpp("trafficlightbenchmarks/TrafficLightExec");
	}

	@Test
	public void testIncreasingSet() throws Exception {
		testCpp("setoperationbenchmarks/IncreasingSet");
	}

	@Test
	public void testSetOperation() throws Exception {
		testCpp("setoperationbenchmarks/SetOperation");
	}


	@Test
	public void testProject2() throws Exception {
		testCpp("project2/MachineA");
	}

	@Test
	public void testSieve() throws Exception {
		testCpp("Sieve");
	}

	@Test
	public void testSieveParallel() throws Exception {
		testCpp("SieveParallel");
	}

	@Test
	public void testSwap() throws Exception {
		testCpp("Swap");
	}

	@Test
	public void testSwap2() throws Exception {
		testCpp("Swap2");
	}

	@Test
	public void testReset() throws Exception {
		testCpp("Reset");
	}

	@Test
	public void testManyLocalDeclarations() throws Exception {
		testCpp("ManyLocalDeclarations");
	}

	@Test
	public void testManyLocalDeclarations2() throws Exception {
		testCpp("ManyLocalDeclarations2");
	}

	@Test
	public void testPlus() throws Exception {
		testCpp("arithmetic/Plus");
	}

	@Test
	public void testMinus() throws Exception {
		testCpp("arithmetic/Minus");
	}

	@Test
	public void testMultiply() throws Exception {
		testCpp("arithmetic/Multiply");
	}

	@Test
	public void testDivide() throws Exception {
		testCpp("arithmetic/Divide");
	}


	@Test
	public void testModulo() throws Exception {
		testCpp("arithmetic/Modulo");
	}

	@Test
	public void testNegative() throws Exception {
		testCpp("arithmetic/Negative");
	}

	@Ignore
	@Test
	public void testPositive() throws Exception {
		testCpp("arithmetic/Positive");
	}

	@Test
	public void testSmallNumbers() throws Exception {
		testCpp("integers/SmallNumbers");
	}

	@Test
	public void testBigNumbers() throws Exception {
		testCpp("integers/BigNumbers");
	}

	@Test
	public void testAnd() throws Exception {
		testCpp("logical/And");
	}


	@Test
	public void testOr() throws Exception {
		testCpp("logical/Or");
	}


	@Test
	public void testImpliesPerformance() throws Exception {
		testCpp("logical/Implies");
	}


	@Ignore
	@Test
	public void testNot() throws Exception {
		testCpp("logical/Not");
	}

	@Test
	public void testEquivalent() throws Exception {
		testCpp("logical/Equivalent");
	}

	@Test
	public void testLess() throws Exception {
		testCpp("comparison/Less");
	}

	@Test
	public void testLessEqual() throws Exception {
		testCpp("comparison/LessEqual");
	}

	@Test
	public void testGreater() throws Exception {
		testCpp("comparison/Greater");
	}


	@Test
	public void testGreaterEqual() throws Exception {
		testCpp("comparison/GreaterEqual");
	}


	@Test
	public void tessEqual() throws Exception {
		testCpp("comparison/Equal");
	}


	@Test
	public void testUnequal() throws Exception {
		testCpp("comparison/Unequal");
	}

	@Test
	public void testCardBig() throws Exception {
		testCpp("setoperation_big/SetCardBig");
	}

	@Test
	public void testDifferenceBig() throws Exception {
		testCpp("setoperation_big/SetDifferenceBig");
	}

	@Test
	public void testElementOfBig() throws Exception {
		testCpp("setoperation_big/SetElementOfBig");
	}

	@Test
	public void testIntersectionBig() throws Exception {
		testCpp("setoperation_big/SetIntersectionBig");
	}

	@Test
	public void testUnionBig() throws Exception {
		testCpp("setoperation_big/SetUnionBig");
	}

	@Test
	public void testCardSmall() throws Exception {
		testCpp("setoperation_small/SetCardSmall");
	}

	@Test
	public void testDifferenceSmall() throws Exception {
		testCpp("setoperation_small/SetDifferenceSmall");
	}

	@Test
	public void testElementOfSmall() throws Exception {
		testCpp("setoperation_small/SetElementOfSmall");
	}

	@Test
	public void testIntersectionSmall() throws Exception {
		testCpp("setoperation_small/SetIntersectionSmall");
	}

	@Test
	public void testUnionSmall() throws Exception {
		testCpp("setoperation_small/SetUnionSmall");
	}

	@Test
	public void testRangeBig() throws Exception {
		testCpp("range_big/RangeBig");
	}


	@Test
	public void testRangeCardBig() throws Exception {
		testCpp("range_big/RangeCardBig");
	}

	@Test
	public void testRangeDifferenceBig() throws Exception {
		testCpp("range_big/RangeDifferenceBig");
	}

	@Test
	public void testRangeElementOfBig() throws Exception {
		testCpp("range_big/RangeElementOfBig");
	}

	@Test
	public void testRangeIntersectionBig() throws Exception {
		testCpp("range_big/RangeIntersectionBig");
	}

	@Test
	public void testRangeUnionBig() throws Exception {
		testCpp("range_big/RangeUnionBig");
	}

	@Test
	public void testRangeSmall() throws Exception {
		testCpp("range_small/RangeSmall");
	}


	@Test
	public void testRangeCardSmall() throws Exception {
		testCpp("range_small/RangeCardSmall");
	}

	@Test
	public void testRangeDifferenceSmall() throws Exception {
		testCpp("range_small/RangeDifferenceSmall");
	}

	@Test
	public void testRangeElementOfSmall() throws Exception {
		testCpp("range_small/RangeElementOfSmall");
	}

	@Test
	public void testRangeIntersectionSmall() throws Exception {
		testCpp("range_small/RangeIntersectionSmall");
	}

	@Test
	public void testRangeUnionSmall() throws Exception {
		testCpp("range_small/RangeUnionSmall");
	}

	@Test
	public void testCardBig2() throws Exception {
		testCpp("setoperation_big/SetCardBig2");
	}

	@Test
	public void testDifferenceBig2() throws Exception {
		testCpp("setoperation_big/SetDifferenceBig2");
	}

	@Test
	public void testElementOfBig2() throws Exception {
		testCpp("setoperation_big/SetElementOfBig2");
	}

	@Test
	public void testIntersectionBig2() throws Exception {
		testCpp("setoperation_big/SetIntersectionBig2");
	}

	@Test
	public void testUnionBig2() throws Exception {
		testCpp("setoperation_big/SetUnionBig2");
	}

	@Test
	public void testCardSmall2() throws Exception {
		testCpp("setoperation_small/SetCardSmall2");
	}

	@Test
	public void testDifferenceSmall2() throws Exception {
		testCpp("setoperation_small/SetDifferenceSmall2");
	}

	@Test
	public void testElementOfSmall2() throws Exception {
		testCpp("setoperation_small/SetElementOfSmall2");
	}

	@Test
	public void testIntersectionSmall2() throws Exception {
		testCpp("setoperation_small/SetIntersectionSmall2");
	}

	@Test
	public void testUnionSmall2() throws Exception {
		testCpp("setoperation_small/SetUnionSmall2");
	}

	@Test
	public void testRangeCardBig2() throws Exception {
		testCpp("range_big/RangeCardBig2");
	}

	@Test
	public void testRangeDifferenceBig2() throws Exception {
		testCpp("range_big/RangeDifferenceBig2");
	}

	@Test
	public void testRangeElementOfBig2() throws Exception {
		testCpp("range_big/RangeElementOfBig2");
	}

	@Test
	public void testRangeIntersectionBig2() throws Exception {
		testCpp("range_big/RangeIntersectionBig2");
	}

	@Test
	public void testRangeUnionBig2() throws Exception {
		testCpp("range_big/RangeUnionBig2");
	}

	@Test
	public void testRangeCardSmall2() throws Exception {
		testCpp("range_small/RangeCardSmall2");
	}

	@Test
	public void testRangeDifferenceSmall2() throws Exception {
		testCpp("range_small/RangeDifferenceSmall2");
	}

	@Test
	public void testRangeElementOfSmall2() throws Exception {
		testCpp("range_small/RangeElementOfSmall2");
	}

	@Test
	public void testRangeIntersectionSmall2() throws Exception {
		testCpp("range_small/RangeIntersectionSmall2");
	}

	@Test
	public void testRangeUnionSmall2() throws Exception {
		testCpp("range_small/RangeUnionSmall2");
	}


	@Test
	public void testTrafficLight() throws Exception {
		testCpp("TrafficLight");
	}

	@Test
	public void testChoice() throws Exception {
		testCpp("Choice");
	}

	@Test
	public void testAssert() throws Exception {
		testCpp("Assert");
	}

	@Test
	public void testCruiseController1() throws Exception {
		testCpp("Cruise_finite1");
	}

	@Test
	public void testCruiseControllerk() throws Exception {
		testCpp("Cruise_finite_k");
	}

	@Test
	public void testCruiseControllerDeterministic() throws Exception {
		testCpp("Cruise_finite_Deterministic");
	}

	@Test
	public void testScheduler() throws Exception {
		testCpp("scheduler");
	}

	@Test
	public void testCanBusTLC() throws Exception {
		testCpp("CAN_BUS_tlc");
	}

	@Ignore
	@Test
	public void testCore() throws Exception {
		testCpp("Core");
	}

	@Ignore
	@Test
	public void testSetLawsNAT() throws Exception {
		testCpp("SetLawsNAT");
	}

	@Test
	public void SimpsonFourSlot() throws Exception {
		testCpp("Simpson_Four_Slot");
	}

	@Test
	public void Train1BeebookTLC() throws Exception {
		testCpp("Train_1_beebook_TLC");
	}

	@Test
	public void Train1() throws Exception {
		testCpp("train_1");
	}

	@Ignore
	@Test
	public void testEarley2() throws Exception {
		testCpp("earley_2");
	}

	@Ignore
	@Test
	public void testOBSW_M001() throws Exception {
		testCpp("obsw_M001");
	}

	@Ignore
	@Test
	public void testRef5Switch() throws Exception {
		testCpp("Ref5_Switch");
	}

	@Ignore
	@Test
	public void testrether_mch1() throws Exception {
		testCpp("rether_mch1");
	}


	@Test
	public void testSetComprehension1() throws Exception {
		testCpp("SetComprehension1");
	}

	@Test
	public void testSetComprehension2() throws Exception {
		testCpp("SetComprehension2");
	}

	@Test
	public void testSetComprehension3() throws Exception {
		testCpp("SetComprehension3");
	}

	@Test
	public void testSetComprehension4() throws Exception {
		testCpp("SetComprehension4");
	}

	@Ignore
	@Test
	public void testSetComprehension5() throws Exception {
		//Does not work because of bounded variables from outer scope
		testCpp("SetComprehension5");
	}

	@Test
	public void testLambda() throws Exception {
		testCpp("Lambda");
	}

	@Test
	public void testLambda2() throws Exception {
		testCpp("Lambda2");
	}

	@Test
	public void testQuantifiedPredicate() throws Exception {
		testCpp("QuantifiedPredicate");
	}

	@Test
	public void testQuantifiedPredicate2() throws Exception {
		testCpp("QuantifiedPredicate2");
	}

	@Test
	public void testQuantifiedPredicate3() throws Exception {
		testCpp("QuantifiedPredicate3");
	}

	@Test
	public void testQuantifiedExpression1() throws Exception {
		testCpp("QuantifiedExpression1");
	}

	@Test
	public void testQuantifiedExpression2() throws Exception {
		testCpp("QuantifiedExpression2");
	}

	@Test
	public void testQuantifiedExpression3() throws Exception {
		testCpp("QuantifiedExpression3");
	}

	@Test
	public void testQuantifiedExpression4() throws Exception {
		testCpp("QuantifiedExpression4");
	}

	@Test
	public void testQuantifiedExpression5() throws Exception {
		testCpp("QuantifiedExpression5");
	}


	@Test
	public void testGeneralizedUnion() throws Exception {
		testCpp("GeneralizedUnion");
	}

	@Test
	public void testGeneralizedIntersection() throws Exception {
		testCpp("GeneralizedIntersection");
	}


	@Test
	public void testFunctionalOverride() throws Exception {
		testCpp("FunctionalOverride");
	}

	@Test
	public void testFunctionalOverride2() throws Exception {
		testCpp("FunctionalOverride2");
	}

	@Test
	public void testFunctionalOverride3() throws Exception {
		testCpp("FunctionalOverride3");
	}

	@Test
	public void testOverride() throws Exception {
		testCpp("Override");
	}

	@Test
	public void testPow() throws Exception {
		testCpp("Pow");
	}

	@Test
	public void testPow1() throws Exception {
		testCpp("Pow1");
	}

	@Test
	public void testFin() throws Exception {
		testCpp("Fin");
	}

	@Test
	public void testFin1() throws Exception {
		testCpp("Fin1");
	}

	@Test
	public void testId() throws Exception {
		testCpp("Id");
	}

	@Test
	public void testParallelProduct() throws Exception {
		testCpp("ParallelProduct");
	}

	@Test
	public void testDirectProduct() throws Exception {
		testCpp("DirectProduct");
	}

	@Test
	public void testComposition() throws Exception {
		testCpp("Composition");
	}

	@Test
	public void testProjection1() throws Exception {
		testCpp("Projection1");
	}

	@Test
	public void testProjection2() throws Exception {
		testCpp("Projection2");
	}

	@Test
	public void testAppend() throws Exception {
		testCpp("Append");
	}


	@Ignore
	@Test
	public void testConc() throws Exception {
		testCpp("Conc");
	}

	@Test
	public void testConcat() throws Exception {
		testCpp("Concat");
	}

	@Test
	public void testDrop() throws Exception {
		testCpp("Drop");
	}

	@Test
	public void testEmptySequence() throws Exception {
		testCpp("EmptySequence");
	}

	@Test
	public void testEnumeratedSequence() throws Exception {
		testCpp("EnumeratedSequence");
	}

	@Test
	public void testFirstElementSequence() throws Exception {
		testCpp("FirstElementSequence");
	}

	@Test
	public void testFrontSequence() throws Exception {
		testCpp("FrontSequence");
	}

	@Test
	public void testLastElementSequence() throws Exception {
		testCpp("LastElementSequence");
	}

	@Test
	public void testPrepend() throws Exception {
		testCpp("Prepend");
	}

	@Test
	public void testReverse() throws Exception {
		testCpp("ReverseSequence");
	}

	@Test
	public void testSizeOfSequence() throws Exception {
		testCpp("SizeOfSequence");
	}

	@Test
	public void testTailSequence() throws Exception {
		testCpp("TailSequence");
	}

	@Test
	public void testTake() throws Exception {
		testCpp("Take");
	}

	@Test
	public void testSequenceOperateRelation() throws Exception {
		testCpp("SequenceOperateRelation");
	}

	@Test
	public void testPred() throws Exception {
		testCpp("Pred");
	}

	@Test
	public void testSucc() throws Exception {
		testCpp("Succ");
	}

	@Test
	public void testIterate() throws Exception {
		testCpp("Iterate");
	}

	@Test
	public void testClosure() throws Exception {
		testCpp("Closure");
	}

	@Test
	public void testClosure1() throws Exception {
		testCpp("Closure1");
	}

	private void cleanUp(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

}
