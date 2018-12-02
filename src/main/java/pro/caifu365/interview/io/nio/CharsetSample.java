package pro.caifu365.interview.io.nio;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;

public class CharsetSample {
    public static void main(String[] args) {

        var defaultCharset = Charset.defaultCharset();
        System.out.println("默认的字符集：");
        System.out.println(defaultCharset);
        System.out.println("");

        SortedMap<String, Charset> charSets = Charset.availableCharsets();

        System.out.println("系统支持的字符集：");
        Collection<Charset> charsetList = charSets.values();
        for (Charset charset : charsetList) {
            System.out.println(charset);
        }


        /*Iterator<String> it = charSets.keySet().iterator();
        while (it.hasNext()) {
            String csName = it.next();
            System.out.print(csName);
            Iterator aliases = charSets.get(csName).aliases().iterator();
            if (aliases.hasNext())
                System.out.print(": ");
            while (aliases.hasNext()) {
                System.out.print(aliases.next());
                if (aliases.hasNext())
                    System.out.print(", ");
            }
            System.out.println();
        }*/
    }

}
