package practica8;

import org.junit.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class HuffmanTest {
    static String path = "src/practica8/ficheros_prueba/";
    //static String testFiles[] = {"path + small.txt", "path + dark.txt", "path + medicine.txt"};
    static String testFiles[] = { path + "small.txt"};


    private Map<Character, List<Integer>> readCodes(String filename) throws IOException {
        Map<Character, List<Integer>> codes = new TreeMap<>();
        RandomAccessFile input = new RandomAccessFile(filename, "r");

        int count = input.readInt();
        while (count > 0) {
            Character c = input.readChar();
            int l = input.readInt();
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < l; i++)
                list.add((int) input.readByte());
            codes.put(c, list);
            count--;
        }
        input.close();

        return codes;
    }

    static public Map<Character, List<Integer>> computeCodeList(HuffmanTree tree) {
        Map<Character, List<Integer>> codesList = new TreeMap<>();
        for (char i=0; i<256; i++) {
            List<Integer> l =  tree.findCode(i);
            if (l != null)
                codesList.put(i, l);
        }

        return codesList;
    }

    public String toString(Map<Character, List<Integer>> codesList) {
        StringBuilder sb = new StringBuilder();
        for (Character c: codesList.keySet())
            if  (codesList.get(c).size() > 0)
                sb.append("Code['" + (c >= ' ' ? c :
                        "\\" + (int)c) + "'] (l=" + codesList.get(c).size() + "):" + " " + codesList.get(c).toString() + '\n');

        return sb.toString();
    }

    private String printableChar(char c) {
        if (c < ' ')
            return "'\\" + (int)c + "'";
        else
            return "'" + Character.toString(c) + "'";
    }


    @org.junit.Test
    public void findCodeTest() throws IOException {
        for (String filename : testFiles) {
            System.out.println("FICHERO " + filename);
            HuffmanTree tree = HuffmanTree.createFromFile(filename);
            Map<Character, List<Integer>> codes = readCodes(filename + ".codification");
            System.out.println(toString(codes));

            for (char c = 0; c < 255; c++) {
                System.out.println("PRUEBA CARACTER" + printableChar(c));
                List<Integer> l = tree.findCode(c);
                System.out.println("  Codigo esperado: " + codes.get(c));
                System.out.println("  Codigo obtenido: "+ l);
                if (!codes.containsKey(c) && l != null)
                    Assert.fail();
                else
                    assertEquals(codes.get(c), l);
                System.out.println("");
            }
        }
    }

    private String createCodedString(String input, Map<Character, List<Integer>>  codification) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            List<Integer> code = codification.get(c);
            for(Integer j: code)
                sb.append(j);
        }

        return sb.toString();
    }

    private String toString(List<Character> l) {
        StringBuilder sb = new StringBuilder();

        for (char c: l)
            sb.append(c);

        return sb.toString();
    }

    private String wrapString(String s, int width) {
        StringBuilder sb = new StringBuilder();

        int start = width;
        while (start < s.length()) {
            int end = (start +  width < s.length() ? start + width: s.length());
            sb.append(s.substring(start, end));
            sb.append('\n');
            start += width;
        }

        return sb.toString();
    }

    @org.junit.Test
    public void decodeTest() throws IOException {
        int count = 1;
        for (String filename : testFiles) {
            System.out.println("CODIFICACION DEL FICHERO " + filename);
            HuffmanTree tree = HuffmanTree.createFromFile(filename);
            Map<Character, List<Integer>> codification = computeCodeList(tree);
            System.out.println(toString(codification));
            Scanner input = new Scanner(new FileInputStream(filename));
            while(input.hasNextLine()) {
                System.out.println("PRUEBA " + count);
                String line = input.nextLine();
                System.out.println("  codificando \"" + line + "\"");
                String codedLine = createCodedString(line, codification);
                System.out.println(wrapString("  -> " + codedLine, 80));

                System.out.println("  decodificando");
                System.out.println("RESULTADOD ESPERADO ");
                System.out.println("  \"" + line + "\"");
                List<Character> decodedList = new ArrayList<>();
                tree.decode(codedLine, decodedList);
                System.out.println("RESULTADO OBTENIDO");
                String decodedLine = toString(decodedList);
                System.out.println("  \"" + decodedLine + "\"");
                assertEquals(line,decodedLine);
                count++;
                System.out.println("");
            }

            input.close();
        }

    }

    @org.junit.Test
    public void HuffmanTreeTest() throws IOException {
        for (String filename : testFiles) {
            System.out.println("FICHERO " + filename);
            HuffmanTree tree = HuffmanTree.createFromFile(filename);
            Map<Character, List<Integer>> codification = computeCodeList(tree);

            System.out.println("  RESULTADO ESPERADO");
            System.out.println(toString(codification));

            HuffmanTree resultado = new HuffmanTree(codification);

            System.out.println("  RESULTADO OBTENIDO");
            Map<Character, List<Integer>> resultadoCodes = computeCodeList(resultado);;
            System.out.println(toString(resultadoCodes));

            assertEquals(tree, resultado);
        }
    }

}
