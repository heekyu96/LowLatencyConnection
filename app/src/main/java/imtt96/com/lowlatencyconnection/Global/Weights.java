package imtt96.com.lowlatencyconnection.Global;

import java.math.BigDecimal;

/**
 * Created by imtt9 on 2018-07-10.
 */

public class Weights {
    private final static Weights instance = new Weights();

    public static Weights getInstance() {
        return instance;
    }

    public BigDecimal[][] wih;
    public BigDecimal[][] who;


}
