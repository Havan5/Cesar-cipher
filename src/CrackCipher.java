
/**
 * @author: Havan Patel
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class CrackCipher {
	static HashMap<Integer, Integer> ciphertextHash = new HashMap<>();
	static HashMap<Integer, Integer> knowntextHash = new HashMap<>();
	static HashMap<Character, Character> offsetHash = new HashMap<>();
	static ArrayList<Character> keys = new ArrayList<>();
	static ArrayList<Character> values = new ArrayList<>();
	static StringBuilder mySb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		CrackingCipher(args[0], args[1]);
//		CrackingCipher("ciphertext-sample.txt", "knowntext-sample.txt");
	}

	public static void CrackingCipher(String cipherText, String knowntext) {
		readFile(knowntext);
		readFile(cipherText);

		sortHashMap(ciphertextHash, 0);
		sortHashMap(knowntextHash, 1);

		for (int i = 0; i < keys.size(); i++) {
			offsetHash.put(keys.get(i), values.get(i));
		}
		finalOffset(cipherText);

		System.out.println(mySb.toString());
	}

	public static void finalOffset(String fileName) {
		BufferedReader reader = null;
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				if (line.isEmpty()) {
					mySb.append("\n");
				} else {
					for (char c : line.toLowerCase().toCharArray()) {
						if (offsetHash.get(c) == null) {
							mySb.append(c);
						} else {
							char cc = offsetHash.get(c);
							mySb.append(cc);
						}

					}
					mySb.append("\n");
				}
				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readFile(String fileName) {
		BufferedReader reader = null;
		InputStream is = null;
		int fileNo = 3;
		try {
			if (fileName.equals("ciphertext-sample.txt")) {
				fileNo = 1;
				is = new FileInputStream(fileName);
			} else if (fileName.equals("knowntext-sample.txt")) {
				fileNo = 2;
				is = new FileInputStream(fileName);
			}
			reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				if (!line.isEmpty()) {
					for (char c : line.toCharArray()) {
						if (Character.isLetter(c) || c == ' ') {
							if (fileNo == 1) {
								int value = ciphertextHash.getOrDefault((int) Character.toLowerCase(c), 0);
								ciphertextHash.put((int) Character.toLowerCase(c), value + 1);
							} else if (fileNo == 2) {
								int value = knowntextHash.getOrDefault((int) Character.toLowerCase(c), 0);
								knowntextHash.put((int) Character.toLowerCase(c), value + 1);
							}
						}
					}
				}

				line = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sortHashMap(HashMap<Integer, Integer> hashMap, int type) {
		Comparator<Entry<Integer, Integer>> valueComparator = new Comparator<Entry<Integer, Integer>>() {
			@Override
			public int compare(Entry<Integer, Integer> e1, Entry<Integer, Integer> e2) {
				Integer v1 = e1.getValue();
				Integer v2 = e2.getValue();
				return v1.compareTo(v2);
			}
		};
		Set<Entry<Integer, Integer>> entries = hashMap.entrySet();
		List<Entry<Integer, Integer>> listOfEntries = new ArrayList<Entry<Integer, Integer>>(entries);
		Collections.sort(listOfEntries, valueComparator);
		LinkedHashMap<Integer, Integer> sortedByValue = new LinkedHashMap<Integer, Integer>(listOfEntries.size());
		for (Entry<Integer, Integer> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		Set<Entry<Integer, Integer>> entrySetSortedByValue = sortedByValue.entrySet();
		for (Entry<Integer, Integer> mapping : entrySetSortedByValue) {
			int i1 = mapping.getKey();
			char ch = (char) i1;
			if (type == 0) {
				keys.add(ch);
			} else if (type == 1) {
				values.add(ch);
			}
		}
	}
}