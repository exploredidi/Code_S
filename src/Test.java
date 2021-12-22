/**
 * @Author: WenJianC
 * @Date: 2021/11/13 20:13
 */
public class Test {


    public static void main(String[] args) {


    }


    //曼彻斯特解码
    static void decode_manch_test(int channel){

        Decode.wait_signal_start(channel);

        //1000毫秒 = 1s
        int flag = 0;
        if(Decode.wait_low(channel)){//检测低电平
            long ms_start = DecodeHelper.get_ms();
            while (Decode.wait_low(channel)){//检测低电平
                long ms_end = DecodeHelper.get_ms();
                if(ms_end - ms_start > 2000){
                    flag = 1;
                    break;
                }
            }

            StringBuilder builder = new StringBuilder();

            if(flag == 1){//开始接受01010，不用判断标志，但是判断结束的逻辑有点问题，得问下老师。
                while (true){
                    long ms = DecodeHelper.get_ms();//保存第一个时间
                    if (Decode.wait_jump(channel,0)){
                        //电平跳跃
                        long ms_end = DecodeHelper.get_ms();
                        if(ms_end - ms > 1200){//电平跳跃后就计算时间
                            break;
                        }else {
                            builder.append("1");
                        }
                    }else {
                        long ms_end = DecodeHelper.get_ms();
                        if(ms_end - ms > 1200){
                            break;
                        }else {
                            builder.append("0");
                        }
                    }

                }
            }
        }

        //接下来就是去掉前四个1，然后解析字符

        System.out.println("Talk is cheap, show me the code.");
    }

    //归零码
    static void decode_rz_test(int channel) {

        System.out.println("Talk is cheap, show me the code.");


    }

    //莫尔斯电报码
    static void decode_morse_test(int channel) {


        System.out.println("Talk is cheap, show me the code.");
    }

    //非归零码
    static void decode_nrz_test(int channel, int clk_channel) {


        System.out.println("Talk is cheap, show me the code.");
    }
}
