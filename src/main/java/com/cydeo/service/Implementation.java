package com.cydeo.service;


import com.cydeo.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.cydeo.service.DataGenerator.*;


public class Implementation {


    /**
     * Read all student users.
     *
     * @return all the student users {@link User} and {@link Role}
     */
    public static List<User> readAllStudents() {
        return fillUsers().stream()
                .filter(user -> user.getRole().getName().equals("STUDENT"))
                .collect(Collectors.toList());
    }


    /**
     * Read all admin users.
     *
     * @return all the admin users {@link User} and {@link Role}
     */

    public static List<User> readAllAdmins() {
        return fillUsers().stream()
                .filter(user -> user.getRole().getName().equals("ADMIN"))
                .collect(Collectors.toList());

    }

    /**
     * Read all manager users.
     *
     * @return all the manager users {@link User} and {@link Role}
     */
    public static List<User> readAllManagers() {
        return fillUsers().stream()
                .filter(user -> user.getRole().getName().equalsIgnoreCase("MANAGER"))
                .collect(Collectors.toList());

    }

    /**
     * Read all SUSPENDED users.
     *
     * @return all the suspended users {@link User} and {@link UserState}
     */
    public static List<User> readAllSuspendedUsers() {
        return fillUsers().stream()
                .filter(user -> user.getState().equals(UserState.SUSPENDED))
                .collect(Collectors.toList());
    }

    /**
     * Read all CONFIRMED users.
     *
     * @return all the confirmed users {@link User} and {@link UserState}
     */
    public static List<User> readAllConfirmedUsers() {
        return fillUsers().stream()
                .filter(user -> user.getState().equals(UserState.CONFIRMED))
                .collect(Collectors.toList());
    }

    /**
     * Read all PENDING users.
     *
     * @return all the pending users {@link User} and {@link UserState}
     */
    public static List<User> readAllPendingUsers() {
        return fillUsers().stream()
                .filter(user -> user.getState().equals(UserState.PENDING))
                .collect(Collectors.toList());
    }


    /**
     * Count all courses.
     *
     * @return number of courses {@link Course}
     */
    public static Long countCourses() {
        return fillCourses().stream()
                .count();

    }


    /**
     * Sum the all duration of courses.
     *
     * @return sum of duration {@link Course}.
     */
    public static Integer sumDurationForAllData() {
        return fillCourses().stream()
                .collect(Collectors.summingInt(Course::getDuration));
    }

    /**
     * Find Courses attended by a specific user.
     *
     * @param id userId information is necessary.
     * @return all courses with user id {@link User} and {@link Course}
     */
    public static List<Course> findCoursesByUserId(Integer id) {
        User specificUser = findUserById(id);

        return fillCoursesAssigned().stream()

                .filter(course -> course.getUser().equals(specificUser))
                .map(CourseAssigned::getCourse)
                .collect(Collectors.toList());
    }


    /**
     * This method converts the duration of course to the weeks based on user information.
     * One week has 40 hours working time. Divider should be 40.
     *
     * @param id userId information is necessary.
     * @return converted duration to week. {@link BigDecimal}
     */
    public static Integer divideToWeek(Integer id) {
        return fillCoursesAssigned().stream()
                .filter(courseAssigned -> courseAssigned.getUser().equals(findUserById(id)))
                .map(courseAssigned -> courseAssigned.getCourse().getDuration())
//                .map(CourseAssigned::getCourse)
//                .map(Course::getDuration)
                .reduce(0,(a, b) -> (a + b) / 40);


    }

    /**
     * This method groups all the CourseAssigned based on their status
     * and numbers. (ex. 4 courses are IN_PROGRESS)
     *
     * @return all CourseStatus with number of courses in that status as a Map<CourseStatus, Long>
     * {@link CourseStatus}
     */
    public static Map<CourseStatus, Long> countCoursesByStatus() {
        return fillCoursesAssigned().stream()
                .collect(Collectors.groupingBy(CourseAssigned::getStatus, Collectors.counting()));
    }

    /**
     * This method finds the courses which has max duration
     * and min duration. Use teeing method.
     *
     * @return Map in this format (ex. Max Duration=1000)
     */

    public static Map<String, Integer> getMinMaxCourseDuration() {
        return fillCourses().stream()
                .collect(Collectors.teeing(
                        Collectors.maxBy(Comparator.comparing(Course::getDuration)),
                        Collectors.minBy(Comparator.comparing(Course::getDuration)),
                        (max, min) -> {
                            Map<String, Integer> result = new LinkedHashMap<>();
                            result.put("Max Duration", max.get().getDuration());
                            result.put("Min Duration", min.get().getDuration());
                            return result;
                        }
                ));
    }

    /**
     * This method finds the first user
     * who is first name starts with J
     *
     * @return the first user depending on first name
     */
    public static User findFirstWithFirstName() {
        return fillUsers().stream()
                .filter(user -> user.getFirstName().startsWith("J"))
                .findFirst().get();
    }

    /**
     * This method finds any user whose
     * last name is Wooden. If no user exists with this last name
     * then throw exception.
     *
     * @return the first user depending on first name
     */
    public static User findAnyWithLastName() {
        return fillUsers().stream()
                .filter(user -> user.getLastName().equals("Wooden"))
                .findAny().orElseThrow(() -> new NoSuchElementException("No user exists"));
    }

    /**
     * This method partitions the users
     * depending on whether user state is CONFIRMED
     *
     * @return map
     */
    public static Map<Boolean, List<User>> partitionOfConfirmedUsers() {
        return fillUsers().stream()
                .collect(Collectors.partitioningBy(user -> user.getState().equals(UserState.CONFIRMED)));
    }

    /**
     * This method groups all CourseAssigned depending on
     * CourseStatus
     *
     * @return map
     */

    public static Map<CourseStatus, List<CourseAssigned>> groupOfCourseAssigned() {
        return fillCoursesAssigned().stream()
                .collect(Collectors.groupingBy(CourseAssigned::getStatus));
    }
}