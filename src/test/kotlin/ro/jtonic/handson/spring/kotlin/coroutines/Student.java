package ro.jtonic.handson.spring.kotlin.coroutines;

import java.util.Optional;

public record Student(
        String name,
        Integer age,
        Street address) {

    public record Street(String name, City city) {
    }

    public record City(String name, State state) {
    }

    public record State(String name) {
    }


    public static String getStateFromJava7(Student student) {
        // Java 7
        if (student != null) {
            Street street = student.address();
            if (street != null) {
                City city = street.city();
                if (city != null) {
                    State state = city.state();
                    if (state != null) {
                        String stateName = state.name();
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
                .map(Student::address)
                .map(Street::city)
                .map(City::state)
                .map(State::name)
                .orElse("unknown");
    }
}
