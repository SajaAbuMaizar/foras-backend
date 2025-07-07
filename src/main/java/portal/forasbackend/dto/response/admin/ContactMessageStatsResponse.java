package portal.forasbackend.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageStatsResponse {
    private long totalMessages;
    private long newMessages;
    private long inProgressMessages;
    private long doneMessages;
    private long archivedMessages;
    private long unreadMessages;
    private long todayMessages;
    private long weekMessages;
    private long monthMessages;
}