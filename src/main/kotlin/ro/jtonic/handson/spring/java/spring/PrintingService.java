package ro.jtonic.handson.spring.java.spring;

import org.springframework.stereotype.Component;

@Component
public class PrintingService {

    private final Printable printable;

    public PrintingService(Printable printable) {
        this.printable = printable;
    }

    public boolean print() {
        return printable.print();
    }
}
