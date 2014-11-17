package cloudNote;

import static cloudNote.RightEnum.*;
import static cloudNote.RightEnum.NONE;

/**
 * Created by Piotr on 2014-11-16.
 */
public class RightProvider {

    public static RightEnum getRightFromInt(int right)
    {
        switch(right)
        {
            case 1:
                return READ;
            case 2:
                return READ_WRITE;
            default:
                return NONE;
        }
    }

    public static Integer getRightFromEnum(RightEnum right) {
        switch(right)
        {
            case READ:
                return 1;
            case READ_WRITE:
                return 2;
            default:
                return 0;
        }
    }

    public static Integer getRightFromString(String right) {
        if(right.equals("R"))
        {
            return 1;
        }
        else if(right.equals("RW"))
        {
            return 2;
        }
        return 0;
    }
}
