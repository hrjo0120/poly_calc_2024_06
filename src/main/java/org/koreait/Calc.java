package org.koreait;

public class Calc {
    public static int run(String exp) {

        boolean needPlus = exp.contains("+");
        boolean needMulti = exp.contains("*");

        if(needPlus) {
            exp = exp.replaceAll("- ", "+ -");// -로 들어온 경우 + -로 치환

            String[] bits = exp.split(" \\+ "); // + 로 자름

            int sum = 0;

            for(int i = 0; i< bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        } else if(needMulti) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for(int i = 0; i< bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("해석 불가 : 올바른 계산식이 아니야");

    }
}
