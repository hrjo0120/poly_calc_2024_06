package org.koreait;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        // 단일 항이 들어오면 바로 리턴
        if(!exp.contains(" ")){ //" " 공백이 있는지 없는지 체크
            return Integer.parseInt(exp);
        }

        boolean needPlus = exp.contains("+") || exp.contains("-");
        boolean needMulti = exp.contains("*");

        boolean needToCompound = needPlus && needMulti;   //섞여있어?!

        if (needToCompound) {   //True일때 실행.
            String[] bits = exp.split(" \\+ "); //{"20", "10", "5 * 2"}

            String newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)        //map: 1대1 대응 하는것. 정수화 하는 것. , run을 실행
                    .mapToObj(e -> e + "")      //객체가 아닌데 객체로 바꿔야될 때
                    .collect(Collectors.joining(" + "));    // " + "로 엮는다.

            return run(newExp);         //재귀함수, 처음으로 돌아가서 값을 처리함.
        }

        if (needPlus) {
            exp = exp.replaceAll("- ", "+ -");// -로 들어온 경우 + -로 치환

            String[] bits = exp.split(" \\+ "); // + 로 자름

            int sum = 0;

            for (int i = 0; i < bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        } else if (needMulti) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for (int i = 0; i < bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("해석 불가 : 올바른 계산식이 아니야");

    }
}
