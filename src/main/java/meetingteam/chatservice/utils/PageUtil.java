package meetingteam.chatservice.utils;

public class PageUtil {
    public static Integer findBestPageSize(Integer receivedNumber) {
        int bestPageSize=20;
        int maxMessagesNum=0;
        int messagesNum=0;
        for(int i=20;i>=2;i--) {
            messagesNum=i*(receivedNumber/i+1)-receivedNumber;
            if(messagesNum>maxMessagesNum) {
                maxMessagesNum=messagesNum;
                bestPageSize=i;
            }
            if(maxMessagesNum>=i-1) break;
        }
        return bestPageSize;
    }
}
