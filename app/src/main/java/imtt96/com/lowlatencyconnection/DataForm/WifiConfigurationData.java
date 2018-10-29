package imtt96.com.lowlatencyconnection.DataForm;

/**
 * Created by imtt9 on 2018-07-11.
 */

public class WifiConfigurationData {
    private String ssid;
    private String password;
    private String security;
    private String encryption;

    public WifiConfigurationData(String ssid, String password, String security, String encryption) {
        this.ssid = ssid;
        this.password = password;
        this.security = security;
        this.encryption = encryption;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }


}
