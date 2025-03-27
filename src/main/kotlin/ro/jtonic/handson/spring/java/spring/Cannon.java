package ro.jtonic.handson.spring.java.spring;

import org.springframework.stereotype.Component;

@Component
public class Cannon implements Printable {
    @Override
    public boolean print() {
        System.out.println("Cannon is printing...");
        return true;
    }
}
