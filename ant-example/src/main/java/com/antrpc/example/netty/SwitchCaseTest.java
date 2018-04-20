package com.antrpc.example.netty;

import com.antrpc.common.protocal.AntProtocal;
import com.antrpc.remoting.model.RemotingTransporter;
import org.xerial.snappy.Snappy;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-04-20
 * Time: 9:46
 */
public class SwitchCaseTest
{

    public SwitchCaseTest(State state) {
        this.state = state;
    }

    private State state;

    public State getState() {
        System.out.println(",,,,");
        return state;
    }

    public void setState(State state){
        this.state = state;
    }

    public void checkState(){
        switch (getState()) { // getState如果获取枚举类型实际是返回一个引用，随着 引用值的变化 ， 下面顺序都会执行
            case A:
                System.out.println("A");
                setState(State.B);
            case B :
                System.out.println("B");
                setState(State.C);
            case C:
                System.out.println("C");
                setState(State.D);
            case D:
                System.out.println("D");
                setState(State.E);
            case E:
                System.out.println("E");
                break;
            default:
                System.out.println("default");
                break;
        }
        System.out.println("RESET");
        setState(State.A);
    }

    public static void main(String[] args) {
        SwitchCaseTest test = new SwitchCaseTest(State.A);
        test.checkState();
    }


    enum State{
        A,B,C,D,E;
    }
}
