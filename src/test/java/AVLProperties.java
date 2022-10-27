import AVLStarter.AVL;
// import AVLCorrected.AVL;   // enable for testing your bug fixes
import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple1;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.Assertions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/********************************************************************************************
 * CISC/CMPE 422/835
 * Properties for property-based testing of AVL-tree implementation
 * Starter properties files
 ********************************************************************************************/
class AVLProperties {

	/***************************************************************************************
	 * SECTION: HELPER METHODS
	 * PURPOSE: to facilitate implementation of examples and properties
	 * NOTES:
	 * - Implementation of additional helper methods is optional
	 * - If you do implement more helper methods, their definition should go at the end this section
	 ***************************************************************************************/

	// compute height of tree (empty tree has height 0)
	static public int height (AVL<Integer,Integer> avl) {
		if (avl.isEmpty())
			return 0;
		else
			return (1 + Math.max(height(avl.left), height(avl.right)));
	}

	// the height of subtrees differs by at most 1
	static public boolean isBalanced (AVL<Integer,Integer> avl) {
		if (avl.isEmpty()) return true;
		else {
			int bf = Math.abs(height(avl.left) - height(avl.right));
			return (bf <= 1 && isBalanced(avl.left) && isBalanced(avl.right));
		}
	}

	// in-order traversal of the tree results in a list of keys that is sorted in strict (no duplicates) ascending order
	static public boolean isSearchTree (AVL<Integer,Integer> avl) {
		List<Integer> keyL = keysInOrder(avl);
		return isSortedInStrictAscendingOrder(keyL);
	}

	// traverse tree in-order and collect keys in that order into a list
	static public ArrayList<Integer> keysInOrder (AVL<Integer,Integer> avl) {
		ArrayList<Integer> res = new ArrayList<>();
		if (!avl.isEmpty()) {
			res = keysInOrder(avl.left);
			res.add(avl.key);
			res.addAll(keysInOrder(avl.right));
		}
		return res;
	}

	// returns true if list is in 'natural order' and does not contain duplicates
	static public boolean isSortedInStrictAscendingOrder(List<Integer> keyL) {
		boolean isSortedInNaturalOrder = keyL.stream().sorted().collect(Collectors.toList()).equals(keyL);
		boolean doesNotContainDuplicates = (new HashSet<>(keyL)).size() == keyL.size();
		return (isSortedInNaturalOrder && doesNotContainDuplicates);
	}

	// compute number of nodes in tree
	static public int numNodes(AVL<Integer,Integer> avl) {
		if (avl.isEmpty())
			return 0;
		else
			return (1 + numNodes(avl.left) + numNodes(avl.right));
	}

	/***************************************************************************************
	 * SECTION: EXAMPLES
	 * PURPOSE: example-based tests
	 * NOTES:
	 * - Implementation of additional examples is optional
	 * - If you do implement more examples, their definition should go at the end of this section
 	 *************************************************************************************/
	@Example
	void exIsEmpty() {
		AVL<Integer, Integer> avl = AVL.empty();
		System.out.println(avl);
		Assertions.assertThat(avl.isEmpty()).isTrue();
		Assertions.assertThat(height(avl)).isEqualTo(0);
	}

	@Example
	void exInsert1() {
		AVL<Integer, Integer> avl = AVL.empty();
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (7,0)");
		avl = avl.insert(7, 0);
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (2,0)");
		avl = avl.insert(2, 0);
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (10,0)");
		avl = avl.insert(10, 0);
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (1,0)");
		avl = avl.insert(1, 0);
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (5,0)");
		avl = avl.insert(5, 0);
		System.out.println("tree: " + avl);
		System.out.println("key list: " + keysInOrder(avl));
		System.out.println("inserting: (8,0), (11,0), (0,0), (3,0), (6,0), (9,0), (12,0), (4,0)");
		avl = avl.insert(8, 0);
		avl = avl.insert(11, 0);
		avl = avl.insert(0, 0);
		avl = avl.insert(3, 0);
		avl = avl.insert(6, 0);
		avl = avl.insert(9, 0);
		avl = avl.insert(12, 0);
		avl = avl.insert(4, 0);
		System.out.println("final tree:  " + avl);
		List<Integer> keyL = keysInOrder(avl);
		System.out.println("final key list: " + keyL);
		Assertions.assertThat(isBalanced(avl)).isTrue();
		Assertions.assertThat(isSortedInStrictAscendingOrder(keyL)).isTrue();
	}

