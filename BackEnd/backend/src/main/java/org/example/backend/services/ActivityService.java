package org.example.backend.services;

import org.example.backend.entities.Activity;
import org.example.backend.entities.Article;
import org.example.backend.repositories.activity.ActivityRepository;

import javax.inject.Inject;
import java.util.List;

public class ActivityService {
    @Inject
    private ActivityRepository activityRepository;

    public List<Activity> allActivities() {
        return activityRepository.allActivities();
    }

    public Activity getActivityByName(String name) {
        return activityRepository.getActivityByName(name);
    }

    public Activity addActivity(Activity activity) {
        return activityRepository.addActivity(activity);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.getActivityById(id);
    }

}
