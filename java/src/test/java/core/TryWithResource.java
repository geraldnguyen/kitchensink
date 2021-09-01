package core;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.Closeable;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TryWithResource {

    @Test
    void shouldAutomaticallyCloseCloseableResource() throws IOException {
        var mockCloseable = Mockito.mock(Closeable.class);

        try (mockCloseable) {
            assertNotNull(mockCloseable);
        } catch (IOException e) {
            fail("IOException from Closeable.close() method");
        }

        verify(mockCloseable).close();
    }

    @Test
    void handleNullCloseableGracefully() throws IOException {
        Closeable mockCloseable = null;

        try (mockCloseable) {
            assertNull(mockCloseable);
        }
    }

    @Test
    void closeResourcesInLifoOrder() throws IOException {
        var mockCloseable1 = Mockito.mock(Closeable.class);
        var mockCloseable2 = Mockito.mock(Closeable.class);
        var mockCloseable3 = Mockito.mock(Closeable.class);

        try (mockCloseable1; mockCloseable2; mockCloseable3) {
            assertNotNull(mockCloseable1);
            assertNotNull(mockCloseable2);
            assertNotNull(mockCloseable3);
        } catch (IOException e) {
            fail("IOException from Closeable.close() method");
        }

        InOrder inOrder = inOrder(mockCloseable1, mockCloseable2, mockCloseable3);  // order here is not important
        inOrder.verify(mockCloseable3).close();
        inOrder.verify(mockCloseable2).close();
        inOrder.verify(mockCloseable1).close();
    }
}
