package ro.jtonic.handson.spring.kotlin.coroutines;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class Student {
    private String name;
    private Integer age;
    private Street address;


    @Getter
    @AllArgsConstructor
    public static class Street {
        private String name;
        private City city;
    }

    @Getter
    @AllArgsConstructor
    public static class City {
        private String name;
        private State state;
    }

    @Getter
    @AllArgsConstructor
    public static class State {
        private String name;
    }


    public static String getStateFromJava7(Student student) {
        // Java 7
        if (student != null) {
            Street street = student.getAddress();
            if (street != null) {
                City city = street.getCity();
                if (city != null) {
                    State state = city.getState();
                    if (state != null) {
                        String stateName = state.getName();
                        if (stateName != null) {
                            return stateName;
                        }
                        return "unknown";
                    }
                    return "unknown";
                }
                return "unknown";
            }
            return "unknown";
        }
        return "unknown";
    }

    public static String getStateFromJava8(Student student) {
        Optional<Student> studentOpt = Optional.ofNullable(student);
        // Java 8
        return studentOpt
                .map(Student::getAddress)
                .map(Street::getCity)
                .map(City::getState)
                .map(State::getName)
                .orElse("unknown");
    }
}