	@Example
	void exInsert2() {
		AVL<Integer, Integer> avl = AVL.empty();
		avl = avl.insert(7, 1);
		avl = avl.insert(2, 0);
		avl = avl.insert(10, 0);
		avl = avl.insert(1, 0);
		avl = avl.insert(5, 0);
		avl = avl.insert(8, 0);
		avl = avl.insert(11, 0);
		avl = avl.insert(0, 0);
		avl = avl.insert(3, 0);
		avl = avl.insert(6, 0);
		avl = avl.insert(9, 0);
		avl = avl.insert(12, 0);
		avl = avl.insert(4, 0);
		System.out.println("avl1:  " + avl);
		Assertions.assertThat(avl.toString()).isEqualTo("[7-1-5:[2-0-4:[1-0-2:[0-0-1:[][]][]][5-0-3:[3-0-2:[][4-0-1:[][]]][6-0-1:[][]]]][10-0-3:[8-0-2:[][9-0-1:[][]]][11-0-2:[][12-0-1:[][]]]]]");
	}

	@Example
	void exInsertAndFind1() {
		AVL<Integer, Integer> avl = AVL.empty();
		avl = avl.insert(0, 0);
		avl = avl.insert(1, 0);
		List<Integer> keyL0 = keysInOrder(avl);
		System.out.println("keyL0: " + keyL0);
		AVL<Integer, Integer> avl0 = avl.clone();
		avl = avl.insert(2, 0);
		List<Integer> keyL1 = keysInOrder(avl);
		System.out.println("keyL1: " + keyL1);
		Assertions.assertThat(keyL0).doesNotContain(2);
		Assertions.assertThat(keyL1).contains(2);
		Assertions.assertThat(avl0.find(2)).isEmpty();
		Assertions.assertThat(avl.find(2)).isPresent();
		keyL1.remove(2);
		System.out.println("keyL1: " + keyL1);
		Assertions.assertThat(keyL1).isEqualTo(keyL0);
	}

	/**************************************************************************************************
	 * SECTION: PROPERTIES
	 *************************************************************************************************/

	/**************
	 * SUB-SECTION: PROPERTIES TO TEST THE GENERATOR AND FACILITATE MANUAL INSPECTION OF GENERATED TREES
	 **************/

	// Property: "the height and number of nodes of generated trees lie within certain ranges"
	// change ranges to force search for counter examples
	@Property
	@Report(Reporting.GENERATED)
	void propGeneratorCanProduceTreeOfCertainHeightAndNumNodes (@ForAll("avlTrees") AVL<Integer,Integer> avl) {
		final int minHeight = 0;
		final int maxHeight = 5;
		final int minNumNodes = 0;
		final int maxNumNodes = 16;
		Assertions.assertThat(height(avl)).isBetween(minHeight,maxHeight);
		Assertions.assertThat(numNodes(avl)).isBetween(minNumNodes,maxNumNodes);
	}

	// Property: "generator can/cannot produce a specific tree"
	@Property
	@Report(Reporting.GENERATED)
	void propGeneratorCannotProduceSpecificTree (@ForAll("avlTrees") AVL<Integer,Integer> avl) {
	//	final String wantedStr = "[1-0-1:[][]]";  // likely to be generated, i.e., should make assertion fail w/ sufficiently large number of tries
	//	final String wantedStr = "[2-0-2:[1-0-1:[][]][3-0-1:[][]]]";  // likely to be generated, i.e., should make assertion fail w/ sufficiently large number of tries
		final String wantedStr = "[3-0-3:[2-0-2:[1-0-1:[][]][]][]]";  // imbalanced, i.e., should make assertion succeed, regardless of number of tries
		Assertions.assertThat(avl.toString()).isNotEqualTo(wantedStr);
	}

	// Property: "generator produces trees that are balanced"
	@Property
	@Report(Reporting.GENERATED)
	void propGeneratorProducesBalancedTrees (@ForAll("avlTrees") AVL<Integer,Integer> avl) {
		Assertions.assertThat(isBalanced(avl)).isTrue();
	}

	// Property: "generator produces trees that have the 'search tree property'" (see course notes)
	@Property
	@Report(Reporting.GENERATED)
	void propGeneratorProducesSearchTrees (@ForAll("avlTrees") AVL<Integer,Integer> avl) {
		Assertions.assertThat(isSearchTree(avl)).isTrue();
	}


	/***********************************************
	 * SUB-SECTION: PROPERTIES TO TEST 'INSERT'
	 * NOTES: Properties can use:
	 * - all methods in class 'AVL' except 'delete'
	 * - all helper methods defined in this class ('AVLProperties')
	 **********************************************/

