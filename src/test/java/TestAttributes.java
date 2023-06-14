import com.patrity.Generator;
import com.patrity.model.AttributeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAttributes {

    @Test
    public void verifyAttributesSet() {
        System.out.println("bla");
        Generator g = new Generator();
        var attribute = g.getAttribute(AttributeType.BACKGROUND, null);
        var attribute2 = g.getAttribute(AttributeType.BACKGROUND, null);

        assertEquals(attribute, attribute2);
    }
}
