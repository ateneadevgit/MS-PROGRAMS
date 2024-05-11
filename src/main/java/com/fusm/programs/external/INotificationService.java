package com.fusm.programs.external;

import com.fusm.programs.model.external.NotificationRequest;
import com.fusm.programs.model.external.Template;
import org.springframework.stereotype.Service;

@Service
public interface INotificationService {

    String sendNotification(NotificationRequest notificationRequest);
    Template getTemplate(Integer templateId);

}
