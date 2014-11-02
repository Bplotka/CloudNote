package cloudNote;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by bwplo_000 on 2014-11-02.
 */
public class ApiHelper {
    public enum Status{
        OK,
        ERROR,
        NO_AUTH
    }
    public static String returnJson(Status status, Map<String, String> return_fields){
        String ret = "{ " +
                "\"status\": \"" + status + "\", \"response\": {";
        Iterator it = return_fields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ret += " \"" + pairs.getKey() + "\": \"" + pairs.getValue() + "\"";
           if (it.hasNext()) ret+=",";
        }
         ret += " } }";

        return ret;
    }
}
