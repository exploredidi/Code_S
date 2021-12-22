import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Decode {
    
    static int TIMEOUT = 1200;
    static int HIGH = 1;
    static int LOW = 0;
    static int UNKNOWN = -1;
    static int ERR = -1;

    static int MORSE_PIN = 1; // 莫尔斯电报码信号线
    static int MANCH_PIN = 2; // 曼彻斯特码信号线
    static int RZERO_PIN = 3; // 归零码信号线
    static int NRZER_PIN = 4; // 非归零码信号线
    static int NRCLK_PIN = 0; // 时钟信号线
    static int CIN_PIN = 5; //自定义输入信号线
    static int COUT_PIN0 = 6; //自定义输出信号线1
    static int COUT_PIN1 = 7; //自定义输出信号线2
    static int COUT_PIN2 = 8; //自定义输出信号线3
    static int CIRCLE = 75; //信号周期

    private static final Map<String, String> MorseDict;

    static {
	//莫尔斯电报码表，0为短，1为长
        MorseDict = new HashMap<String, String>();
        MorseDict.put("01", "a");
        MorseDict.put("1000", "b");
        MorseDict.put("1010", "c");
        MorseDict.put("100", "d");
        MorseDict.put("0", "e");
        MorseDict.put("0010", "f");
        MorseDict.put("110", "g");
        MorseDict.put("0000", "h");
        MorseDict.put("00", "i");
        MorseDict.put("0111", "j");
        MorseDict.put("101", "k");
        MorseDict.put("0100", "l");
        MorseDict.put("11", "m");
        MorseDict.put("10", "n");
        MorseDict.put("111", "o");
        MorseDict.put("0110", "p");
        MorseDict.put("1101", "q");
        MorseDict.put("010", "r");
        MorseDict.put("000", "s");
        MorseDict.put("1", "t");
        MorseDict.put("001", "u");
        MorseDict.put("0001", "v");
        MorseDict.put("011", "w");
        MorseDict.put("1001", "x");
        MorseDict.put("1011", "y");
        MorseDict.put("1100", "z");
    }

    //等待高电平出现或超时
    static boolean wait_high(int channel) {
        long cnt = DecodeHelper.get_ms();
        while (0 == DecodeHelper.digitalRead(channel)) {
            if (DecodeHelper.get_ms() - cnt > TIMEOUT) {
                return false;
            }
        }
        return true;
    }

    //等待低电平出现或超时
    static boolean wait_low(int channel) {
        long cnt = DecodeHelper.get_ms();
        while (1 == DecodeHelper.digitalRead(channel)) {
            if (DecodeHelper.get_ms() - cnt > TIMEOUT) {
                return false;
            }
        }
        return true;
    }

    //等待跳变出现或超时
    static boolean wait_jump(int channel, int state) {
        if (UNKNOWN == state) {
            state = DecodeHelper.digitalRead(channel);
        }
        long cnt = DecodeHelper.get_ms();
        while (DecodeHelper.digitalRead(channel) == state) {
            if (DecodeHelper.get_ms() - cnt > TIMEOUT) {
                return false;
            }
        }
        return true;
    }

    //等待上一段信号结束，发生TIMEOUT以上的低电平
    static void wait_signal_end(int channel) {
        long cnt = DecodeHelper.get_ms();
        while (true) {
            if (DecodeHelper.digitalRead(channel) == HIGH) {
                cnt = DecodeHelper.get_ms();// 遇到高电平重新计时
            }
            if (DecodeHelper.get_ms() - cnt > TIMEOUT) {
                break;
            }
        }
    }

    //等待信号开始(上一段信号结束后的第一个高电平出现)
    static void wait_signal_start(int channel) {
        System.out.print("Wait next frame...\r\n");
        wait_signal_end(channel);
        System.out.print("Wait frame head...\r\n");
        while (DecodeHelper.digitalRead(channel) == LOW);
    }


    //曼彻斯特解码
    static void decode_manch(int channel){
	System.out.println("Talk is cheap, show me the code.");
    }

    //归零码
    static void decode_rz(int channel) {
	System.out.println("Talk is cheap, show me the code.");

    }

    //莫尔斯电报码
    static void decode_morse(int channel) {
	System.out.println("Talk is cheap, show me the code.");
    }

    //非归零码
    static void decode_nrz(int channel, int clk_channel) {
	System.out.println("Talk is cheap, show me the code.");
    }

    //输出电平演示，实现三灯流水灯效果
    static void light_demo() {
        DecodeHelper.digitalWrite(COUT_PIN0, LOW);
        DecodeHelper.digitalWrite(COUT_PIN1, LOW);
        DecodeHelper.digitalWrite(COUT_PIN2, LOW);
        for (int i = 0; i < 200; i++) {
            DecodeHelper.digitalWrite(COUT_PIN0, i % 3 % 2 > 0 ? HIGH : LOW);
            DecodeHelper.digitalWrite(COUT_PIN1, (i + 1) % 3 % 2 > 0 ? HIGH : LOW);
            DecodeHelper.digitalWrite(COUT_PIN2, (i + 2) % 3 % 2 > 0 ? HIGH : LOW);
            DecodeHelper.delay_ms(50);
        }
        DecodeHelper.digitalWrite(COUT_PIN0, LOW);
        DecodeHelper.digitalWrite(COUT_PIN1, LOW);
        DecodeHelper.digitalWrite(COUT_PIN2, LOW);
    }

    //以5毫秒为周期检测指定的信号线电平变化并显示在屏幕，按ESC结束
    static void signal_detect() {

        while (true) {

            int channel = 0;
            System.out.println("\r\nSelect channel:");
            System.out.println("1: NRZ");
            System.out.println("2: Morse");
            System.out.println("3: Manchester");
            System.out.println("4: RZ");
            System.out.println("5: CLK");
            System.out.println("6: DATA_IN");
            System.out.println("0: Back");

            Scanner input = new Scanner(System.in);
            String chc = input.next();
            if ("0".equals(chc)) {
                break;
            }
            switch (chc) {
                case "1":
                    channel = NRZER_PIN;
                    break;
                case "2":
                    channel = MORSE_PIN;
                    break;
                case "3":
                    channel = MANCH_PIN;
                    break;
                case "4":
                    channel = RZERO_PIN;
                    break;
                case "5":
                    channel = NRCLK_PIN;
                    break;
                case "6":
                    channel = CIN_PIN;
                    break;
                default:
                    continue;
            }

            //等待信号出现
            while (DecodeHelper.digitalRead(channel) == HIGH);
            while (DecodeHelper.digitalRead(channel) == LOW);
            DecodeHelper.delay_ms(25);

            while (true) {
                if (DecodeHelper.esc_key()) //ESC键结束
                {
                    break;
                }
                System.out.print(DecodeHelper.digitalRead(channel) == HIGH ? "▉" : "▓");
                DecodeHelper.delay_ms(5);
            }
        }
    }

    public static void main(String[] args) {
//        if (0 == DecodeHelper.open_dev()) {
//            System.out.println("Device not found or busy!");
//            return;
//        } else {
//            System.out.println("Device opened.");
//        }

        while (true) {
            System.out.println("1: Morse");
            System.out.println("2: Manchester");
            System.out.println("3: NRZ");
            System.out.println("4: RZ");
            System.out.println("5: Signal detect");
            System.out.println("6: Output DEMO");
            System.out.println("0: Quit");
            Scanner input = new Scanner(System.in);
            String chc = input.next();
            if ("0".equals(chc)) {
                break;
            }

            switch (chc) {
                case "1":
                    decode_morse(MORSE_PIN);
                    break;
                case "2":
                    decode_manch(MANCH_PIN);
                    break;
                case "3":
                    decode_nrz(NRZER_PIN, NRCLK_PIN);
                    break;
                case "4":
                    decode_rz(RZERO_PIN);
                    break;
                case "5":
                    signal_detect();
                    break;
                case "6":
                    light_demo();
                    break;
                default:
                    continue;
            }
        }

        DecodeHelper.close_dev();
    }

}
