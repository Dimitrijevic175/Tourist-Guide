package org.example.backend.repositories.activity;

import org.example.backend.entities.Activity;

import java.util.List;

public interface ActivityRepository {

    List<Activity> allActivities();
    Activity addActivity(Activity activity);
     Activity getActivityById(Long id);
     Activity getActivityByName(String name);

}
