package org.koreait;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static boolean debug = true;     //평소에 개발할때는 false, 확인할때는 true로 두고 확인해보기
    public static int runCallCount = 0;

    public static int run(String exp) {
        return _run(exp, 0);
    }

    public static int _run(String exp, int depth) {
        // 3 - 1 + (1 - (4 _ 1 - (1 - 1)))
        runCallCount++;

        exp = exp.trim();   //양 옆의  쓸데없는 공백 제거 ex) " 20 + 20 " => "20 + 20", 가운데에 있는 공백을 없애주지는 않음.
        // 괄호 제거
        exp = stripOuterBrackets(exp);

        // 만약에 -( 패턴이라면, 내가 갖고있는 코드는 해석할 수 없으므로 해석할 수 있는 형태로 수정
        int[] pos = null;
        while ((pos = findCaseMinusBracket(exp)) != null) {
            exp = changeMinusBracket(exp, pos[0], pos[1]);
        }

        exp = stripOuterBrackets(exp);

        if (debug) {
            System.out.print(" ".repeat(depth * 4));
            System.out.printf("exp(%d) : %s\n", runCallCount, exp);
        }
        // 단일 항이 들어오면 바로 리턴
        if (!exp.contains(" ")) { //" " 공백이 있는지 없는지 체크
            return Integer.parseInt(exp);
        }

        boolean needToPlus = exp.contains(" + ") || exp.contains(" - ");
        boolean needToMulti = exp.contains(" * ");
        boolean needToSplit = exp.contains("(") || exp.contains(")");
        boolean needToCompound = needToPlus && needToMulti;   //섞여있어?!

        if (needToSplit) {              //문장을 잘라야할 때 실행된다.
            exp = exp.replaceAll("- ", "+ -");
            int splitPointIndex = findSplitPointIndex(exp);

            String firstExp = exp.substring(0, splitPointIndex);
            String secondExp = exp.substring(splitPointIndex + 1);

            char operator = exp.charAt(splitPointIndex);

            exp = Calc._run(firstExp, depth + 1) + " " + operator + " " + Calc._run(secondExp, depth + 1);

            return Calc._run(exp, depth + 1);
        } else if (needToCompound) {   //True일때 실행.
            String[] bits = exp.split(" \\+ ");

            String newExp = Arrays.stream(bits)
                    .mapToInt(e -> Calc._run(e, depth + 1))        //map: 1대1 대응 하는것. 정수화 하는 것. , run을 실행
                    .mapToObj(e -> e + "")                              //객체가 아닌데 객체로 바꿔야될 때
                    .collect(Collectors.joining(" + "));    // " + "로 엮는다.

            return _run(newExp, depth + 1);         //재귀함수, 처음으로 돌아가서 값을 처리함.
        }

        if (needToPlus) {
            exp = exp.replaceAll("- ", "+ -");// -로 들어온 경우 + -로 치환


            String[] bits = exp.split(" \\+ "); // + 로 자름

            int sum = 0;

            for (int i = 0; i < bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        } else if (needToMulti) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for (int i = 0; i < bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("해석 불가 : 올바른 계산식이 아니야");

    }

    private static String changeMinusBracket(String exp, int startPos, int endPos) {
        String head = exp.substring(0, startPos);
        String body = "(" + exp.substring(startPos + 1, endPos + 1) + " * -1)";
        String tail = exp.substring(endPos + 1);

        exp = head + body + tail;

        return exp;
    }

    private static int[] findCaseMinusBracket(String exp) {
        for (int i = 0; i < exp.length() - 1; i++) {
            if (exp.charAt(i) == '-' && exp.charAt(i + 1) == '(') {
                // 발견

                int bracketsCount = 1;

                for (int j = i + 2; j < exp.length(); j++) {
                    char c = exp.charAt(j);

                    if (c == '(') {
                        bracketsCount++;
                    } else if (c == ')') {
                        bracketsCount--;
                    }

                    if (bracketsCount == 0) {
                        return new int[]{i, j};
                    }
                }
            }
        }

        return null;
    }

    private static int findSplitPointIndex(String exp) { // 어디서 잘라야하는지 대신 찾아주는 역할
        int index = findSplitPointIndexBy(exp, '+');    // 찾아야하는 문자 "+"

        if (index >= 0) return index;                             // 문자를 찾으면 그 문자의 index를 리턴.

        return findSplitPointIndexBy(exp, '*');         // 아닐 경우 리턴값으로 *을 찾는 함수 실행
    }

    private static int findSplitPointIndexBy(String exp, char findChar) {
        int bracketsCount = 0;

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (c == '(') {
                bracketsCount++;
            } else if (c == ')') {
                bracketsCount--;
            } else if (c == findChar) {
                if (bracketsCount == 0) return i;
            }
        }
        return -1;
    }

    private static String stripOuterBrackets(String exp) {
        if (exp.charAt(0) == '(' && exp.charAt(exp.length() - 1) == ')') {
            int bracketsCount = 0;

            for (int i = 0; i < exp.length(); i++) {
                if (exp.charAt(i) == '(') {
                    bracketsCount++;
                } else if (exp.charAt(i) == ')') {
                    bracketsCount--;
                }

                if (bracketsCount == 0) {
                    if (exp.length() == i + 1) {
                        return stripOuterBrackets(exp.substring(1, exp.length() - 1));
                    }

                    return exp;
                }
            }
        }

        return exp;
    }
}
