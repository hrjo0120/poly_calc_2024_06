package org.koreait;

public class Calc {
    public static int run(String exp) {

        System.out.println(exp);    // 바꾸기 전

        exp = exp.replaceAll("- ", "+ -");// -로 들어온 경우 + -로 치환

        System.out.println(exp);    //바꾼 후

        String[] bits = exp.split(" \\+ "); // + 로 자름

        int a = Integer.parseInt(bits[0]);
        int b = Integer.parseInt(bits[1]);
        int c = 0;

        if (bits.length > 2) {  // 항이 2개 이상일 때
            c = Integer.parseInt(bits[2]);
        }

        return a + b + c;

//        throw new RuntimeException("해석 불가 : 올바른 계산식이 아니야");
    }
}