	// Property: "insert(k,v) preserves the balanced search tree properties"
	// Note that since insert is used by the generator to build trees, the checks above that it produces
	// balanced search trees only make this check unnecessary
	@Property
	@Report(Reporting.GENERATED)
	void propInsertPreservesBalancedSearchTreeNess (@ForAll("avlTrees") AVL<Integer,Integer> avl,
													@ForAll @IntRange(min=0,max=15) Integer k) {
		avl = avl.insert(k,0);
		Assertions.assertThat(isBalanced(avl)).isTrue();
		Assertions.assertThat(isSearchTree(avl)).isTrue();
	}

	// Property: "inserting a key produces correct key list (properly ordered and contains k once)"
	@Property
	@Report(Reporting.GENERATED)
	void propInsertProducesProperKeyList1 (@ForAll("avlTrees") AVL<Integer,Integer> avl,
										   @ForAll @IntRange(min=0,max=15) Integer k) {
		AVL<Integer,Integer> avl0 = avl.clone();   // make copy of original tree
		List<Integer> keyL0 = keysInOrder(avl0);
		avl = avl.insert(k,0);
		List<Integer> keyL1 = keysInOrder(avl);
		Assertions.assertThat(isSortedInStrictAscendingOrder(keyL1)).isTrue();
		Assertions.assertThat(keyL1).contains(k);
		if (avl0.find(k).isEmpty()) {   // inserted key was new/fresh
			keyL1.remove(k);
		}
		Assertions.assertThat(keyL1).isEqualTo(keyL0);
	}

	// Property: "insert is idempotent, i.e., inserting the same key twice does not change the tree"
	@Property
	@Report(Reporting.GENERATED)
	void propInsertIsIdempotent (@ForAll("avlTrees") AVL<Integer,Integer> avl,
								 @ForAll @IntRange(min=0,max=15) Integer k) {
		System.out.println("tree0: " + avl);
		avl = avl.insert(k,0);
		System.out.println("tree1: " + avl);
		String avl1Str = avl.toString();
		avl = avl.insert(k,0);
		String avl2Str = avl.toString();
		Assertions.assertThat(avl2Str).isEqualTo(avl1Str);
	}

	// Part II (Questions 2a, 2b, and 2c)

	// Q2a, Property P1: "find(k) after insert(k,v) produces v (i.e., after insert(k,v), the tree stores v under k)"
	// <Your implementation of P1 here>

	// Q2b, Property P2: "insert(k) increases number of nodes by 1 if k new, and maintains it otherwise"
	// <Your implementation of P2 here>

	// Q2c, Property P3: "insert(k) increases height of tree by at most 1 if k new, and maintains it otherwise"
	// <Your implementation of P3 here>

	/***********************************************
	 * SUB-SECTION: PROPERTIES TO TEST 'DELETE'
	 * Your answers to Question 3 go into this section
	 * NOTES: properties can use:
	 * - all methods in 'AVL'
	 * - all helper methods defined in this class
	 **********************************************/

	// Property 1
	// <informal description here>
	// Fails on starter code: <yes or no answer here>
	// Fails on corrected code: <yes or no answer here>
	// <Your implementation of property here>


	// Property 2
	// <informal description here>
	// Fails on starter code: <yes or no answer here>
	// Fails on corrected code: <yes or no answer here>
	// <Your implementation of property here>


	// Property 3
	// <informal description here>
	// Fails on starter code: <yes or no answer here>
	// Fails on corrected code: <yes or no answer here>
	// <Your implementation of property here>


	// Property 4
	// <informal description here>
	// Fails on starter code: <yes or no answer here>
	// Fails on corrected code: <yes or no answer here>
	// <Your implementation of property here>


	/****************************************************************************************
	 * SECTION: GENERATORS
	 * NOTES:
	 * - The definition of additional generators should not be necessary
	 * - If you do implement more generators, their definition should go into this section
	 ****************************************************************************************/

	@Provide
	Arbitrary<AVL<Integer, Integer>> avlTrees() {
		final int minKey = 0;
		final int maxKey = 15;
		final int maxNumNodes = maxKey-minKey+1;
		final int minVal = 0;
		final int maxVal = minVal;  // generate trees containing value 0 only
		Arbitrary<Integer> keys = Arbitraries.integers().between(minKey,maxKey);
		Arbitrary<Integer> values = Arbitraries.integers().between(minVal,maxVal);
		Arbitrary<List<Tuple2<Integer,Integer>>> keysAndValues =
				Combinators.combine(keys,values)
						.as(Tuple::of)
						.list()
						.uniqueElements(Tuple1::get1)
						.ofMinSize(0)
						.ofMaxSize(maxNumNodes);
		return keysAndValues.map(keyValueList -> {
			AVL<Integer,Integer> avl = AVL.empty();
			for (Tuple2<Integer,Integer> kv : keyValueList) {
				avl = avl.insert(kv.get1(), kv.get2());
			}
			return avl;
		});
	}
}
