package unit.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserHandlerTest {

    @Test
    @DisplayName("Register Works")
    public void registerWorks(){
        assert true;
    }

    @Test
    @DisplayName("Register Doesn't Work, Bad Request")
    public void registerFailsRequest(){
        assert false;
    }

    @Test
    @DisplayName("Register Doesn't Work-Name in Use")
    public void registerFailsDouble(){
        assert false;
    }

}
