// Round1 - 계산기
// 작성자 : 하주영

import java.math.BigDecimal;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame implements ActionListener
{
    public static JLabel input;   // 입력내용
    public static JLabel output;  // 연산결과
    static int check = 0; // 주어진 수가 범위를 초과했는 지 판별하는 수

    // default constructor
    public Calculator()
    {
        setTitle("Round1 - Calculator, Ver1.2"); // 윈도우 제목

        Container c = getContentPane();
        c.setLayout(new BorderLayout(5, 5)); // BorderLayout 사용
        c.setBackground(new Color(249, 231, 159));

        // 메뉴버튼 생성
        JMenu menu1 = new JMenu("옵션");
        JMenu menu2 = new JMenu("기타");
        JMenuItem m;

        m = new JMenuItem("종료하기");
        m.addActionListener(this);
        menu1.add(m);
        m = new JMenuItem("프로그램 정보");
        m.addActionListener(this);
        menu2.add(m);

        // 생성한 메뉴들을 메뉴바에 추가
        JMenuBar mBar = new JMenuBar();
        mBar.add(menu1); // menu1 버튼을 menubar에 추가
        mBar.add(menu2); // menu1 버튼을 menubar에 추가
        this.setJMenuBar(mBar); // 현재 프레임에 메뉴바를 추가

        // JPanel 추가
        NorthPanel NP = new NorthPanel(); // 값을 출력과 관련된 패널부분
        c.add(NP, BorderLayout.EAST);
        CenterPanel CP = new CenterPanel(); // 값의 입력과 관련된 패널부분
        c.add(CP, BorderLayout.SOUTH);

        // 윈도우 size 설정
        setSize(500, 800);
        setResizable(false); // 창의 크기 임의 조정을 제한함
    }

    // 메뉴에 대한 동작을 정의
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        if(actionCommand.equals("종료하기")) // 종료버튼 클릭 시
            System.exit(0); // 프로세스 종료
        else if(actionCommand.equals("프로그램 정보"))
            new newWindow(); // 새로운 윈도우를 띄워 정보출력
    }

    // main method
    public static void main(String[] args)
    {
        Calculator cal = new Calculator();
        cal.setVisible(true);
    }

    // Panel에 대한 설정
    public class NorthPanel extends JPanel
    {
        public NorthPanel() {
            setLayout(new GridLayout(2, 1)); // 세로2, 가로1로 레이아웃을 설정
            setBackground(new Color(249, 231, 159));
            // 기본출력 메시지 설정
            input = new JLabel("");
            output = new JLabel("");

            input.setFont(new Font("맑은 고딕", 0, 30));
            input.setOpaque(true); // 투명배경을 사용하지 않음
            input.setBackground(new Color(249, 231, 159));
            input.setForeground(Color.BLACK); // 나타나는 폰트색상 지정
            input.setHorizontalAlignment(SwingConstants.RIGHT); // 출력내용에 대한 오른쪽 정렬 - 1

            output.setFont(new Font("맑은 고딕", 0, 30));
            output.setOpaque(true); // 투명 배경을 사용하지 않음
            output.setBackground(new Color(249, 231, 159));
            output.setForeground(Color.BLACK);
            output.setHorizontalAlignment(SwingConstants.RIGHT); // 출력내용에 대한 오른쪽 정렬 - 2

            add(input); // 패널에 레이블 추가 - 1
            add(output); // 패널에 레이블 추가 - 2
        }
    }

    // 입력버튼의 배치, 동작에 대한 설정
    public class CenterPanel extends JPanel
    {
        public CenterPanel() {
            JButton[] bt = new JButton[28]; // 버튼 정보를 저장할 배열 선언
            setBackground(Color.WHITE); // 버튼이 위치할 Lebel의 배경색상 설정
            setLayout(new GridLayout(7, 4, 0, 0)); // 그리드 생성
            // 첫번째 열
            bt[0] = new JButton("7");
            bt[1] = new JButton("8");
            bt[2] = new JButton("9");
            bt[3] = new JButton("÷");
            // 두번째 열
            bt[4] = new JButton("4");
            bt[5] = new JButton("5");
            bt[6] = new JButton("6");
            bt[7] = new JButton("×");
            // 세번째 열
            bt[8] = new JButton("1");
            bt[9] = new JButton("2");
            bt[10] = new JButton("3");
            bt[11] = new JButton("-");
            // 네번째 열
            bt[12] = new JButton("0");
            bt[13] = new JButton("000");
            bt[14] = new JButton(".");
            bt[15] = new JButton("+");
            // 다섯번째 열
            bt[16] = new JButton("AC");
            bt[17] = new JButton("C");
            bt[18] = new JButton("=");
            bt[19] = new JButton("%");
            // 여섯번째 열
            bt[20] = new JButton("Hex");
            bt[21] = new JButton("Oct");
            bt[22] = new JButton("<<");
            bt[23] = new JButton(">>");
            // 일곱번째 열
            bt[24] = new JButton("&");
            bt[25] = new JButton("|");
            bt[26] = new JButton("^");
            bt[27] = new JButton("~");

            // 버튼 디자인, 액션에 대한 부분
            for (int i = 0; i < 28; i++) {
                // 숫자 key
                if (i % 4 != 3 && i < 16 && i != 14) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 30));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(241, 196, 15));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]); // 해당 버튼을 추가

                    // 숫자 key 액션지정
                    bt[i].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e)
                        {
                            // 사용자가 입력하는 내용을 반영해서 보여주는 내용 업데이트
                            JButton b = (JButton) e.getSource(); // 어떤 버튼이 눌렸는 지 식별
                            String oldtext = input.getText();    // 버튼 클릭 전의 입력 문자열
                            String text = b.getText();           // 버튼을 통해 입력받은 문자를 가져옴
                            String newtext = oldtext + text;     // 이를 추가하여 새로운 문자열 생성

                            // 문자열의 길이에 따라 출력하는 폰트의 크기를 우동적으로 조절
                            int n = newtext.length(); // 새롭게 생성한 문자열의 길이
                            if (n > 0 && n <= 20) // 문자열의 길이가 0 초과 10 이하인 경우
                                input.setFont(new Font("맑은 고딕", 0, 40));
                            else if(n > 20 && n <= 25)
                                input.setFont(new Font("맑은 고딕", 0, 30));
                            // 사용자 입력 값의 범위를 제한 - 최대 25자리의 식으로 제한(그 이상은 처리X)
                            if (n <= 25) {
                                input.setText(newtext); // 새롭게 생성한 문자열을 Label에 업데이트
                                output.setText("수식 입력 중...");
                            } else
                                output.setText("가용 입력 범위 초과");
                        }
                    });
                }
                // 사칙연산 (/, *, +, -) key
                else if ((i % 4 == 3) && i < 20) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 40));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(234, 150, 72));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 버튼이 클릭된 경우 다음의 액션을 수행한다.
                    bt[i].addActionListener(new MyListener());
                }
                // '.' key
                else if (i == 14) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 40));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(128, 139, 150));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 버튼이 클릭된 경우 다음의 액션을 수행한다.
                    bt[i].addActionListener(new MyListener());
                }
                // "AC" key (All Clear) - 지금까지 입력한 내용을 한 번에 완벽히 삭제
                else if (i == 16) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 30));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(236, 112, 99));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 버튼이 클릭된 경우 다음의 액션을 수행한다.
                    bt[i].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            input.setText("");  // 입력된 모든 내용을 삭제
                            output.setText(""); // 출력된 모든 내용을 삭제
                        }
                    });
                }
                // C키(backspace 키), 이전에 입력한 1개의 수를 삭제
                else if (i == 17) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 30));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(236, 112, 99));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 버튼이 클릭된 경우 다음의 액션을 수행한다.
                    bt[i].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int n = input.getText().length() - 1; // 입력된 값의 길이를 가져옴

                            // 입력된 값이 존재하지 않는다면
                            if (n <= 1) {
                                output.setText("삭제할 입력 내용이 없음...");
                                input.setText("");
                            }
                            // 입력된 값이 20자 이하인 경우
                            else if (n <= 20) {
                                input.setFont(new Font("맑은 고딕", 0, 40));
                                input.setText(input.getText().substring(0, n));
                                output.setText("최근 입력내용 1개 삭제...");
                            }
                            // 입력된 값이 25자 이하인 경우
                            else if (n<=25) {
                                input.setFont(new Font("맑은 고딕", 0, 30));
                                input.setText(input.getText().substring(0, n));
                                output.setText("최근 입력내용 1개 삭제...");
                            }
                        }
                    });
                }
                // '=' key
                else if (i == 18) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 40));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(255, 111, 0));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 입력에 대한 액션 추가
                    bt[i].addActionListener(new CalculatorListener());
                }
                // 'bit wise operation' key
                else if (i >= 22) {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 30));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(41, 182, 246));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 입력에 대한 액션을 취하는 부분
                    bt[i].addActionListener(new MyListener());
                }
                // 'Hex', 'Oct' key
                else {
                    // 버튼을 생성하는 부분
                    bt[i].setFont(new Font("맑은 고딕", 0, 30));
                    bt[i].setOpaque(true); // 투명배경을 사용하지 않음
                    bt[i].setBackground(new Color(46, 204, 113));
                    bt[i].setForeground(Color.BLACK);
                    add(bt[i]);

                    // 입력에 대한 액션을 취하는 부분
                    bt[i].addActionListener(new convListener());
                }
            }
        }
    }

    // 산술연산자, 비트단위 연산자가 입력된 경우에 대한 반응
    private static class MyListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource(); // 클릭된 버튼을 식별(이벤트가 발생한 object를 반환)
            int n = input.getText().length();    // 현재 입력된 식(문자열)의 길이를 구함
            Character c = input.getText().charAt(n - 1); // 현재 입력된 식에서 마지막 문자를 가져옴
            // 입력된 마지막 문자가 피연산자 -> 연산자 입력 허용
            if (c != '+' && c != '-' && c != '×' && c != '÷' && c != '.'
                    && c != '%' && c != '&' && c != '|' && c != '^' && c != '~'
                    && c != '<' && c != '>') {
                String oldtext = input.getText(); // 연산자를 입력하기 이전의 문자열을 가져옴
                String text = b.getText();        // 입력한 연산자를 가져옴
                String newtext = oldtext + text;  // 가져온 연산자를 추가하여 새로운 입력문자열 생성
                input.setText(newtext);           // 새롭게 생성한 문자열을 Label에 업데이트
            }
            // 입력된 마지막 문자가 연산자 -> 연산자 입력 거부
            else output.setText("입력 거부...(연속된 연산자 입력)");
        }
    }

    // 진법전환 버튼이 입력된 경우에 대한 반응
    private class convListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource(); // 클릭된 버튼을 식별(이벤트가 발생한 object를 반환)
            String oldtext = input.getText(); // 연산자를 입력하기 이전의 문자열을 가져옴
            String text = b.getText();        // 입력한 연산자를 가져옴
            String newtext = oldtext + text;  // 가져온 연산자를 추가하여 새로운 입력문자열 생성
            input.setText(newtext);           // 새롭게 생성한 문자열을 Label에 업데이트

            if(b.getText().equals("Hex")) {
                String result = convCalculator(input.getText());
                output.setFont(new Font("맑은 고딕", 0, 40));
                output.setText(" = " + result);
            }
            if(b.getText().equals("Oct")) {
                String result = convCalculator(input.getText());
                output.setFont(new Font("맑은 고딕", 0, 40));
                output.setText(" = " + result);
            }
        }
    }

    // '=' 입력 시 대응하는 Listener
    private class CalculatorListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            String s = input.getText(); // 입력된 연산식을 가져옴
            double result; // 연산결과를 저장할 double 형 변수

            // 가져온 연산식에 bit-wise operator 존재 -> Bit 연산 method 호출
            if(s.contains("&") || s.contains("|") || s.contains("^") || s.contains("<<") || s.contains(">>"))
                result = Bit_calculator(s);
            else if(s.contains("~"))
                result = Unary_bit_calculator(s);
            else // 가져온 연산식에 일반 operator만 존재 -> 일반 산술연산 method 호출
                result = Calculator(s).doubleValue();
            // output label 폰트 설정
            output.setFont(new Font("맑은 고딕", 0, 40));
            output.setText(" = " + Double.toString(result));


            // 연산결과의 타입을 확인하여 출력형식 산정(정수, 소수 구분)
            if(result == (int)result) // 연산결과 값이 정수인 경우
                output.setText(" = " + Integer.toString((int)result)); // 강제형변환(값이 없는 소수점 부분을 삭제)
            else // 연산결과 값이 소수인 경우
                output.setText(" = " + Double.toString(result));
        }
    }

    // 입력된 식에 대한 연산을 수행하는 method
    private BigDecimal Calculator(String inputExpression)
    {
        int i; // 반복문 변수
        BigDecimal result; // 연산결과

        // 부동소수점 오차 문제를 해결하기 위해 BigDecimal 사용
        ArrayList<BigDecimal> operand = new ArrayList<BigDecimal>();  // 입력된 식에서 피연산자를 별도로 저장하는 공간
        ArrayList<String> operator = new ArrayList<String>(); // 입력된 식에서 연산자를 별도로 저장하는 공간

        operator.add(null); // 연산자 저장공간을 null로 초기화
        String str = ""; // 가져온 연산식의 일부를 임시저장하는 문자열

        // 입력으로 받아온 수식을 탐색하여 처리하는 부분
        for (i = 0; i < inputExpression.length(); i++)
        {
            Character c = inputExpression.charAt(i); // 입력받은 연산식에 대한 문자를 하나씩 가져옴

            // 가져온 문자를 연산자와 피연산자로 구분
            if (Character.isDigit(c)) // 가져온 값이 피연산자(숫자)인 경우
            {
                str += Character.toString(c); // 가져온 문자를 문자열 str에 이어붙임
                if (i == inputExpression.length() - 1) // 해당문자가 입력한 연산식의 마지막 문자인 경우
                {
                    operand.add(new BigDecimal(str)); // operand에 마지막 피연산자를 추가
                    str = ""; // 문자열 str을 빈문자열로 초기화
                }
            }
            else if (c.equals('.')) // 가져온 값이 dot(".") 인 경우
                str += c; // 가져온 문자를 문자열 str에 이어붙임
            else // 가져온 값이 연산자인 경우
            {
                operand.add(new BigDecimal(str)); // 기존에 저장된 피연산자를 double로 변경하여 operand로 추가
                operator.add(Character.toString(c));  // 인식한 연산자를 operator에 추가
                str = ""; // 문자열 str을 빈문자열로 초기화
            }
        }

        // 저장된 피연산자가 입력한도를 넘지 않는 경우
        if (check == 0)
        {
            // 읽어온 연산자의 수 만큼 다음을 반복함
            for (i = 1; i < operand.size() ; i++)
            {
                String s = operator.get(i); // 저장된 연산자를 1개 불러옴
                double tmp; // 연산한 값을 임시저장하는 double형 변수

                if (s.equals("×")) // 곱셈연산
                {
                    BigDecimal operand1 = operand.get(i-1);
                    BigDecimal operand2 = operand.get(i);
                    BigDecimal ans = operand1.multiply(operand2); // 두 수를 곱함

                    operator.remove(i); // 연산을 마친 해당연산자를 ArrayList에서 제거함
                    operand.remove(i);
                    operand.remove(i - 1); // 연산을 마친 2개의 피연산자를 ArrayList에서 제거함
                    operand.add(i - 1, ans); // 연산에 대한 결과값을 추가함
                    i--;
                }
                // 나눗셈 연산
                else if (s.equals("÷"))
                {
                    BigDecimal operand1 = operand.get(i-1);
                    BigDecimal operand2 = operand.get(i);
                    BigDecimal ans = operand1.divide(operand2);

                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, ans);
                    i--;
                }
                // 나머지 연산
                else if (s.equals("%"))
                {
                    BigDecimal operand1 = operand.get(i-1);
                    BigDecimal operand2 = operand.get(i);
                    BigDecimal ans = operand1.remainder(operand2);

                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, ans);
                    i--;
                }
            }

            result = operand.get(0);
            for (i = 1; i < operand.size(); i++)
            {
                String s = operator.get(i);
                BigDecimal n = operand.get(i);
                // 덧셈연산
                if (s.compareTo("+") == 0)
                    result = result.add(n);
                // 뺄셈연산
                else if (s.compareTo("-") == 0)
                    result = result.subtract(n);
            }
            return result;
        }
        result = new BigDecimal("0");
        return result;
    }

    public double Bit_calculator(String inputExpression)
    {
        int i; // 반복문 변수
        int result; // 연산결과
        check = 0;
        ArrayList<Integer> operand = new ArrayList<Integer>();  // 피연산자 저장공간
        ArrayList<String> operator = new ArrayList<String>();   // 연산자 저장공간

        operator.add(null); // 연산자 저장공간을 null로 초기화

        String str = ""; // 빈문자열 str 생성

        for (i = 0; i < inputExpression.length(); i++)
        {
            Character c = inputExpression.charAt(i); // 입력받은 연산식에 대한 문자를 하나씩 가져옴
            String s = Character.toString(c); // 가져온 문자를 문자열str 에 저장(임시저장)
            System.out.println("Character : " + s); // 디버깅을 위한 부분

            // 가져온 값이 피연산자(숫자)인 경우
            if (Character.isDigit(c))
            {
                str += Character.toString(c); // 가져온 문자를 문자열 str에 이어붙임
                if (i == inputExpression.length() - 1) // 해당문자가 입력한 연산식의 마지막 문자인 경우
                    operand.add(Integer.parseInt(str)); //
            }
            // 가져온 값이 연산자인 경우
            else
            {
                if(c.equals('<')  || c.equals('>')) i++;
                operand.add(Integer.parseInt(str)); // 기존에 저장된 피연산자를 int로 변경하여 operand로 추가
                operator.add(Character.toString(c));  // 인식한 연산자를 operator에 추가
                str = ""; // 문자열 str을 빈문자열로 초기화
            }
        }

        if (check == 0)
        {
            for (i = 1; i < operand.size(); i++)
            {
                String s = operator.get(i); // 저장된 연산자를 하나씩 가져옴
                int tmp;
                // '&'연산
                if (s.equals("&"))
                {
                    tmp = operand.get(i - 1) & operand.get(i);
                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                    i--;
                }
                // '|'연산
                else if (s.equals("|"))
                {
                    tmp = operand.get(i - 1) | operand.get(i);
                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                    i--;
                }
                // '^' 연산
                else if (s.equals("^"))
                {
                    tmp = operand.get(i - 1) ^ operand.get(i);
                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                    i--;
                }
                // '<<' 연산
                else if (s.equals("<"))
                {
                    tmp = operand.get(i - 1) << operand.get(i);
                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                    i--;
                }
                // '>>' 연산
                else if (s.equals(">"))
                {
                    tmp = operand.get(i - 1) >> operand.get(i);
                    operator.remove(i);
                    operand.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                    i--;
                }
                // '~' 연산 - 단항연산자(처리에 대한 고민할 것)
                else if (s.equals("~"))
                {
                    tmp = ~operand.get(i-1);
                    // System.out.println("DEBUG : " + tmp); // 디버깅을 위한 테스트 코드
                    operator.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                }
            }
            result = operand.get(0); // operand의 가장 처음에 남는 값이 결과 값을 result로 가져옴
            return result;
        }
        return 0; // 유효범위 밖인 경우 연산결과로 0을 반환
    }

    public double Unary_bit_calculator(String inputExpression)
    {
        int i; // 반복문 변수
        int result; // 연산결과
        check = 0;
        ArrayList<Integer> operand = new ArrayList<Integer>();  // 피연산자 저장공간
        ArrayList<String> operator = new ArrayList<String>();   // 연산자 저장공간

        operator.add(null); // 연산자 저장공간을 null로 초기화

        String str = ""; // 빈문자열 str 생성

        for (i = 0 ; i < inputExpression.length() ; i++)
        {
            Character c = inputExpression.charAt(i); // 입력받은 연산식에 대한 문자를 하나씩 가져옴
            String s = Character.toString(c); // 가져온 문자를 문자열str 에 저장(임시저장)
            System.out.println("Character : " + s); // 디버깅을 위한 부분

            // 가져온 값이 피연산자(숫자)인 경우
            if (Character.isDigit(c))
            {
                str += Character.toString(c); // 가져온 문자를 문자열 str에 이어붙임
                if (i == inputExpression.length() - 1) // 해당문자가 입력한 연산식의 마지막 문자인 경우
                    operand.add(Integer.parseInt(str));
            }
            // 가져온 값이 단일 연산자인 경우
            else
            {
                operand.add(Integer.parseInt(str)); // 기존에 저장된 피연산자를 int로 변경하여 operand로 추가
                operator.add(Character.toString(c));  // 인식한 연산자를 operator에 추가
                str = ""; // 문자열 str을 빈문자열로 초기화
            }
        }

        if (check == 0)
        {
            for (i = 1; i < operand.size()+1; i++)
            {
                String s = operator.get(i); // 저장된 연산자를 하나씩 가져옴
                int tmp;

                // '~' 연산 - 단항연산자(처리에 대한 고민할 것)
                if (s.equals("~"))
                {
                    tmp = ~operand.get(i-1);
                    operator.remove(i);
                    operand.remove(i - 1);
                    operand.add(i - 1, tmp);
                }
            }
            result = operand.get(0); // operand의 가장 처음에 남는 값이 결과 값을 result로 가져옴
            return result;
        }
        return 0; // 유효범위 밖인 경우 연산결과로 0을 반환
    }

    public String convCalculator(String inputString)
    {
        String result; // 변환한 결과를 저장하는 문자열

        if(inputString.contains("Hex"))
        {
            int idx = inputString.indexOf("H");
            if(inputString.contains("."))
                return "유효하지 않은 입력...";
            int operand = Integer.parseInt(inputString.substring(0, idx));
            result = Integer.toHexString(operand); // 16진수변환 문자열 획득

            return result.toUpperCase();
        }
        else if(inputString.contains("Oct"))
        {
            int idx = inputString.indexOf("O");
            if(inputString.contains("."))
                return "유효하지 않은 입력...";
            int operand = Integer.parseInt(inputString.substring(0, idx));
            result = Integer.toOctalString(operand); // 16진수변환 문자열 획득

            return result;
        }
        else
            return "Error! : Invalid input value"; // 에러메시지 출력
    }

    public static class newWindow extends JFrame {
        // 버튼이 눌러지면 만들어지는 새 창을 정의한 클래스
        newWindow() {
            setTitle("새로 띄운 창");

            JPanel NewWindowContainer = new JPanel();
            setContentPane(NewWindowContainer);

            JLabel NewLabel = new JLabel("Round1 - Calculator, ver1.2");
            NewLabel.setFont(new Font("맑은 고딕", 0, 40));

            NewWindowContainer.add(NewLabel);

            setSize(800,800);
            setResizable(false);
            setVisible(true);
        }
    }
}