package com.coinnews;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
            Dotenv dotenv = Dotenv.configure().load();
        System.out.println("Web Hook ID: " + dotenv.get("WEB_HOOK_ID"));
        System.out.println("Scheduler Start");

        try {
            Scheduler scheduler = new Scheduler();
            News news = new News();
            scheduler.schedule(
                    news::execute,
                    Schedules.fixedDelaySchedule(Duration.ofSeconds(5))
            );
        } catch (NoClassDefFoundError e) {
            System.out.println("Main Error: " + e.getMessage());
        }
    }
}