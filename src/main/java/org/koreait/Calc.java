package org.koreait;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        // (20 + 20) + 20
        exp = exp.trim();   //양 옆의  쓸데없는 공백 제거 ex) " 20 + 20 " => "20 + 20", 가운데에 있는 공백을 없애주지는 않음.
        // 괄호 제거
        exp = stripOuterBrackets(exp);

        // 단일 항이 들어오면 바로 리턴
        if (!exp.contains(" ")) { //" " 공백이 있는지 없는지 체크
            return Integer.parseInt(exp);
        }

        boolean needPlus = exp.contains("+") || exp.contains("-");
        boolean needMulti = exp.contains("*");
        boolean needToSplit = exp.contains("(") || exp.contains(")");
        boolean needToCompound = needPlus && needMulti;   //섞여있어?!

        if (needToSplit) {              //괄호가 존재하는 식이 넘어왔을 때. 쌍이되는 괄호의 갯수를 센다.
            int bracketsCount = 0;      //아직 카운트 하지 않았으니 0으로 둔다.
            int splitPointIndex = -1;   // 아직 못찾음.

            for (int i = 0; i < exp.length(); i++) {
                if (exp.charAt(i) == '(') {         // exp에 괄호("(") 1개가 있을경우
                    bracketsCount++;                // bracketsCount가 증가된다.
                } else if (exp.charAt(i) == ')') {  // exp에 괄호(")") 1개가 있을 경우
                    bracketsCount--;                // bracketsCount가 감소된다.
                }
                 if (bracketsCount == 0) {           // 따라서 bracketsCount 가 0이면,
                     splitPointIndex = i;            // splitPointIntex를 자르는 위치로 지정함.
                    break;
                }
            }
            String firstExp = exp.substring(0, splitPointIndex + 1);
            String secondExp = exp.substring(splitPointIndex + 4);

            return Calc.run(firstExp) + Calc.run(secondExp);
        } else if (needToCompound) {   //True일때 실행.
            String[] bits = exp.split(" \\+ ");

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

        throw new

                RuntimeException("해석 불가 : 올바른 계산식이 아니야");


    }

    private static String stripOuterBrackets(String exp) {
        int outerBracketsCount = 0;

        while (exp.charAt(0) == '(' && exp.charAt(exp.length() - 1 - outerBracketsCount) == ')') {// 괄호의 쌍을 찾기 위해 범위가 exp.length() - 1 - outerBracketsCount가 된다.
            outerBracketsCount++;
        }

        if (outerBracketsCount == 0) return exp;

        return exp.substring(outerBracketsCount, exp.length() - outerBracketsCount);
    }
}
