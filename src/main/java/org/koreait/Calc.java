package org.koreait;

public class Calc {
    public static int run(String exp) {

        boolean needToPlus = exp.contains("+");
        boolean needToMinus = exp.contains("-");

        String[] bits = null;

        if (needToPlus) {
            bits = exp.split(" \\+ ");
        } else if (needToMinus) {
            bits = exp.split(" \\- ");
        }

        int a = Integer.parseInt(bits[0]);
        int b = Integer.parseInt(bits[1]);

        if (needToPlus) {
            return a + b;
        } else if (needToMinus) {
            return a - b;
        }

        throw new RuntimeException("해석 불가 : 올바른 계산식이 아니야");
    }
}
