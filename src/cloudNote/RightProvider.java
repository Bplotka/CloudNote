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
            case 3:
                return CAN_GRANT_READ;
            case 4:
                return CAN_GRANT_READ_WRITE;
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
            case CAN_GRANT_READ:
                return 3;
            case CAN_GRANT_READ_WRITE:
                return 4;
            default:
                return 0;
        }
    }
}
