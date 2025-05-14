package ro.jtonic.handson.spring.java.spring;

import org.junit.jupiter.api.Test;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ro.jtonic.handson.spring.java.JavaSpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@JavaSpringIntegrationTest
class PrintingServiceIT {

    @Autowired
    PrintingService printingService;

    @MockitoSpyBean
    Printable printable;

    @Test
    void testPrint() {

        MockingDetails mockingDetails = Mockito.mockingDetails(printable);
        MockCreationSettings<?> mockCreationSettings = mockingDetails.getMockCreationSettings();
        MockName mockName = mockCreationSettings.getMockName();

        // arrange
        doReturn(false).when(printable).print();

        // act
        var result = printingService.print();

        // assert
        verify(printable, times(1)).print();
        assertFalse(result);
    }

    @Test
    void testPrint2() {
        // arrange
        Mockito.doCallRealMethod().when(printable).print();

        // act
        var result = printingService.print();

        // assert
        verify(printable, times(1)).print();
        assertTrue(result);
    }
}