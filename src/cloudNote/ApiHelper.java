package cloudNote;

import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;

import java.util.Iterator;
import java.util.List;
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

    public static String noteJson(List<NoteEntity> notes) {
        String ret = "[";
        Iterator it = notes.iterator();
        while (it.hasNext()) {
            NoteEntity note = (NoteEntity)it.next();
            ret += "{ ";
            ret += "\"id\": \"" + String.valueOf(note.getId())+ "\", ";
            ret += "\"title\": \"" +  String.valueOf(note.getTitle())+ "\", ";
            ret += "\"is_public\": \"" + String.valueOf(note.getIsPublic())+ "\", ";
            ret += "\"content\": \"" + String.valueOf(note.getContent())+ "\", ";
            ret += "\"create_time\": \"" + String.valueOf(note.getCreateTime())+ "\", ";
            ret += "\"last_modify\": \"" +  String.valueOf(note.getLastModify())+ "\" ";
            //ret += "\"author\": \"" +  String.valueOf(note.getCreateBy())+ "\", ";
            ret += "}";
            if (it.hasNext()) ret+=", ";
        }
        ret += "]";

        return ret;
    }

    public static String permissionJson(List<UserNoteRelationsEntity> permissions) {
        String ret = "[";
        Iterator it = permissions.iterator();
        while (it.hasNext()) {
            UserNoteRelationsEntity rights = (UserNoteRelationsEntity)it.next();
            ret += "{ ";
            ret += "\"id\": \"" + String.valueOf(rights.getId())+ "\", ";
            ret += "\"permission\": \"" +  String.valueOf(rights.getNoteRight())+ "\", ";
            ret += "\"user\": \"" + String.valueOf(rights.user.getLogin())+ "\" ";
          //  ret += "\"create_time\": \"" + String.valueOf(note.getCreateTime())+ "\", ";
           // ret += "\"last_modify\": \"" +  String.valueOf(note.getLastModify())+ "\", ";
            ret += "}";
            if (it.hasNext()) ret+=", ";
        }
        ret += "]";

        return ret;
    }
}
