package net.querz;

import junit.framework.TestCase;

/**
 * Demonstration of auto return typing pattern using the simplest possible constructs for the reader
 * to grasp its operation.
 * <p>This pattern does have one key weakness, and that is if the returned type does not match the
 * expected type a ClassCastException is thrown from the call site. There is no way for the auto
 * function, {@code create(String)} below, to trap this exception and add details or hints.
 * The caller needs to be aware of this.</p>
 */
public class AutoTypingDemoTest extends TestCase {

    private static abstract class Base {public abstract String str();}
    private static class ImplA extends Base {public String str() {return "A";}}
    private static class ImplB extends Base {public String str() {return "B";}}

    @SuppressWarnings("unchecked")
    private <T extends Base> T create(String hint) {
        if ("A".equals(hint)) return (T) new ImplA();
        if ("B".equals(hint)) return (T) new ImplB();
        throw new IllegalArgumentException();
    }

    public void testAutoReturnTyping_directAssignmentDemo() {
        ImplA a = create("A");
        ImplB b = create("B");
        try {
            ImplA bad = create("B");
            fail();
        } catch (ClassCastException expected) {
            // Note, it's not possible to trap and clarify this exception within #create(String)
            // Ideally I'd like to be able to provide the caller with a more helpful message / hint for correction
            // but this is a shortcoming of the pattern
        }
    }

    public void testAutoReturnTyping_handlingReturnValueWhenCallerDoesntKnowTheTypeAtTimeOfCall() {
        ImplA a;
        ImplB b;
        Base unknown = create("A");

        if (unknown instanceof ImplA) a = (ImplA) unknown;
        else if (unknown instanceof ImplB) b = (ImplB) unknown;
        else throw new UnsupportedOperationException();  // or ignore it, or whatever you want
    }
}
