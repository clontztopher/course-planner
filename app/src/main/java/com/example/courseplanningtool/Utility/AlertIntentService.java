package com.example.courseplanningtool.Utility;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.R;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AlertIntentService extends IntentService {
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private static final String ACTION_START_ALERTS = "com.example.courseplanningtool.action.START_ALERTS";
    private static final String NOTIFICATION_CHANNEL = "channel_notifications";
    private static int notificationId = 0;
    private static boolean alertsStarted = false;

    public AlertIntentService() {
        super("AlertIntentService");
    }

    public static void startActionAlerts(Context context) {
        Intent intent = new Intent(context, AlertIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            alertsStarted = true;
            createNotificationChannel();
            while (alertsStarted) {
                try {
                    List<String> alertData = getNotificationData();
                    if (!alertData.isEmpty()) {
                        for (String alert : alertData) {
                            createNotification(alert);
                        }
                    }
                    Thread.sleep(1000 * 30); // Thirty seconds for testing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void createNotificationChannel() {
        CharSequence name = "Upcoming Course/Assessment";
        String description = "Opt-in notifications for upcoming courses and assessments.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void createNotification(String text) {
        Log.d("NOTIFICATION_SIMULATION", text);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(++notificationId, notification);
    }

    private List<String> getNotificationData() {
        LocalDateTime today = LocalDateTime.now();
        Duration oneWeek = Duration.ofDays(7);

        List<String> alerts = new ArrayList<>();

        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<List<Course>> coursesFuture = courseRepo.getAllCourses();

        try {
            List<Course> courses = coursesFuture.get();
            for (Course course: courses) {
                if (course.hasStartAlert()) {
                    LocalDateTime startDate = LocalDate.parse(course.getStartDateString(), dtFormatter).atStartOfDay();
                    if (today.isAfter(startDate.minus(oneWeek)) && today.isBefore(startDate)) {
                        alerts.add("Course, " + course.getTitle() + " starting on " + startDate.format(dtFormatter));
                    }
                }
                if (course.hasEndAlert()) {
                    LocalDateTime endDate = LocalDate.parse(course.getEndDateString(), dtFormatter).atStartOfDay();
                    if (today.isAfter(endDate.minus(oneWeek)) && today.isBefore(endDate)) {
                        alerts.add("Course, " + course.getTitle() + " ending on " + endDate.format(dtFormatter));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AssessmentRepository assessmentRepo = new AssessmentRepository(getApplication());
        Future<List<Assessment>> assessmentFuture = assessmentRepo.getAllAssessments();

        try {
            List<Assessment> assessments = assessmentFuture.get();
            for (Assessment assessment: assessments) {
                if (assessment.hasStartAlert()) {
                    LocalDateTime startDate = LocalDate.parse(assessment.getStartDate(), dtFormatter).atStartOfDay();
                    if (today.isAfter(startDate.minus(oneWeek)) && today.isBefore(startDate)) {
                        alerts.add("Assessment, " + assessment.getAssessmentTitle() + " starting on " + startDate.format(dtFormatter));
                    }
                }
                if (assessment.hasEndAlert()) {
                    LocalDateTime endDate = LocalDate.parse(assessment.getEndDate(), dtFormatter).atStartOfDay();
                    if (today.isAfter(endDate.minus(oneWeek)) && today.isBefore(endDate)) {
                        alerts.add("Assessment, " + assessment.getAssessmentTitle() + " ending on " + endDate.format(dtFormatter));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alerts;
    }
}