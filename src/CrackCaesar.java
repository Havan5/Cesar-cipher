import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * @author: Havan Patel
 */
public class CrackCaesar {

	public static ArrayList<String> words = new ArrayList<>();
	public static HashSet<String> hash = new HashSet<String>();

	public static void main(String[] args) {
		CrackingCaesar(args[0], args[1]);
//		CrackingCaesar("ciphertext-sample.txt", "dictionary-sample.txt");

	}

	public static void CrackingCaesar(String cipherText, String dictionary) {
		readFile(dictionary);
		readFile(cipherText);
	}

	public static void readFile(String fileName) {
		BufferedReader reader;
		int fileNo = 0;
		int off = 0;
		try {
			if (fileName.equals("dictionary-sample.txt")) {
				fileNo = 1;
			}
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			while (line != null) {
				if (fileNo == 0) {
					if (line.isEmpty()) {
						System.out.println();
					} else {
						off = dycrypt(line.toString());
					}
				} else if (fileNo == 1) {
					words.add(line.toString());
				}
				line = reader.readLine();
			}
			reader.close();
			if (fileNo == 0) {
				System.out.println(off);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int dycrypt(String message) {
		StringBuilder dycreptedMessage = new StringBuilder();
		String[] finalMessage = new String[26];
		for (int offset = 0; offset < 26; offset++) {
			dycreptedMessage.setLength(0);
			for (char c : message.toCharArray()) {
				dycreptedMessage.append(shiftChars(c, offset));
			}
			finalMessage[offset] = dycreptedMessage.toString();
		}
		int index = 0;
		int max = 0;
		int currentMax = 0;
		Stack<Integer> l = new Stack<>();
		while (index < 26) {
			String[] word1 = finalMessage[index].split("\\s");
			for (String w : word1) {
				for (int j = 0; j < words.size(); j++) {
					if (words.get(j).equals(w)) {
						currentMax++;
					}
				}
			}
			if (currentMax > max) {
				l.push(index);
				max = currentMax;
			}
			currentMax = 0;
			index++;
		}
		int finalOffset = l.peek();
		l.clear();
		System.out.println(finalMessage[finalOffset].toString());
		return finalOffset;
	}

	public static char shiftChars(char c, int offset) {
		if (Character.isLetter(c)) {
			if (Character.isUpperCase(c)) {
				c = (char) ('A' + (c - 'A' + offset) % 26);
			} else {
				c = (char) ('a' + (c - 'a' + offset) % 26);
			}
		}
		return c;
	}

}
