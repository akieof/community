package cf.vsing.community.util;

public interface StatusUtil {
    int ACTIVATION_NULL=0;
    int ACTIVATION_OK=1;
    int ACTIVATION_REPEAT=2;
    int ACTIVATION_WRONG=3;
    int DEFAULT_EXPIRED_SECONDS=3600*24;
    int LONG_EXPIRED_SECONDS=3600*24*15;
}
